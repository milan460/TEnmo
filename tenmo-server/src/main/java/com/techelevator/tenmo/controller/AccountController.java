package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
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

    @RequestMapping(path = "/account/me", method = RequestMethod.GET)
    public Account myAccount(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());
        return accountDao.getAccountByUserId(user.getId());
    }
}
