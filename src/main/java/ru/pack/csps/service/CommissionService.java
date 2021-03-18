package ru.pack.csps.service;

import ru.pack.csps.entity.Deals;
import ru.pack.csps.entity.Users;
import ru.pack.csps.util.ICommissionPolicy;

import java.text.DecimalFormat;

public class CommissionService {
    private ICommissionPolicy commissionPolicy;
    private DecimalFormat df = new DecimalFormat("#.##");

    public Double getCommissionSum(Deals deals, Users borrowerUser) {
        double percent = commissionPolicy.getCommissionPercent(deals, borrowerUser);
        double dealGivenSum = deals.getDealGivenSum();
        double commission = (dealGivenSum / 100) * percent;
        return Double.parseDouble(df.format(commission));
    }

    public ICommissionPolicy getCommissionPolicy() {
        return commissionPolicy;
    }
    public void setCommissionPolicy(ICommissionPolicy commissionPolicy) {
        this.commissionPolicy = commissionPolicy;
    }
}
