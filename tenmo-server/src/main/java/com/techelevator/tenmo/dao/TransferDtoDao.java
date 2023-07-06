package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDto;

import java.math.BigDecimal;

public interface TransferDtoDao {
    TransferDto sendTransfer(int accountToId, BigDecimal amount);


}
