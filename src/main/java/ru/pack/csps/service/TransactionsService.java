//package ru.pack.csps.service;
//
//
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.pack.csps.entity.States;
//import ru.pack.csps.entity.Transactions;
//import ru.pack.csps.gate.BankGate;
//import ru.pack.csps.repository.TransactionsRepository;
//import ru.pack.csps.util.BankResponse;
//
//import java.util.List;
//
//@Service
//public class TransactionsService implements Runnable {
//    @Autowired
//    private SettingsService settingsService;
//    @Autowired
//    private TransactionsRepository transactionsRepository;
//
//
//    @Override
//    public void run() {
//         List<Transactions> transactionsList = transactionsRepository.findByTraStateId((Integer) settingsService.getProperty("transactionWaitingState"));
//
//            for (Transactions t: transactionsList) {
//                BankResponse bankResponse = BankGate.executeTransaction(t);
//
//                if (bankResponse.isSuccess()) {
//                    t.setTraStateId((States) settingsService.getProperty("transactionDoneState")));
//                } else {
//                    t.setTraStateId((States) crudService.findSingleResult(dbSession, "States.findByStateId", "stateId", settingsService.getProperty("transactionErrorState")));
//                }
//                t.setTraHandleMessage(bankResponse.getMessage());
//
//                crudService.update(t, dbSession);
//            }
//
//    }
//}
