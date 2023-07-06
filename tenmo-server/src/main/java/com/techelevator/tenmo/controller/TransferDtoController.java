package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDtoDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferDtoController {
    private TransferDtoDao transferDtoDao;

    public TransferDtoController(TransferDtoDao transferDtoDao){
        this.transferDtoDao = transferDtoDao;
    }


}
