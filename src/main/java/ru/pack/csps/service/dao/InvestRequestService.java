//package ru.pack.csps.service.dao;
//
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import ru.pack.csps.entity.InvestRequests;
//import ru.pack.csps.entity.Users;
//import ru.pack.csps.exceptions.PropertyFindException;
//import ru.pack.csps.repository.InvestRequestsRepository;
//import ru.pack.csps.repository.RolesRepository;
//import ru.pack.csps.repository.StatesRepository;
//import ru.pack.csps.repository.UsersRepository;
//import ru.pack.csps.service.EncryptorService;
//import ru.pack.csps.service.SettingsService;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static ru.pack.csps.util.JSONResponse.*;
//
//@Service
//public class InvestRequestService {
//    @Autowired private UsersRepository usersRepository;
//    @Autowired private StatesRepository statesRepository;
//    @Autowired private InvestRequestsRepository irRepository;
//    @Autowired private RolesRepository rolesRepository;
//    @Autowired private SettingsService settingsService;
//    @Autowired private EncryptorService encryptorService;
//
//    public InvestRequests findById(Long irId, String currentUserName, boolean isAdmin) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        InvestRequests ir = irRepository.findOne(irId);
//
//        if (ir == null) {return null;}
//        if (!currentUser.equals(ir.getIrInitUserId()) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return ir;
//    }
//
//    public List<InvestRequests> findByUserId(Long userId, String currentUserName, boolean isAdmin) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users invUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (invUser == null) { return new ArrayList<>(); }
//        if (!currentUser.equals(invUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return irRepository.findInvestRequestsByIrInitUserIdUserId(invUser.getUserId());
//    }
//
//    public List<InvestRequests> findByUserIdAndStateId(Long userId, Integer stateId, String currentUserName, boolean isAdmin) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users invUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (invUser == null) { return new ArrayList<>(); }
//        if (!currentUser.equals(invUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return irRepository.findInvestRequestsByIrInitUserIdUserIdAndIrStateIdStateId(invUser.getUserId(), stateId);
//    }
//
//    public List<InvestRequests> selectInvestRequestForUser(Long userId
//            , Integer loanTerm
//            , Double loanAmount
//            , Double refundAmount
//            , String currentUserName) throws ParseException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
//
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users irForUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (irForUser == null) { return new ArrayList<>(); }
//        if (!currentUser.equals(irForUser)) { return new ArrayList<>(); }
//
//        if ((Boolean) settingsService.getProperty("encryptEnable")) {
//            encryptorService.decrypt(irForUser);
//        }
//
//        Double userRate = irForUser.getUserRate() != null ? irForUser.getUserRate() : 0;
//        Integer userAge = irForUser.getUserAge();
//        Integer appTermRange = (Integer) settingsService.getProperty("appTermRangeCriteria");
//
//        List<InvestRequests> hList = irRepository.findInvestRequestsForUser_H(loanTerm
//                , appTermRange
//                , loanAmount
//                , userAge
//                , userRate);
//
//        List<InvestRequests> lList = irRepository.findInvestRequestsForUser_L(loanTerm
//                , appTermRange
//                , loanAmount
//                , userAge
//                , userRate);
//
//        hList.addAll(lList);
//        return hList;
//    }
//
//    public JSONAware addInvestRequest(Long userId, InvestRequests investRequests, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users irUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (irUser == null) { return createNotFoundResponse("Пользователь не найден по ID: " + userId); }
//        if (!irUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
//        if (irUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER"))) {
//            return createERRResponse("Пользователи с ролью Заемщика не могут создавать заявки на инвестирование");
//        }
//
//        investRequests.setIrAgeLimitMax(investRequests.getIrAgeLimitMax() != null ? investRequests.getIrAgeLimitMax() : 150);
//        investRequests.setIrAgeLimitMin(investRequests.getIrAgeLimitMin() != null ? investRequests.getIrAgeLimitMin() : 0);
//        investRequests.setIrMinUserRate(investRequests.getIrMinUserRate() != null ? investRequests.getIrMinUserRate() : 0);
//
//        investRequests.setIrStateId(statesRepository.findStatesByStateName("request.registrated"));
//        investRequests.setIrRegCreated(new Date());
//        investRequests.setIrInitUserId(currentUser);
//
//        investRequests = irRepository.save(investRequests);
//
//        if (currentUser.getRoleList().stream().filter(v -> v.getRoleName().equals("ROLE_INVESTOR")).count() == 0) {
//            currentUser.getRoleList().add(rolesRepository.findRolesByRoleName("ROLE_INVESTOR"));
//            usersRepository.save(currentUser);
//        }
//
//        return createOKResponse("Создана заявка с ID: " + investRequests.getIrId());
//    }
//}
