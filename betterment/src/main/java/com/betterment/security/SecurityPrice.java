package com.betterment.security;

import java.time.LocalDate;

public class SecurityPrice {
    private double price;
    private LocalDate date;

    public SecurityPrice(double price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }
}
