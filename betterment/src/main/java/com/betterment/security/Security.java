package com.betterment.security;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class Security {
    private String symbol;
    private String displayName;
    private Collection<SecurityPrice> securityPrices = new ArrayList<>();

    public Security(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void addPrice(LocalDate date, double price) {
        securityPrices.add(new SecurityPrice(price, date));
    }

    public double getPriceOn(LocalDate date) {
        return securityPrices.stream()
                .filter(s -> s.getDate().equals(date))
                .findFirst()
                .map(SecurityPrice::getPrice)
                .orElseThrow(() -> new IllegalStateException(String.format("Missing price for %s on %s", getSymbol(), date)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return symbol.equals(security.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
