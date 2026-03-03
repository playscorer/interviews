package com.betterment.position;

import com.betterment.account.Account;
import com.betterment.security.Security;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PositionsTest {

    @Test
    public void currentPositions() {
        Security s1 = new Security("ticker1", "company 1");
        Security s2 = new Security("ticker2", "company 2");

        Account account = new Account("test");
        account.addPositionChange(s1, 3.0, LocalDate.now());
        account.addPositionChange(s1, 4.0, LocalDate.now());
        account.addPositionChange(s2, 5.0, LocalDate.now());
        account.addPositionChange(s2, 5.0, LocalDate.now().plusMonths(1));
        account.addPositionChange(s2, 5.0, LocalDate.now().minusMonths(1));

        Map<Security, Double> currentPositions = account.getPositionsAsOf(LocalDate.now());
        assertTrue(currentPositions.get(s1).compareTo(7.0) == 0);
        assertTrue(currentPositions.get(s2).compareTo(10.0) == 0);

        currentPositions = account.getPositionsAsOf(LocalDate.now().plusMonths(1));
        assertTrue(currentPositions.get(s1).compareTo(7.0) == 0);
        assertTrue(currentPositions.get(s2).compareTo(15.0) == 0);
    }
}
