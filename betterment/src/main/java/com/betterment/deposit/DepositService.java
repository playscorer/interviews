package com.betterment.deposit;

import com.betterment.account.Account;
import com.betterment.account.TargetAllocation;
import com.betterment.security.Security;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DepositService {

    /*
     * Interview with Kyle Copeland on March 3, 2026
     *
     * $100
     *
     * 50% x $100 = $50
     *
     * $50 of GOOG = $1 / share => $50 / $1 = 50 shares
     * $50 of AAPL = $2 / share => $50 / $2 = 25 shares
     *
     * GOOG -> 50 @ $3 = $150
     * AAPL -> 25 @ $2 = $50
     *
     * PF val = $200
     * +$100
     *
     * => $300
     *
     * $150 of GOOG : 50 shares total
     * $150 of AAPL => +$100 of AAPL : +50 shares of AAPL : 75 shares total
     */

    private static final Logger LOG = Logger.getLogger(DepositService.class.getName());

    public void oneTimeDeposit(Account account, double depositAmount, LocalDate depositDate) {
        LOG.info("Performing one time deposit");
        // TODO one time deposit

        Collection<TargetAllocation> targetAllocations = account.getTargetAllocations();

        // Map of Allocations
        Map<Security, Double> allocationMap = new HashMap<>();
        targetAllocations.forEach(t -> allocationMap.put(t.getSecurity(), t.getPercent()));

        // current value of portfolio
        Map<Security, Double> currentPositions = account.getPositionsAsOf(depositDate);
        double pfValue = 0;
        for (Map.Entry<Security, Double> currentPosition : currentPositions.entrySet()) {
            // In the Real Interview I forgot to multiply by the current price and that's why last test was failing
            pfValue += currentPosition.getValue() * currentPosition.getKey().getPriceOn(depositDate);
        }
        pfValue += depositAmount;

        for (Map.Entry<Security, Double> allocation : allocationMap.entrySet()) {
            double newDollarsAllocation = allocation.getValue() * pfValue;
            double newShareAllocation = newDollarsAllocation / allocation.getKey().getPriceOn(depositDate);
            double oldShareAllocation = currentPositions.getOrDefault(allocation.getKey(), 0d);
            double change = newShareAllocation - oldShareAllocation;

            if (change > 0) {
                account.addPositionChange(allocation.getKey(), change, depositDate);
            }
        }
    }
}
