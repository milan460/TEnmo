package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountDao {
    //transfer in
    BigDecimal transfer(BigDecimal transferAmount);

    //get account by account_id. outputs only one account
    Account getAccountById(int id);
    //get account by user_id. outputs a list of accounts
    Account getAccountByUserId(int userId);

    //get a list of ids with usernames
    Map<Integer,String> getListOfUserAndId(String username);
}
