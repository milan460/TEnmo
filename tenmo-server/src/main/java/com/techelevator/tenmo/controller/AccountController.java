package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    //create a path for id
    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public Account account(@PathVariable int id){
        return accountDao.getAccountById(id);
    }

    //create a path for user id
    @RequestMapping(path = "/account/u{id}", method = RequestMethod.GET)
    public Account userAccount(@PathVariable int id){
        return accountDao.getAccountByUserId(id);
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public Map<Integer, String> listAccountByUserName(@RequestParam (required = false) String username){
        return accountDao.getListOfUserAndId(username);
    }
}
