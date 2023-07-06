package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDto;

import java.math.BigDecimal;

public class JdbcTransferDtoDao implements TransferDtoDao {

    @Override
    public TransferDto sendTransfer(int accountToId, BigDecimal amount) {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(01);
        transferDto.setTransferTypeId(2);
        transferDto.setTransferStatusId(1);
        transferDto.setAccountTo(accountToId);
        transferDto.setAmount(amount);

        //add exceptions to not send zero or negative amounts
        //add exception to not be able to send money to myself (make sure the accountFrom ID does not match accountTo ID)

        return null;
    }
}
