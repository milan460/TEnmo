package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal transfer(BigDecimal transferAmount) {
        return null;
    }

    @Override
    public Account getAccountById(int id) {
        String sql= "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        Account account;
        SqlRowSet sqlRowSet;
        try{
            sqlRowSet = jdbcTemplate.queryForObject(sql,SqlRowSet.class, id);
            if (sqlRowSet.next()) {
                account = mapToAccount(sqlRowSet);
            } else { throw new NullPointerException("No Account found with this ID"); };
        } catch (DataAccessException e){
            throw new DaoException(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql= "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        Account account;
        SqlRowSet sqlRowSet;
        try{
            sqlRowSet = jdbcTemplate.queryForObject(sql,SqlRowSet.class, userId);
            if (sqlRowSet.next()) {
                account = mapToAccount(sqlRowSet);
            } else { throw new NullPointerException("No Account found with this ID"); };
        } catch (DataAccessException e){
            throw new DaoException(e.getMessage());
        }
        return account;
    }

    private Account mapToAccount(SqlRowSet sqlRowSet){
        Account account = new Account();
        account.setId(sqlRowSet.getInt("account_id"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        return account;
    }
}
