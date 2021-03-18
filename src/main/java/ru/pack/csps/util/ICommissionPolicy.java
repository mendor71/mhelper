package ru.pack.csps.util;

import ru.pack.csps.entity.Deals;
import ru.pack.csps.entity.Users;

public interface ICommissionPolicy {
    double getCommissionPercent(Deals deals, Users borrowerUser);
}
