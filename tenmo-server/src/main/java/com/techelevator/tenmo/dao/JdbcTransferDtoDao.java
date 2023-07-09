package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PreAuthorize("isAuthenticated()")
public class JdbcTransferDtoDao implements TransferDtoDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDtoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferDto transfer(TransferDto transferDto) {
        TransferDto transfer = transferDto;
        boolean success = false;
        // post the transfer to the transfer table
        String transferSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
             transfer.setId( jdbcTemplate.queryForObject(transferSql, int.class,transferDto.getTransferTypeId(),transferDto.getTransferStatusId(),transferDto.getAccountFrom(),transferDto.getAccountTo(),transferDto.getAmount()));
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        } catch (NumberFormatException e){
            throw new DaoException("Number is not in correct format", e);
        }
        if(transfer.getTransferStatusId() == 2) {
            success = moveMoney(transferDto);
        }
        return transfer;
    }

    @Override
    public List<TransferDto> getTransfers(int userID, boolean isPending, boolean isActionable) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE account_from = ? OR account_to = ?;";
        if(isPending){
            sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE (account_from = ? or account_to = ?) AND transfer_status_id = 1;";
            if(isActionable){
               sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE account_from = ? AND transfer_status_id = 1;";
            }
        }
        SqlRowSet results;
        List<TransferDto> listy=new ArrayList<>();
        try{
            if(isPending && isActionable) {

                results = jdbcTemplate.queryForRowSet(sql, userID);
            }
            else {
                results = jdbcTemplate.queryForRowSet(sql, userID, userID);
            }
            while(results.next()){
                TransferDto item = mapToTransferDto(results);
                listy.add(item);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (NumberFormatException e) {
            throw new DaoException("Number is not in correct format", e);
        }
        catch (NullPointerException e) {
            throw new DaoException("No results currently available", e);
        }

        return listy;
    }

    @Override
    public TransferDto getTransferByID(int tId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results;
        TransferDto item = null;
        try{
            results = jdbcTemplate.queryForRowSet(sql,tId);
            if(results.next()) {
                item = mapToTransferDto(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (NumberFormatException e) {
            throw new DaoException("Number is not in correct format", e);
        }
        catch (NullPointerException e) {
            throw new DaoException("No results currently available", e);
        }

        return item;
    }


    private boolean moveMoney(TransferDto transferDto){

        String decreaseSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(decreaseSql, transferDto.getAmount(),transferDto.getAccountFrom());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        }
        catch (NumberFormatException e){
            throw new DaoException("Number is not in correct format", e);
        }


        // increase the recipients Mulah
        String increaseSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(increaseSql, transferDto.getAmount(),transferDto.getAccountTo());
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to Server Database",e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation",e);
        }
        catch (NumberFormatException e){
            throw new DaoException("Number is not in correct format", e);
        }

        return true;
    }

    private TransferDto mapToTransferDto (SqlRowSet results){
        TransferDto item = new TransferDto();

        item.setId(results.getInt("transfer_id"));
        item.setTransferTypeId(results.getInt("transfer_type_id"));
        item.setTransferStatusId(results.getInt("transfer_status_id"));
        item.setAccountFrom(results.getInt("account_from"));
        item.setAccountTo(results.getInt("account_to"));
        item.setAmount(results.getBigDecimal("amount"));

        return item;
    }


}
