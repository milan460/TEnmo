package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDtoDao;
import com.techelevator.tenmo.dao.UserDao;


import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@RestController
public class TransferDtoController {
    private TransferDtoDao transferDtoDao;
    private final UserDao userDao;
    private final AccountDao accountDao;

    public TransferDtoController(TransferDtoDao transferDtoDao, UserDao userDao, AccountDao accountDao){
        this.transferDtoDao = transferDtoDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping (path = "/Send", method = RequestMethod.POST)
    public void makeTransfer(@RequestBody int accountToId, @RequestBody BigDecimal amount){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        User user = userDao.getUserByUsername(authentication.getName());

        if(user.getId()==accountToId){
            throw new IllegalArgumentException("Cannot send money to yourself");
        }
        BigDecimal zero = BigDecimal.valueOf(0);
        if (!(amount.compareTo(zero) ==1)) {
            throw new IllegalArgumentException("Amount sent must be a non zero positive value");
        }
        BigDecimal balance = accountDao.getAccountById(user.getId()).getBalance();
        if ((amount.compareTo(balance)==1)){
            throw new IllegalArgumentException("Cannot transfer more money than your current balance");
        }

        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(user.getId()); // authentication to pull the account ID
        transferDto.setTransferTypeId(2);
        transferDto.setTransferStatusId(2);
        transferDto.setAccountTo(accountToId);
        transferDto.setAmount(amount);

        transferDtoDao.sendTransfer(transferDto);
    }

    // path /transfers

    // path /transfers/{id}
}
