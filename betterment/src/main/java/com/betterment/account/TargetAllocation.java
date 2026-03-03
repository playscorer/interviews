package com.betterment.account;

import com.betterment.security.Security;

public class TargetAllocation {

    private Security security;
    private double percent;

    public TargetAllocation(Security security, double percent) {
        this.security = security;
        this.percent = percent;
    }

    public Security getSecurity() {
        return security;
    }

    public double getPercent() {
        return percent;
    }
}
