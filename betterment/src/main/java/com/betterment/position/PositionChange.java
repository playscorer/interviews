package com.betterment.position;

import com.betterment.security.Security;

import java.time.LocalDate;

public class PositionChange {
    private Security security;
    private LocalDate date;
    private double change;

    public PositionChange(Security security, LocalDate date, double change) {
        this.security = security;
        this.date = date;
        this.change = change;
    }

    public Security getSecurity() {
        return security;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getChange() {
        return change;
    }
}
