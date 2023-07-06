package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private static final BigDecimal BALANCE_1 = new BigDecimal(1000.00);
    private static final BigDecimal BALANCE_2 = new BigDecimal(500.00);
    private static final BigDecimal BALANCE_3 = new BigDecimal(0.00);
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BALANCE_1);
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, BALANCE_2);
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, BALANCE_3);

    private JdbcAccountDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    //test null Id
    //test a invalid Id
    @Test(expected = NullPointerException.class)
    public void getAccountById_given_invalid_Id(){
        sut.getAccountById(-1);
    }

    @Test
    public void getAccountById_valid_user_id_returns_account(){
        Account account = sut.getAccountById(ACCOUNT_1.getId());
        Assert.assertEquals("getAccountById does not return the correct Account.",ACCOUNT_1, account);
    }
}
