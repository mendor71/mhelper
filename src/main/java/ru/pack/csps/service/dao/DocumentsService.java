package ru.pack.csps.service.dao;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.*;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.*;
import ru.pack.csps.service.*;
import ru.pack.csps.util.CompanyProperties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentsService {
    private DocumentsTypesRepository documentsTypesRepository;
    private DealsDocumentsRepository dealsDocumentsRepository;
    private UserDocumentsRepository userDocumentsRepository;
    private DealsRepository dealsRepository;
    private UsersRepository usersRepository;
    private StatesRepository statesRepository;
    private CompanyProperties companyProperties;
    private SettingsService settingsService;
    private EncryptorService encryptorService;
    private PdfService pdfService;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private SmsService smsService;
    private CodeGeneratorService codeGeneratorService;
    private BCryptPasswordEncoder passwordEncoder;
    private UsersService usersService;

    public DocumentsTypes getDocumentTypeById(Integer dtId) throws InvalidValueException {
        DocumentsTypes documentsTypes = documentsTypesRepository.findOne(dtId);
        if (documentsTypes == null) { throw new InvalidValueException("Тип документов не найден по ID: " + dtId); }
        return documentsTypes;
    }

    public DocumentsTypes getDocumentTypeByName(String dtName) throws InvalidValueException {
        DocumentsTypes documentsTypes = documentsTypesRepository.findByDtName(dtName);
        if (documentsTypes == null) { throw new InvalidValueException("Тип документов не найден по наименованию: " + dtName); }
        return documentsTypes;
    }

    public DealsDocuments createDealDocument(Long dealId, String dtName, String currentUserName) throws InvalidValueException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);

        Roles currentUserDealRole = usersService.getCurrentUserDealsRole(currentUser);

        if (currentUserDealRole.getRoleName().equals("ROLE_BORROWER") && !deals.getDealState().getStateName().equals("request.registrated")) { throw new IllegalStateException("Сделка в статусе: " + deals.getDealState().getStateName() + " недоступна!"); }
        if (currentUserDealRole.getRoleName().equals("ROLE_INVESTOR") && !deals.getDealState().getStateName().equals("request.on_confirm")) { throw new IllegalStateException("Сделка в статусе: " + deals.getDealState().getStateName() + " недоступна!"); }

        deals.setDealBorrowerUser(currentUser);

        if (deals.getDealInvestorUser() == null
                || deals.getDealBorrowerUser() == null
                /*|| deals.getDealDeadLine() == null
                || deals.getDealCommission() == null*/)
            throw new IllegalStateException("Некорректное состояние сделки: " + dealId + " заемщик, инвестор, дата возврата и комиссия должны быть определены");
        if (deals.getDealInvestorUser().getUserPassports() == null)
            throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать данные паспорта!");
        if (deals.getDealBorrowerUser().getUserPassports() == null)
            throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать данные паспорта!");
        if (deals.getDealBorrowerUser().getUserCustomFields() == null || deals.getDealBorrowerUser().getUserCustomFields().getUcfPhone() == null)
            throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать номер телефона!");
        if (deals.getDealInvestorUser().getUserCustomFields() == null || deals.getDealInvestorUser().getUserCustomFields().getUcfPhone() == null)
            throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать номер телефона!");
        if (!currentUser.equals(deals.getDealBorrowerUser()) && !currentUser.equals(deals.getDealInvestorUser()))
            throw new AccessDeniedException("Недостаточно привилегий");

        DocumentsTypes documentsTypes = documentsTypesRepository.findByDtName(dtName);
        if (documentsTypes == null) { throw new InvalidValueException("Тип документов не найден по наименованию: " + dtName); }

        return createDealDocument(documentsTypes, deals, currentUser);
    }

    public UserDocuments createUserDocument(Long userId, String dtName, String currentUserName) throws InvalidValueException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (users == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!users.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
        if (users.getUserPassports() == null) {throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать данные паспорта!"); }
        if (users.getUserCustomFields() == null) { throw new IllegalStateException("Некорректное состояние пользователя, отсутствует экземпляр UserCustomFields!"); }
        if (users.getUserCustomFields().getUcfBirthDate() == null) { throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать дату рождения!"); }
        if (users.getUserCustomFields().getUcfPhone() == null) { throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать номер телефона!"); }

        DocumentsTypes documentsTypes = documentsTypesRepository.findByDtName(dtName);
        if (documentsTypes == null) { throw new InvalidValueException("Тип документов не найден по названию: " + dtName); }

        return createUserDocument(users, documentsTypes);
    }

    public UserDocuments createUserDocument(Users users, String dtName, String currentUserName) throws PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        users.setUserId(currentUser.getUserId());

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(users);
        }

        if (users.getUserPassports() == null) {throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать данные паспорта!"); }
        if (users.getUserCustomFields() == null) { throw new IllegalStateException("Некорректное состояние пользователя, отсутствует экземпляр UserCustomFields!"); }
        if (users.getUserCustomFields().getUcfBirthDate() == null) { throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать дату рождения!"); }
        if (users.getUserCustomFields().getUcfPhone() == null) { throw new IllegalStateException("Некорректное состояние пользователя, необходимо указать номер телефона!"); }

        DocumentsTypes documentsTypes = documentsTypesRepository.findByDtName(dtName);
        if (documentsTypes == null) { throw new InvalidValueException("Тип документов не найден по названию: " + dtName); }

        UserDocuments userDocuments = userDocumentsRepository
                .findFirstByUserDocUserAndUserDocTypeAndUserDocState(users, documentsTypes, statesRepository.findStatesByStateName("document.new"));

        if (userDocuments != null) {
            return userDocuments;
        }

        String template = documentsTypes.getDtHtmlTemplate();
        Map paramsMap = createParametersMap(users);

        String docText = createDocumentText(template, paramsMap);

        String confirmCode = codeGeneratorService.generateCode();

        userDocuments = new UserDocuments();
        userDocuments.setUserDocConfirmCode(passwordEncoder.encode(confirmCode));
        userDocuments.setUserDocUser(users);
        userDocuments.setUserDocState(statesRepository.findStatesByStateName("document.new"));
        userDocuments.setUserDocType(documentsTypes);
        userDocuments.setUserDocText(docText);
        UserDocuments doc = userDocumentsRepository.save(userDocuments);

        smsService.sendSms(users.getUserCustomFields().getUcfPhone()
                , "Код подтверждения документа " + doc.getUserDocType().getDtNameLocale() + ". " + confirmCode);
        return doc;
    }

    public void printDealDocument(Long dealDocId, String currentUserName, HttpServletResponse response) throws InvalidValueException, IOException, PropertyFindException, DocumentException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        DealsDocuments dealsDocuments = dealsDocumentsRepository.findOne(dealDocId);
        if (dealsDocuments == null) { throw new InvalidValueException("Документ не найден по ID: " + dealDocId); }

        if (!dealsDocuments.getDealDocUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (!dealsDocuments.getDealDocState().equals(statesRepository.findStatesByStateName("document.confirmed"))) {
            throw new IllegalStateException("Невозможна печать неподтвержденного документа");
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + currentUserName + "_" + dealsDocuments.getDealDocType().getDtName() + ".pdf");
        pdfService.writePdf(dealsDocuments.getDealDocText(), response.getOutputStream());

    }

    public void printUserDocument(Long userDocId, String currentUserName, HttpServletResponse response) throws InvalidValueException, IOException, PropertyFindException, DocumentException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        UserDocuments userDocuments = userDocumentsRepository.findOne(userDocId);
        if (userDocuments == null) { throw new InvalidValueException("Документ не найден по ID: " + userDocId); }

        if (!userDocuments.getUserDocUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (!userDocuments.getUserDocState().equals(statesRepository.findStatesByStateName("document.confirmed"))) {
            throw new IllegalStateException("Невозможна печать неподтвержденного документа");
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + currentUserName + "_" + userDocuments.getUserDocType().getDtName() + ".pdf");
        pdfService.writePdf(userDocuments.getUserDocText(), response.getOutputStream());
    }

    public void confirmDealDocument(Long dealDocId, String confirmCode, String currentUserName) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        DealsDocuments dealsDocuments = dealsDocumentsRepository.findOne(dealDocId);
        if (dealsDocuments == null) { throw new InvalidValueException("Документ не найден по ID: " + dealDocId); }

        if (!dealsDocuments.getDealDocUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (dealsDocuments.getDealDocState().equals(statesRepository.findStatesByStateName("document.rejected"))) {
            throw new IllegalStateException("Невозможно подтвердить отказанный документ");
        }

        if (!passwordEncoder.matches(confirmCode, dealsDocuments.getDealDocConfirmCode())) {
            throw new AccessDeniedException("Некорректный код доступа");
        }

        dealsDocuments.setDealDocConfirmed(true);
        dealsDocuments.setDealDocConfirmDate(new Date());
        dealsDocuments.setDealDocState(statesRepository.findStatesByStateName("document.confirmed"));

        dealsDocumentsRepository.save(dealsDocuments);
    }

    public void confirmUserDocument(Long userDocId, String confirmCode, String currentUserName) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректно значение парасетра currentUserName"); }

        UserDocuments userDocuments = userDocumentsRepository.findOne(userDocId);
        if (userDocuments == null) { throw new InvalidValueException("Документ не найден по ID: " + userDocId); }

        if (!userDocuments.getUserDocUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (userDocuments.getUserDocState().equals(statesRepository.findStatesByStateName("document.rejected"))) {
            throw new IllegalStateException("Невозможно подтвердить отказанный документ");
        }

        if (!passwordEncoder.matches(confirmCode, userDocuments.getUserDocConfirmCode())) {
            throw new AccessDeniedException("Некорректный код доступа");
        }

        userDocuments.setUserDocConfirmed(true);
        userDocuments.setUserDocConfirmDate(new Date());
        userDocuments.setUserDocState(statesRepository.findStatesByStateName("document.confirmed"));

        userDocumentsRepository.save(userDocuments);

        if (userDocuments.getUserDocType().getDtName().equals("personal_data_agreement")) { /*TODO!*/
            Users users = userDocuments.getUserDocUser();
            users.setUserPersonalDataAgreementConfirmed(true);
            usersRepository.save(users);
        }
    }

    private DealsDocuments createDealDocument(DocumentsTypes documentsTypes, Deals deals, Users dealUser) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, PropertyFindException {
        DealsDocuments dealsDocuments = dealsDocumentsRepository
                .findFirstByDealDocUserAndDealDocTypeAndDealDocState(dealUser, documentsTypes, statesRepository.findStatesByStateName("document.new"));

        if (dealsDocuments != null) {
            return dealsDocuments;
        }

        String template = documentsTypes.getDtHtmlTemplate();
        Map paramsMap = createParametersMap(deals);

        String docText = createDocumentText(template, paramsMap);

        String confirmCode = codeGeneratorService.generateCode();

        dealsDocuments = new DealsDocuments();
        dealsDocuments.setDealDocConfirmCode(passwordEncoder.encode(confirmCode));
        dealsDocuments.setDealDocDeal(deals);
        dealsDocuments.setDealDocState(statesRepository.findStatesByStateName("document.new"));
        dealsDocuments.setDealDocType(documentsTypes);
        dealsDocuments.setDealDocText(docText);
        dealsDocuments.setDealDocUserId(dealUser);
        DealsDocuments doc = dealsDocumentsRepository.save(dealsDocuments);

        smsService.sendSms(dealUser.getUserCustomFields().getUcfPhone()
                , "Код подтверждения документа " + doc.getDealDocType().getDtNameLocale() + " в рамках сделки ID " + deals.getDealId() + ". " + confirmCode);
        return doc;
    }

    private UserDocuments createUserDocument(Users users, DocumentsTypes documentsTypes) throws PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        UserDocuments userDocuments = userDocumentsRepository
                .findFirstByUserDocUserAndUserDocTypeAndUserDocState(users, documentsTypes, statesRepository.findStatesByStateName("document.new"));

        if (userDocuments != null) {
            return userDocuments;
        }

        String template = documentsTypes.getDtHtmlTemplate();
        Map paramsMap = createParametersMap(users);

        String docText = createDocumentText(template, paramsMap);

        String confirmCode = codeGeneratorService.generateCode();

        userDocuments = new UserDocuments();
        userDocuments.setUserDocConfirmCode(passwordEncoder.encode(confirmCode));
        userDocuments.setUserDocUser(users);
        userDocuments.setUserDocState(statesRepository.findStatesByStateName("document.new"));
        userDocuments.setUserDocType(documentsTypes);
        userDocuments.setUserDocText(docText);
        UserDocuments doc = userDocumentsRepository.save(userDocuments);

        smsService.sendSms(users.getUserCustomFields().getUcfPhone()
                , "Код подтверждения документа " + doc.getUserDocType().getDtNameLocale() + ". " + confirmCode);
        return doc;
    }

    private HashMap createParametersMap(Deals deals) throws PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        HashMap<String, String> params = new HashMap<>();

        Users investor = deals.getDealInvestorUser();
        Users borrower = deals.getDealBorrowerUser();

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(investor);
            encryptorService.decrypt(borrower);
        }

        params.put("investorUserFullName", investor.getFullName());
        params.put("investorUserLastName", investor.getUserLastName());
        params.put("investorUserFirstName", investor.getUserFirstName());
        params.put("investorUserMiddleName", investor.getUserMiddleName());
        params.put("investorUserPassportSeries", investor.getUserPassports().getUpSeries());
        params.put("investorUserPassportNumber", investor.getUserPassports().getUpNumber());
        params.put("investorUserPassportGivenBy", investor.getUserPassports().getUpGivenBy());
        params.put("investorUserPassportGivenDate", investor.getUserPassports().getUpGivenDate());
        params.put("investorUserPassportLocationAddress", investor.getUserPassports().getUpLocationAddress());

        params.put("borrowerUserFullName", borrower.getFullName());
        params.put("borrowerUserLastName", borrower.getUserLastName());
        params.put("borrowerUserFirstName", borrower.getUserFirstName());
        params.put("borrowerUserMiddleName", borrower.getUserMiddleName() != null ? borrower.getUserMiddleName() : "");
        params.put("borrowerUserPassportSeries", borrower.getUserPassports().getUpSeries());
        params.put("borrowerUserPassportNumber", borrower.getUserPassports().getUpNumber());
        params.put("borrowerUserPassportGivenBy", borrower.getUserPassports().getUpGivenBy());
        params.put("borrowerUserPassportGivenDate", borrower.getUserPassports().getUpGivenDate());
        params.put("borrowerUserPassportLocationAddress", borrower.getUserPassports().getUpLocationAddress());

        params.put("dealGivenSum", deals.getDealGivenSum().toString());
        params.put("dealRefundSum", deals.getDealRefundSum().toString());
        params.put("dealCommission", deals.getDealCommission() != null ? deals.getDealCommission().toString() : "");
        params.put("dealTermDays", deals.getDealTermDays().toString());
        params.put("dealDeadLine", deals.getDealDeadLine() != null ? dateFormat.format(deals.getDealDeadLine()) : "");

        params.put("companyName", companyProperties.getCompanyName());
        params.put("companyINN", companyProperties.getCompanyINN());
        params.put("companyOrgNum", companyProperties.getCompanyOrgNum());
        params.put("companyPhysicalAddress", companyProperties.getCompanyPhysicalAddress());
        params.put("platformName", companyProperties.getPlatformName());

        params.put("currentDate", dateFormat.format(new Date()));
        return params;
    }

    private HashMap createParametersMap(Users users) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(users);
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("userFullName", users.getFullName());
        params.put("userLastName", users.getUserLastName());
        params.put("userFirstName", users.getUserFirstName());
        params.put("userMiddleName", users.getUserMiddleName());
        params.put("userBirthDate", users.getUserCustomFields().getUcfBirthDate());
        params.put("userPassportSeries", users.getUserPassports().getUpSeries());
        params.put("userPassportNumber", users.getUserPassports().getUpNumber());
        params.put("userPassportGivenBy", users.getUserPassports().getUpGivenBy());
        params.put("userPassportGivenDate", users.getUserPassports().getUpGivenDate());
        params.put("userPassportLocationAddress", users.getUserPassports().getUpLocationAddress());

        params.put("companyName", companyProperties.getCompanyName());
        params.put("companyINN", companyProperties.getCompanyINN());
        params.put("companyOrgNum", companyProperties.getCompanyOrgNum());
        params.put("companyPhysicalAddress", companyProperties.getCompanyPhysicalAddress());
        params.put("platformName", companyProperties.getPlatformName());

        params.put("currentDate", dateFormat.format(new Date()));
        return params;
    }

    private String createDocumentText(String template, Map<String,String> parametersMap) {
        final String[] tmp = new String[] {template};
        parametersMap.forEach((k,v) -> {
            tmp[0] = tmp[0].replace("${" + k + "}", v);
        });
        return tmp[0];
    }

    @Autowired
    public void setDocumentsTypesRepository(DocumentsTypesRepository documentsTypesRepository) {
        this.documentsTypesRepository = documentsTypesRepository;
    }
    @Autowired
    public void setDealsDocumentsRepository(DealsDocumentsRepository dealsDocumentsRepository) {
        this.dealsDocumentsRepository = dealsDocumentsRepository;
    }
    @Autowired
    public void setDealsRepository(DealsRepository dealsRepository) {
        this.dealsRepository = dealsRepository;
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
    public void setUserDocumentsRepository(UserDocumentsRepository userDocumentsRepository) {
        this.userDocumentsRepository = userDocumentsRepository;
    }
    @Autowired
    public void setCompanyProperties(CompanyProperties companyProperties) {
        this.companyProperties = companyProperties;
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
    public void setPdfService(PdfService pdfService) {
        this.pdfService = pdfService;
    }
    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
    @Autowired
    public void setCodeGeneratorService(CodeGeneratorService codeGeneratorService) {
        this.codeGeneratorService = codeGeneratorService;
    }
    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}
