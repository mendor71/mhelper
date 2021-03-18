package ru.pack.csps.gate;

import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Transactions;
import ru.pack.csps.util.BankResponse;

public class BankGate {

    public static BankResponse executeTransaction(Transactions transactions) {
        /*TODO write CODE!*/

        return new BankResponse(true, "transaction " + transactions.getTraId() + " handled");
    }

}
