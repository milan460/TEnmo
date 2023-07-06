package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public interface TransferDtoDao {
    TransferDto sendTransfer(TransferDto transferDto);

}
