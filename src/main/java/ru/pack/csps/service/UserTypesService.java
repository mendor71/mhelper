//package ru.pack.csps.service;
//
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.pack.csps.entity.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class UserTypesService {
//    @Autowired
//    private CRUDService crudService;
//
//    public UserTypes getCurrentUserType(String userName, Session dbSession) {
//        Users creator = (Users) crudService.findSingleResult(dbSession, "Users.findByUserName", "userName", userName);
//
//        List<Integer> paramsId = new ArrayList<Integer>();
//        paramsId.add(1);
//
//        List<InvestLoanRequests> loans = crudService.findResultListWithParamsList(dbSession
//                ,"Users.getLoans"
//                , new String[] {"userId","stateList"}
//                , new Object[] {creator.getUserId(), new Integer[] {12,13,14}}, paramsId);
//
//        List<InvestLoanRequests> invests = crudService.findResultListWithParamsList(dbSession
//                ,"Users.getInvests"
//                , new String[] {"userId","stateList"}
//                , new Object[] {creator.getUserId(), new Integer[] {12,13,14}}, paramsId);
//
//        if (invests.size() != 0) {
//            return UserTypes.ROLE_INVESTOR;
//        } else if (loans.size() != 0) {
//            return UserTypes.ROLE_BORROWER;
//        } else {
//            return UserTypes.ROLE_FREEMAN;
//        }
//    }
//}
