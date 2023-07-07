package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


//    @Override
//    public BigDecimal transfer(BigDecimal transferAmount) {
//        return null;
//    }

    @Override
    public Account getAccountById(int id) {
        String sql= "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        Account account;
        SqlRowSet sqlRowSet;
        try{
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (sqlRowSet.next()) {
                account = mapToAccount(sqlRowSet);
            } else { throw new NullPointerException("No Account found with this ID"); };
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (NumberFormatException e) {
            throw new DaoException("Number is not in correct format", e);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql= "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        Account account;
        SqlRowSet sqlRowSet;
        try{
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (sqlRowSet.next()) {
                account = mapToAccount(sqlRowSet);
            } else { throw new NullPointerException("No Account found with this ID"); };
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (NumberFormatException e){
            throw new DaoException("Number is not in correct format", e);
        }
        return account;
    }

    @Override
    public Map<Integer, String> getListOfUserAndId(String username) {
        Map<Integer,String> userMap = new HashMap<>();
        String wildcardName = "%";
        if(username != null){
            wildcardName = username;
        }
        String sql = "SELECT account_id, username \n" +
                "FROM account\n" +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
                "WHERE username ILIKE ?";
        try{
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, wildcardName);
            while(sqlRowSet.next()){
                userMap.put(sqlRowSet.getInt("account_id"), sqlRowSet.getString("username"));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userMap;
    }


    private Account mapToAccount(SqlRowSet sqlRowSet){
        Account account = new Account();
        account.setId(sqlRowSet.getInt("account_id"));
        account.setUserId(sqlRowSet.getInt("user_id"));
        account.setBalance(sqlRowSet.getBigDecimal("balance"));
        return account;
    }
}
