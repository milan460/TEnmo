package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int id;
    private int userId;
    private BigDecimal balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id == account.id && userId == account.userId && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, balance);
    }
}
