package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDtoDao implements TransferDtoDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDtoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferDto sendTransfer(TransferDto transferDto) {
        TransferDto transfer = transferDto;

        // post the transfer to the transfer table
        String transferSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
             transfer.setId( jdbcTemplate.queryForObject(transferSql, int.class,transferDto.getTransferTypeId(),transferDto.getTransferStatusId(),transferDto.getAccountFrom(),transferDto.getAccountTo(),transferDto.getAmount()));
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        }

        // decrease the sender's deniro
        String decreaseSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(decreaseSql, transferDto.getAmount(),transferDto.getAccountFrom());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        }

        // increase the recipients Mulah
        String increaseSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(decreaseSql, transferDto.getAmount(),transferDto.getAccountTo());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        }

        return transfer;
    }


}
