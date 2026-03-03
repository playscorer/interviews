package com.betterment.account;

import com.betterment.position.PositionChange;
import com.betterment.security.Security;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Account {
    private String userName;
    private Collection<PositionChange> positionChanges = new ArrayList<>();
    private Collection<TargetAllocation> targetAllocations = new ArrayList<>();

    public Account(String userName) {
        this.userName = userName;
    }

    public Map<Security, Double> getPositionsAsOf(LocalDate date) {
        return positionChanges.stream()
                .filter(p -> p.getDate().compareTo(date) <= 0)
                .collect(toMap(PositionChange::getSecurity, PositionChange::getChange, Double::sum));
    }

    public void addPositionChange(Security security, double shares, LocalDate date) {
        positionChanges.add(new PositionChange(security, date, shares));
    }

    public Collection<TargetAllocation> getTargetAllocations() {
        return targetAllocations;
    }

    public void addTargetAllocation(Security security, double percent) {
        targetAllocations.add(new TargetAllocation(security, percent));
    }
}
