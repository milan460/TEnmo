package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    //transfer in
    BigDecimal transferIn(BigDecimal transferAmount);
    //transfer out

    BigDecimal transferOut(BigDecimal transferAmount);
    //get account by account_id. outputs only one account
    Account getAccountById(int id);
    //get account by user_id. outputs a list of accounts
    List<Account> getAccountByUserId(int userId);
}
