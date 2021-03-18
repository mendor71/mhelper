package ru.pack.csps.util;

import ru.pack.csps.entity.Deals;
import ru.pack.csps.entity.Users;

public class SimpleCommissionPolicy implements ICommissionPolicy {
    private Double staticPercent;

    public SimpleCommissionPolicy(Double staticPercent) {
        this.staticPercent = staticPercent;
    }

    @Override
    public double getCommissionPercent(Deals deals, Users borrowerUser) {
        return staticPercent;
    }
}
