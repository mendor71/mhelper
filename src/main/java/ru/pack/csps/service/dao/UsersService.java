package ru.pack.csps.service.dao;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ru.pack.csps.entity.*;
import ru.pack.csps.exceptions.FileUploadException;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.NotUniqueUserNameException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.*;
import ru.pack.csps.service.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static ru.pack.csps.util.JSONResponse.createOKResponse;

@Service
public class UsersService {
    private UsersRepository usersRepository;
    private StatesRepository statesRepository;
    private RolesRepository rolesRepository;
    private SettingsService settingsService;
    private EncryptorService encryptorService;
    private NotificationsRepository notificationsRepository;
    private UserCustomFieldsRepository ucfRepository;
    private UsersPassportsRepository passportsRepository;
    private PasswordEncoder passwordEncoder;
    private FileCopyService fileCopyService;
    private MailService mailService;
    private PassportsService passportsService;

    public boolean userExists(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("???????????????? userName ???? ?????????? ?????????? ???????????????? null");
        }
        Users user = usersRepository.findUsersByUserName(userName);
        return user != null;
    }

    public Users getUserById(Long userId, String currentUserName, boolean isAdmin) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException, PropertyFindException, InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users user = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (user == null) {
            return null;
        }
        if (!currentUser.equals(user) && !isAdmin) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }
        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(user);
        }
        return user;
    }

    public Users getUserByUserName(String userName, String currentUserName, boolean isAdmin) throws InvalidValueException, PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users user = userName.equals(currentUserName) ? currentUser : usersRepository.findUsersByUserName(userName);
        if (user == null) {
            return null;
        }
        if (!currentUser.equals(user) && !isAdmin) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }
        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(user);
        }
        return user;
    }

    public Users getUserByPasswordResetToken(String token) throws InvalidValueException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        token = encryptorService.encrypt(token);
        Users users = usersRepository.findByUserPwdRestoreUUID(token);

        if (users == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? token");
        }

        if (new Date().after(users.getUserPwdRestoreUUIDExpiryDate())) {
            throw new InvalidValueException("???????? ???????????????? ?????????????????? ???????????? ??????????");
        }

        return users;
    }

    public List<Roles> getUserRolesByUserId(Long userId, String currentUserName) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users users = userId == -1
                ? currentUser
                : usersRepository.findOne(userId);

        return users == null ? new ArrayList() : users.getRoleList();
    }

    public List<Users> getUsersByStateId(Integer stateId, boolean isAdmin) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException, PropertyFindException {
        List<Users> users = usersRepository.findUserByUserStateIdStateId(stateId);

        if (!isAdmin) {
            return new ArrayList();
        }

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt((List) users);
        }
        return users;
    }

    public States getUserState(Long userId, String currentUserName) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (users == null) {
            return null;
        }
        return users.getUserStateId();
    }

    public Roles getCurrentUserDealsRole(Long userId, String currentUserName, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users user = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (user == null) { return null; }

        if (!user.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("???????????????????????? ????????????????????"); }

        return getCurrentUserDealsRole(user);
    }

    public Roles getCurrentUserDealsRole(Users user) throws InvalidValueException {
        boolean investor = false;
        boolean borrower = false;

        if (user.getRoleList().stream().noneMatch(v -> v.getRoleName().equals("ROLE_USER")))
            throw new InvalidValueException("???????????????????????? ?????????????????? ??????????! ???????????????????????? " + user.getUserName() + " ???? ?????????????????? ???????? ????????????????????????!");

        if (user.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR")))
            investor = true;
        if (user.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER")))
            borrower = true;

        if (!investor && !borrower) {
            return user.getRoleList().stream().filter(v -> v.getRoleName().equals("ROLE_USER")).findFirst().get();
        } else if (investor && !borrower) {
            return user.getRoleList().stream().filter(v -> v.getRoleName().equals("ROLE_INVESTOR")).findFirst().get();
        } else if (!investor && borrower) {
            return user.getRoleList().stream().filter(v -> v.getRoleName().equals("ROLE_BORROWER")).findFirst().get();
        } else {
            throw new InvalidValueException("???????????????????????? ?????????????????? ??????????! ???????????????????????? " + user.getUserName() + " ???????????????????????? ?????????????????? ???????? ?????????????????? ?? ????????????????!");
        }
    }

    public boolean checkPassportUploaded(Long userId, Integer pageId, String currentUserName, boolean isAdmin) throws IOException, InvalidValueException {
        if (pageId != 1 && pageId != 2) {
            throw new IllegalArgumentException("?????????????? ???? ?????????????????????????????? ???????????????????? ???????????????? ???????????????? ?? ID: " + pageId);
        }

        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users requestedUser = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (requestedUser == null) {
            throw new InvalidValueException("???????????????????????? ???? ???????????? ???? ID: " + userId);
        }
        if (!requestedUser.equals(currentUser) && !isAdmin) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }

        UserCustomFields ucf = requestedUser.getUserCustomFields();
        if (ucf == null) {
            throw new InvalidValueException("???????????????????? ???????????? ???? ???????????????????? ?????? ????????????????????????: " + userId);
        }

        return (pageId != 1 || ucf.getUcfP1FileFormat() != null) && (pageId != 2 || ucf.getUcfP2FileFormat() != null);
    }

    public void getPassportPage(Long userId, Integer pageId, HttpServletResponse response, String currentUserName, boolean isAdmin) throws IOException, PropertyFindException, InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users userToRequest = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (!currentUser.equals(userToRequest) && !isAdmin) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }

        UserCustomFields ucf = userToRequest.getUserCustomFields();
        if (ucf == null) {
            throw new InvalidValueException("?????? ???????????????????????? ???? ?????????????????? ?????????????????? userCustomFields");
        }

        byte[] image = null;
        String format = null;
        if (pageId == 1) {
            image = ucf.getUcfPassport1();
            format = ucf.getUcfP1FileFormat();
        } else if (pageId == 2) {
            image = ucf.getUcfPassport2();
            format = ucf.getUcfP2FileFormat();
        }

        if (image == null) {
            throw new InvalidValueException("?????????????????????? ???????????????? ???????????????? ???? ???????? ?????????????????? ??????????????????????????");
        }

        String fileName = currentUserName + "_page_" + pageId + "." + format;
        File target = new File((String) settingsService.getProperty("tmp_dir_path") + "/" + fileName);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        fileCopyService.writeUserImage(image, target, format, response.getOutputStream());
    }

    public List<Notifications> getAllUserNotifications(Long userId, String currentUserName) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) {
            throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName");
        }

        Users findUser = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (!currentUser.equals(findUser)) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }

        return notificationsRepository.findNotificationsByNotifUserIdUserId(findUser.getUserId());
    }

    public Users create(Users user) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException, NotUniqueUserNameException, InvalidValueException {
        if (user.getUserName() == null || user.getUserName().length() == 0) {
            throw new InvalidValueException("???????? userName ???????????? ???????? ???????????????????? ?????? ????????????????????????");
        }

        if (user.getUserPassword() == null || user.getUserPassword().length() == 0) {
            throw new InvalidValueException("???????? userPassword ???????????? ???????? ???????????????????? ?????? ????????????????????????");
        }

        if (user.getUserCustomFields() == null) {
            throw new InvalidValueException("?????????????? UserCustomField ???????????? ???????? ????????????????!");
        }

        Users dbUser = usersRepository.findUsersByUserName(user.getUserName());
        if (dbUser != null) { throw new NotUniqueUserNameException("???????????????????????? ?? ?????????????????? ?????????????? ?????? ????????????????????"); }

        dbUser = usersRepository.findByUserCustomFieldsUcfMail(encryptorService.encrypt(user.getUserCustomFields().getUcfMail()));
        if (dbUser != null) { throw new NotUniqueUserNameException("???????????????????????? ?? ?????????????????? ?????????????? ?????????????????????? ?????????? ?????? ????????????????????"); }

        UserCustomFields dbUcf = new UserCustomFields();
        dbUcf = ucfRepository.save(dbUcf);

        UserCustomFields ucf = user.getUserCustomFields();
        ucf.setUcfId(dbUcf.getUcfId());

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setUserStateId(statesRepository.findOne((Integer) settingsService.getProperty("user_default_state_id")));
        user.setUserRegCreated(new Date());
        user.setUserRate((Double) settingsService.getProperty("default_user_rate"));

        Roles defaultRole = rolesRepository.findOne((Integer) settingsService.getProperty("user_default_role_id"));

        user.setRoleList(Collections.singletonList(defaultRole));

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(user);
        }

        user.setUserCustomFields(ucf);
        ucfRepository.save(ucf);
        return usersRepository.save(user);
    }

    public void uploadPassport(Long userId, Integer pageId
            , MultipartHttpServletRequest fileRequest, String currentUserName) throws IOException, PropertyFindException, InvalidValueException, FileUploadException {
        if (pageId != 1 && pageId != 2) { throw new IllegalArgumentException("?????????????? ???? ?????????????????????????????? ???????????????????? ???????????????? ???????????????? ?? ID: " + pageId); }

        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? currentUserName"); }

        Users userToChange = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (!currentUser.equals(userToChange)) { throw new AccessDeniedException("???????????????????????? ????????????????????"); }

        if (fileRequest.getMultiFileMap() == null || fileRequest.getMultiFileMap().entrySet().size() == 0) {
            throw new IllegalArgumentException("???? ?????????????? ???????? ??????????/???????????????????? ????????????????");
        }

        for (Map.Entry<String, List<MultipartFile>> fileEntry : fileRequest.getMultiFileMap().entrySet()) {
            LinkedList fileList = (LinkedList) fileEntry.getValue();
            CommonsMultipartFile fileItem = (CommonsMultipartFile) fileList.get(0);

            String newFileName = settingsService.getProperty("tmp_dir_path") + currentUserName + "_page_1" + fileItem.getOriginalFilename();

            File tmpFile = new File(newFileName);
            try {
                fileItem.getFileItem().write(tmpFile);
            } catch (Exception e) {
                throw new FileUploadException("???????????????? ?????????? " + fileItem.getOriginalFilename() + " ???? ??????????????????, ?????????????????? ?????????????? ??????????.", e);
            }

            UserCustomFields ucfToChange = userToChange.getUserCustomFields() != null ? userToChange.getUserCustomFields() : new UserCustomFields();
            if (pageId == 1) {
                ucfToChange.setUcfPassport1(newFileName);
                ucfToChange.setUcfP1FileFormat(getImageFormat(newFileName));
            } else {
                ucfToChange.setUcfPassport2(newFileName);
                ucfToChange.setUcfP2FileFormat(getImageFormat(newFileName));
            }
            ucfRepository.save(ucfToChange);
            tmpFile.delete();
        }
    }

    public JSONAware change(Long userId, Users users, String currentUserName, boolean isAdmin) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        Users userToChange = usersRepository.findOne(userId);

        if (!userToChange.equals(currentUser) && !isAdmin) {
            throw new AccessDeniedException("???????????????????????? ????????????????????");
        }

        userToChange.setUserLastName(users.getUserLastName());
        userToChange.setUserFirstName(users.getUserFirstName());
        userToChange.setUserMiddleName(users.getUserMiddleName());

        UserCustomFields ucfToChange = null;
        UserPassports passportsToChange = null;

        if (users.getUserCustomFields() != null) {
            ucfToChange = userToChange.getUserCustomFields() != null ? userToChange.getUserCustomFields() : new UserCustomFields();
            ucfToChange.setUcfMail(users.getUserCustomFields().getUcfMail());
            ucfToChange.setUcfPhone(users.getUserCustomFields().getUcfPhone());
            ucfToChange.setUcfAddress(users.getUserCustomFields().getUcfAddress());
            ucfToChange.setUcfBirthDate(users.getUserCustomFields().getUcfBirthDate());
            userToChange.setUserCustomFields(ucfToChange);
        }

        if (users.getUserPassports() != null) {
            passportsToChange = userToChange.getUserPassports() != null ? userToChange.getUserPassports() : new UserPassports();
            passportsToChange.setUpSeries(users.getUserPassports().getUpSeries());
            passportsToChange.setUpNumber(users.getUserPassports().getUpNumber());
            passportsToChange.setUpGivenBy(users.getUserPassports().getUpGivenBy());
            passportsToChange.setUpGivenDate(users.getUserPassports().getUpGivenDate());
            passportsToChange.setUpLocationAddress(users.getUserPassports().getUpLocationAddress());
            userToChange.setUserPassports(passportsToChange);
        }

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(userToChange);
        }

        if (passportsToChange != null)
            passportsRepository.save(passportsToChange);
        if (ucfToChange != null)
            ucfRepository.save(ucfToChange);

        usersRepository.save(userToChange);
        return createOKResponse("???????????? ???????????????????????? ?????????????? ??????????????????");
    }

    public void createRestorePasswordUrl(String mail, String requestUrl) throws InvalidValueException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MessagingException {
        String encodedMail = encryptorService.encrypt(mail);

        Users users = usersRepository.findByUserCustomFieldsUcfMail(encodedMail);
        if (users == null) { throw new InvalidValueException("???????????????????????? ???? ???????????? ???? ???????????? ?????????????????????? ??????????: " + mail); }

        String uuid = UUID.randomUUID().toString();
        Date expiryDate = new Date(new Date().getTime() + 3600000);

        users.setUserPwdRestoreUUID(encryptorService.encrypt(uuid));
        users.setUserPwdRestoreUUIDExpiryDate(expiryDate);

        usersRepository.save(users);

        String messageBody = "<html><body><h3>???????????? ????????!</h3>" +
                "<p>?????? ???????????????????????????? ???????????? ?????????????????? ???? ????????????: " + requestUrl + "?token=" + uuid +"</p>" +
                "<p>?????????? ???????????????? ???????????? 1 ??????</p></body><html>";

        mailService.send(mail, "???????????????????????????? ???????????? ?????????????? MoneyHelper", messageBody); //TODO change subject;
    }

    public Users changeUserPassword(String newPassword, String userName) throws InvalidValueException {
        Users user = usersRepository.findUsersByUserName(userName);
        if (user == null) { throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? userName"); }

        user.setUserPassword(passwordEncoder.encode(newPassword));
        return usersRepository.save(user);
    }

    public Users changeUserPassword(String oldPassword, String newPassword, String userName) throws InvalidValueException {
        Users user = usersRepository.findUsersByUserName(userName);
        if (user == null) { throw new InvalidValueException("???????????????? ???????????????????????? ???????????????? ?????????????????? userName"); }

        if (!passwordEncoder.encode(oldPassword).equals(user.getUserPassword())) {
            throw new InvalidValueException("?????????????? ???????????? ???????????? ??????????????????????");
        }

        user.setUserPassword(passwordEncoder.encode(newPassword));
        return usersRepository.save(user);
    }



    private String getImageFormat(String origImageName) {
        if (origImageName == null) {
            return "UNKNOWN";
        }
        String imageName = origImageName.toLowerCase();
        if (imageName.endsWith(".png")) {
            return "png";
        }
        if (imageName.endsWith(".jpg")) {
            return "jpg";
        }
        if (imageName.endsWith(".jpeg")) {
            return "jpeg";
        }
        if (imageName.endsWith(".gif")) {
            return "gif";
        }
        if (imageName.endsWith(".tiff")) {
            return "tiff";
        }
        if (imageName.endsWith(".bmp")) {
            return "bmp";
        }
        return "UNKNOWN";
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setStatesRepository(StatesRepository statesRepository) {
        this.statesRepository = statesRepository;
    }

    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Autowired
    public void setEncryptorService(EncryptorService encryptorService) {
        this.encryptorService = encryptorService;
    }

    @Autowired
    public void setNotificationsRepository(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Autowired
    public void setUcfRepository(UserCustomFieldsRepository ucfRepository) {
        this.ucfRepository = ucfRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setFileCopyService(FileCopyService fileCopyService) {
        this.fileCopyService = fileCopyService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setPassportsService(PassportsService passportsService) {
        this.passportsService = passportsService;
    }
    @Autowired
    public void setPassportsRepository(UsersPassportsRepository passportsRepository) {
        this.passportsRepository = passportsRepository;
    }
}
