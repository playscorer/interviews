package com.betterment.deposit;

import com.betterment.account.Account;
import com.betterment.security.Security;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DepositServiceTest {
    private Security google;
    private Security apple;
    private Security facebook;

    private DepositService depositService = new DepositService();

    @Before
    public void beforeEach() {
        google = new Security("GOOG", "Google");
        apple = new Security("AAPL", "Apple");
        facebook = new Security("FB", "Facebook");
    }

    @Test
    public void onToday_testExampleDeposit() {
        LocalDate today = LocalDate.now();

        // Set up test account
        Account account = new Account("Test account");

        // 50/50 portfolio of google & apple
        account.addTargetAllocation(google, 0.50);
        account.addTargetAllocation(apple, 0.50);
        account.addTargetAllocation(facebook, 0);

        google.addPrice(today, 1.0);
        google.addPrice(today.plusMonths(1), 3.0);

        apple.addPrice(today, 2.0);
        apple.addPrice(today.plusMonths(1), 2.0);

        facebook.addPrice(today, 3.0);
        facebook.addPrice(today.plusMonths(1), 3.0);

        // Perform $100 deposit -> $50 to buy google and $50 to buy apple
        depositService.oneTimeDeposit(account, 100.0, today);
        Map<Security, Double> currentPositions = account.getPositionsAsOf(today);

        // Assert new positions are correct
        assertEquals(2, currentPositions.size());
        assertEquals(50.0, currentPositions.get(google), 0);
        assertEquals(25.0, currentPositions.get(apple), 0);

        // Perform $100 deposit -> $0 to google, $100 to buy apple
        depositService.oneTimeDeposit(account, 100.0, today.plusMonths(1));
        currentPositions = account.getPositionsAsOf(today.plusMonths(1));

        // Assert new positions are correct
        assertEquals(2, currentPositions.size());
        assertEquals(50.0, currentPositions.get(google), 0);
        assertEquals(75.0, currentPositions.get(apple), 0);
    }
}
