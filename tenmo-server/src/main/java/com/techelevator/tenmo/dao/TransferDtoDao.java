package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


public interface TransferDtoDao {
    TransferDto transfer(TransferDto transferDto);

    List<TransferDto> getTransfers(int userID, boolean isPending, boolean isActionable);

    TransferDto getTransferByID(int tId);

    TransferDto updateTransfer(TransferDto transferDto);

}
