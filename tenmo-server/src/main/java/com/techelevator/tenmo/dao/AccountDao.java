package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    //transfer in
    BigDecimal transfer(BigDecimal transferAmount);



    //get account by account_id. outputs only one account
    Account getAccountById(int id);
    //get account by user_id. outputs a list of accounts
    Account getAccountByUserId(int userId);
}
