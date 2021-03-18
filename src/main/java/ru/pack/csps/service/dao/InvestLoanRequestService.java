//package ru.pack.csps.service.dao;
//
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import ru.pack.csps.entity.*;
//import ru.pack.csps.repository.*;
//import ru.pack.csps.util.DATEUtil;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static ru.pack.csps.util.JSONResponse.*;
//
//@Service
//public class InvestLoanRequestService {
//    @Autowired private UsersRepository usersRepository;
//    @Autowired private InvestLoanRequestsRepository ilrRepository;
//    @Autowired private StatesRepository statesRepository;
//    @Autowired private ClosureCodesRepository closureCodesRepository;
//    @Autowired private NotificationsRepository notificationsRepository;
//    @Autowired private InvestRequestsRepository irRepository;
//    @Autowired private RolesRepository rolesRepository;
//
//    public InvestLoanRequests getInvestLoanRequestByIlrId(Long ilrId, String currentUserName, boolean isAdmin) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        InvestLoanRequests ilr = ilrRepository.findOne(ilrId);
//
//        if (ilr == null) { return null; }
//        if (!currentUser.equals(ilr.getIlrLoanUsers())
//                && !currentUser.equals(ilr.getIlrInvestRequests().getIrInitUserId()) && !isAdmin) {
//            throw new AccessDeniedException("Недостаточно привилегий");
//        }
//
//        return ilr;
//    }
//
//    public List<InvestLoanRequests> getInvestLoanRequestsByLoanUserId(Long userId, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users ilrUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (ilrUser == null) { return new ArrayList<>(); }
//        if (!ilrUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return ilrRepository.findInvestLoanRequestsByIlrLoanUsersUserId(ilrUser.getUserId());
//    }
//
//    public List<InvestLoanRequests> getInvestLoanRequestsByLoanUserIdAndIlrStateId(Long userId, Integer stateId, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users ilrUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (ilrUser == null) { return new ArrayList<>(); }
//        if (!ilrUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return ilrRepository.findInvestLoanRequestsByIlrLoanUsersUserIdAndIlrStateIdStateId(ilrUser.getUserId(), stateId);
//    }
//
//    public List<InvestLoanRequests> getInvestLoanRequestsByInvestUserId(Long userId, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users ilrIrInitUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (ilrIrInitUser == null) { return new ArrayList<>(); }
//        if (!ilrIrInitUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
//
//        return ilrRepository.findInvestLoanRequestsByIlrInvestRequestsIrInitUserIdUserId(ilrIrInitUser.getUserId());
//    }
//
//    public List<InvestLoanRequests> getInvestLoanRequestsByInvestUserIdAndIlrStateId(Long userId, Integer stateId, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        Users ilrIrInitUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (ilrIrInitUser == null) { return new ArrayList<>(); }
//        if (!ilrIrInitUser.equals(currentUser)) { return new ArrayList<>(); }
//
//        return ilrRepository.findInvestLoanRequestsByIlrInvestRequestsIrInitUserIdUserIdAndIlrStateIdStateId(ilrIrInitUser.getUserId(),stateId);
//    }
//
//    public JSONAware setSolution(Long ilrId, Boolean confirm, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//        InvestLoanRequests ilr = ilrRepository.findOne(ilrId);
//
//        if (ilr == null) { return createNotFoundResponse("Сделка не найдена по ID: " + ilrId); }
//        if (!currentUser.equals(ilr.getIlrInvestRequests().getIrInitUserId())) { return createAccessDeniedResponse("Недостаточно прав для обновления объекта"); }
//
//        InvestRequests ir = ilr.getIlrInvestRequests();
//
//        if (ir == null) { return createNotFoundResponse("Для сделки не определна заявка на инвестирование: " + ilrId); }
//
//        States ilrStates;
//        States irStates;
//
//        if (confirm) {
//            Date deadLine = DATEUtil.addDays(new Date(), ilr.getIlrTerm());
//
//            ilrStates = statesRepository.findStatesByStateName("request.in_work");
//            irStates = ilrStates;
//
//            ilr.setIlrInvestorConfirmed(true);
//            ilr.setIlrDeadLine(deadLine);
//            ir.setIrLoanDeadline(deadLine);
//        } else {
//            ilrStates = statesRepository.findStatesByStateName("request.closed");
//            irStates = statesRepository.findStatesByStateName("request.registrated");
//
//            ilr.setIlrClosureCodeId(closureCodesRepository.findClosureCodesByCloCodeName("rejected_by_investor"));
//            ilr.setIlrInvestorConfirmed(false);
//        }
//
//        ilr.setIlrStateId(ilrStates);
//        ilrRepository.save(ilr);
//
//        ir.setIrStateId(irStates);
//        irRepository.save(ir);
//
//        Notifications borrowerNotification = new Notifications(
//                0
//                , "Согласование по Вашей сделке завершено с результатом: " + (confirm ? "\"Согласована\"" : "\"Отказана\"")
//                , new Date()
//                , ilr.getIlrLoanUsers()
//                , statesRepository.findStatesByStateName("not_read"));
//        notificationsRepository.save(borrowerNotification);
//
//        return createOKResponse("Состояние сделки " + ilrId + " успешно изменено на: " + (confirm ? "\"Согласована\"" : "\"Отказана\""));
//    }
//
//
//    public JSONAware setInvestRequestRelation(Long irId, Long userId, String currentUserName) {
//        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
//
//        Users ilrUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
//
//        if (ilrUser == null) { return createNotFoundResponse("Пользователь не найден по ID: " + userId); }
//        if (!ilrUser.equals(currentUser)) { return createAccessDeniedResponse("Недостаточно прав для изменения объекта"); }
//        if (ilrUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR"))) {
//            return createERRResponse("Пользователи с ролью Инвестора не могут создавать заявки на займ");
//        }
//
//        InvestRequests ir = irRepository.findOne(irId);
//        if (ir == null) { return createNotFoundResponse("Заявка на инвестирование не найдена по ID: " + irId); }
//
//        InvestLoanRequests ilr = new InvestLoanRequests(ir.getIrInvestAmount()
//                ,ir.getIrRefundAmount()
//                ,ir.getIrLoanTerm()
//                ,new Date()
//                ,ilrUser
//                ,ir
//                ,statesRepository.findStatesByStateName("request.on_confirm"));
//        ilr.setIlrBorrowerConfirmed(true);
//        ilrRepository.save(ilr);
//
//        ir.setIrStateId(statesRepository.findStatesByStateName("request.on_confirm"));
//        irRepository.save(ir);
//
//        Notifications notifications = new Notifications(
//                0
//                , "Запрошено согласование по Вашей заявке. ID сделки: " + ilr.getIlrId() + ". Подробная информация в разделе \"Мои сделки\"."
//                , new Date()
//                , ir.getIrInitUserId()
//                , statesRepository.findStatesByStateName("not_read")
//        );
//
//        notificationsRepository.save(notifications);
//        ilrUser.getRoleList().add(rolesRepository.findRolesByRoleName("ROLE_BORROWER"));
//
//        usersRepository.save(ilrUser);
//
//        return createOKResponse("Сделка направлена на согласование инвестору, отслеживать ее состояние можно в разделе \"Мои сделки\"");
//    }
//}
