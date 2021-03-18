//package ru.pack.csps.controller.rest;
//
//import org.hibernate.Session;
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import ru.pack.csps.entity.*;
//import ru.pack.csps.service.*;
//import ru.pack.csps.util.JSONConvertorSerivce;
//import ru.pack.csps.util.JSONResponse;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@RestController
//public class IlrRequestHandlerController {
//
//    @Autowired
//    private SessionService sessionService;
//    @Autowired
//    private CRUDService crudService;
//    @Autowired
//    private UserTypesService userTypesService;
//    @Autowired
//    private JSONConvertorSerivce jsonConvertorSerivce;
//    @Autowired
//    private RespEntityService respEntityService;
//    @Autowired
//    private SettingsService settingsService;
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.GET, value = "IlrRequestHandler/byRole")
//    public ResponseEntity getByRole() {
//        Session dbSession = sessionService.getSession();
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String username = auth.getPrincipal().toString();
//            Users currentUser = (Users) crudService.findSingleResult(dbSession, "Users.findByUserName", "userName", username);
//
//            UserTypes userTypes = userTypesService.getCurrentUserType(username, dbSession);
//
//            List ilrList = new ArrayList<InvestLoanRequests>();
//
//            if (userTypes.equals(UserTypes.ROLE_INVESTOR)) {
//                for (InvestRequests ir: currentUser.getInvestRequestsList()) {
//                    ilrList.addAll(ir.getIlrList());
//                }
//            } else {
//                for (InvestLoanRequests lr: currentUser.getLoanList()) {
//                    ilrList.add(lr);
//                }
//            }
//
//            return respEntityService.createRespEntity(jsonConvertorSerivce.toJSON(ilrList), HttpStatus.OK);
//        } finally {
//            dbSession.close();
//        }
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.POST, value = "IlrRequestHandler")
//    public ResponseEntity get(@RequestParam(value = "ilrId", required =  false) Integer ilrId) {
//        Session dbSession = sessionService.getSession();
//        try {
//            JSONAware responseObject;
//            if (ilrId != null) {
//                List<InvestLoanRequests> ilrList = crudService.findResultList(dbSession, "InvestLoanRequests.findByIlrId", "ilrId", ilrId );
//                if (ilrList.size() == 0) {
//                    return respEntityService.createRespEntity(JSONResponse.createNotFoundResponse("Invest Loan Request relation not found by ID: " + ilrId), HttpStatus.NOT_ACCEPTABLE);
//                }
//                InvestLoanRequests ilr = ilrList.get(0);
//                responseObject = ilr.toJSONObject();
//            } else {
//                List ilrList = crudService.findResultList(dbSession, "InvestLoanRequests.findAll");
//                responseObject = jsonConvertorSerivce.toJSON(ilrList);
//            }
//
//            return respEntityService.createRespEntity(responseObject, HttpStatus.OK);
//        } finally {
//            dbSession.close();
//        }
//    }
//
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.POST, value = "IlrRequestHandler/setRelation")
//    public ResponseEntity setLoanRelation(@RequestParam(value = "irId", required = true) Integer irId
//            , @RequestParam(value = "ilrInvestSum", required = false) Double ilrInvestSum
//            , @RequestParam(value = "ilrRefundSum", required = false) Double ilrRefundSum
//            , @RequestParam(value = "ilrTerm", required = false) Integer ilrTerm)
//     {
//        Session dbSession = sessionService.getSession();
//
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String username = auth.getPrincipal().toString();
//            Users currentUser = (Users) crudService.findSingleResult(dbSession, "Users.findByUserName", "userName", username);
//
//            List<InvestRequests> iReqList = crudService.findResultList(dbSession, "InvestRequests.findByIrId", "irId", irId);
//            if (iReqList.size() == 0) {
//                return respEntityService.createRespEntity(JSONResponse.createNotFoundResponse("Invest Request not found by ID: " + irId), HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            InvestRequests ir = iReqList.get(0);
//
//            States states = (States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("ilrInvestInitiatedState"));
//
//            InvestLoanRequests ilr = new InvestLoanRequests();
//            ilr.setIlrInvestRequests(ir);
//            ilr.setIlrLoanUsers(currentUser);
//            ilr.setIlrStateId(states);
//            ilr.setIlrInvestSum(ilrInvestSum != null ? ilrInvestSum : ir.getIrInvestAmount());
//            ilr.setIlrRefundSum(ilrRefundSum != null ? ilrRefundSum : ir.getIrRefundAmount());
//            ilr.setIlrTerm(ilrTerm != null ? ilrTerm : ir.getIrLoanTerm());
//            ilr.setIlrBorrowerConfirmed(true);
//            ilr.setIlrInvestorConfirmed(false);
//            ilr.setIlrRegDate(new Date());
//
//            crudService.create(ilr, dbSession);
//
//            ir.setIrStatId((States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("investRequestTransactionConcludedState")));
//            crudService.update(ir, dbSession);
//
//            Notifications notifications = new Notifications();
//            notifications.setNotifStateId((States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("notifNotReadState")));
//            notifications.setNotifDate(new Date());
//            notifications.setNotifType(0);
//            notifications.setNotifText(notifStringBuilder((String) settingsService.getProperty("onConfirmNotification"), ilr.getIlrId(), ilr.getIlrLoanUsers().getUserId(), ilr.getIlrInvestRequests().getIrId()));
//            notifications.setNotifUserId(ir.getIrInitUserId());
//
//            crudService.create(notifications, dbSession);
//
//
//            /*TODO TRANSACTIONS!*/
//
//            return respEntityService.createRespEntity(ilr.toJSONObject(), HttpStatus.OK);
//        } finally {
//            dbSession.close();
//        }
//    }
//
//
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.POST, value = "IlrRequestHandler/refund")
//    public ResponseEntity setRelationState(@RequestParam(value = "ilrId", required = true) Integer ilrId) {
//        Session dbSession = sessionService.getSession();
//        try {
//            List<InvestLoanRequests> reqList = crudService.findResultList(dbSession, "InvestLoanRequests.findByIlrId", "ilrId", ilrId);
//            if (reqList.size() == 0) {
//                return respEntityService.createRespEntity(JSONResponse.createNotFoundResponse("Invest - Loan Requests  not found by ID: " + ilrId), HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            InvestLoanRequests ilr = reqList.get(0);
//
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String username = auth.getPrincipal().toString();
//            Users currentUser = (Users) crudService.findSingleResult(dbSession, "Users.findByUserName", "userName", username);
//
//            if (!currentUser.getUserId().equals(ilr.getIlrLoanUsers().getUserId())) {
//                return respEntityService.createRespEntity(JSONResponse.createNotFoundResponse("Current user have not permissions to change that object"), HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            States state = (States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("ilrRefundInitiatedState"));
//
//            Transactions transactions = new Transactions();
//            transactions.setTraCreated(new Date());
//            transactions.setTraFromUserId(ilr.getIlrLoanUsers());
//            transactions.setTraToUserId(ilr.getIlrInvestRequests().getIrInitUserId());
//            transactions.setTraIlrId(ilr);
//            transactions.setTraStateId((States) crudService.findResultList(dbSession, "States.findByStateId", "stateId",  settingsService.getProperty("transactionWaitingState")));
//            transactions.setTraSum(ilr.getIlrInvestRequests().getIrRefundAmount());
//            transactions.setTraType(Transactions.TYPE.FROM_BORROWER_TO_INVESTOR.toString());
//
//            crudService.create(true, dbSession);
//
//            ilr.setIlrStateId(state);
//            crudService.update(ilr, dbSession);
//
//            InvestRequests ir = ilr.getIlrInvestRequests();
//            ir.setIrStatId(state);
//
//            crudService.update(ir, dbSession);
//
//            States notifState = (States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("notifNotReadState"));
//            Notifications notif = new Notifications(0, notifStringBuilder((String) settingsService.getProperty("refundNotification"), ilr.getIlrId(), ilr.getIlrLoanUsers().getUserId(), ilr.getIlrInvestRequests().getIrId()) , new Date(), ilr.getIlrInvestRequests().getIrInitUserId(), notifState);
//
//            crudService.create(notif, dbSession);
//
//            return respEntityService.createRespEntity(ilr.toJSONObject(), HttpStatus.OK);
//        } finally {
//            dbSession.close();
//        }
//    }
//
//    private String notifStringBuilder(String mainText, Integer ilrId, Integer lrId, Integer irId) {
//        return mainText + " ID: " + ilrId + " (ID Заявки на инвестирование: " + irId + ", ID Заявки на займ: " + lrId + ")";
//    }
//}
