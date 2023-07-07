package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferDto {
    private int id;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal Amount;
    private String transferType;

    public TransferDto(){

    }

    public TransferDto(int id, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount){
        this.id = id;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.Amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    @Override
    public String toString() {
        return "TransferDto{" +
                "id=" + id +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", Amount=" + Amount +
                ", transferType='" + transferType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferDto)) return false;
        TransferDto that = (TransferDto) o;
        boolean amountEquals = (Amount.compareTo(((TransferDto) o).getAmount()) == 0);
        return id == that.id && transferTypeId == that.transferTypeId && transferStatusId == that.transferStatusId && accountFrom == that.accountFrom && accountTo == that.accountTo && Amount.equals(that.Amount) && transferType.equals(that.transferType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transferTypeId, transferStatusId, accountFrom, accountTo, Amount, transferType);
    }

    //    private void setTransferType(String transferType) {
//        this.transferType = transferType;
//    }

    //create user info
    //get user info
    //update user info
    //get account info
    //update account info
}
