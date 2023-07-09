package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDtoDao;
import com.techelevator.tenmo.dao.UserDao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferDtoController {
    private TransferDtoDao transferDtoDao;
    private final UserDao userDao;
    private final AccountDao accountDao;

    public TransferDtoController(TransferDtoDao transferDtoDao, UserDao userDao, AccountDao accountDao) {
        this.transferDtoDao = transferDtoDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public int makeTransfer(@RequestParam int accountToId, @RequestParam BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());
        TransferDto transferDto = null;

        Account userAccount = accountDao.getAccountByUserId(user.getId());
        if (userAccount.getId() == accountToId) {
            throw new IllegalArgumentException("Cannot send money to yourself");
        }
        BigDecimal zero = BigDecimal.valueOf(0);
        if (!(amount.compareTo(zero) == 1)) {
            throw new IllegalArgumentException("Amount sent must be a non zero positive value");
        }
        BigDecimal balance = userAccount.getBalance();
        if ((amount.compareTo(balance) == 1)) {
            throw new IllegalArgumentException("Cannot transfer more money than your current balance");
        } else {

            transferDto = new TransferDto();
            transferDto.setAccountFrom(userAccount.getId()); // authentication to pull the account ID
            transferDto.setTransferTypeId(2);
            transferDto.setTransferStatusId(2);
            transferDto.setAccountTo(accountToId);
            transferDto.setAmount(amount);

            transferDtoDao.transfer(transferDto);
        }

        return transferDto.getId();
    }


    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<TransferDto> myTransfers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());


        Account userAccount = accountDao.getAccountByUserId(user.getId());
        return transferDtoDao.getTransfers(userAccount.getId(), false, false);
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public TransferDto getTransfer(@PathVariable int id) {
        return transferDtoDao.getTransferByID(id);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public int requestTransfer(@RequestParam int accountToId, @RequestParam BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());
        TransferDto transferDto = null;
        Account userAccount = accountDao.getAccountByUserId(user.getId());

        if (userAccount.getId() == accountToId) {
            throw new IllegalArgumentException("Cannot request money from yourself");
        }
        BigDecimal zero = BigDecimal.valueOf(0);
        if (!(amount.compareTo(zero) == 1)) {
            throw new IllegalArgumentException("Amount sent must be a non zero positive value");
        } else {

            transferDto = new TransferDto();
            transferDto.setAccountFrom(accountToId); // authentication to pull the account ID
            transferDto.setTransferTypeId(1);
            transferDto.setTransferStatusId(1);
            transferDto.setAccountTo(userAccount.getId());
            transferDto.setAmount(amount);

            transferDtoDao.transfer(transferDto);
        }


        return transferDto.getId();
    }

    @RequestMapping(path = "/transfers/pending", method = RequestMethod.GET)
    public List<TransferDto> myPendingTransfers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());


        Account userAccount = accountDao.getAccountByUserId(user.getId());
        return transferDtoDao.getTransfers(userAccount.getId(), true, false);
    }

    @RequestMapping(path = "/transfers/actionable", method = RequestMethod.GET)
    public List<TransferDto> myActionableTransfers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());


        Account userAccount = accountDao.getAccountByUserId(user.getId());
        return transferDtoDao.getTransfers(userAccount.getId(), true, true);
    }

    @RequestMapping (path = "/transfers/{id}/{approval}", method = RequestMethod.PUT)
    public TransferDto approveRequest (@PathVariable int id, @PathVariable String approval){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDao.getUserByUsername(authentication.getName());

        TransferDto transferDto = transferDtoDao.getTransferByID(id);
        int statusId = 0;

        Account userAccount = accountDao.getAccountByUserId(user.getId());
        BigDecimal balance = userAccount.getBalance();
        BigDecimal amount = transferDto.getAmount();
        if ((amount.compareTo(balance) == 1)) {
            transferDto=null;
            throw new IllegalArgumentException("Cannot transfer more money than your current balance");

        }else {
        if (approval.equals("approve")){
            statusId=2;
        }else if (approval.equals("reject")){
            statusId=3;
        }
        if (statusId == 2 || statusId ==3){
            transferDto.setTransferStatusId(statusId);
            transferDto = transferDtoDao.updateTransfer(transferDto);
        }
        }
        return transferDto;
    }
}
