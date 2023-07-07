package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private static final BigDecimal BALANCE_1 = new BigDecimal(1000.00);
    private static final BigDecimal BALANCE_2 = new BigDecimal(500.00);
    private static final BigDecimal BALANCE_3 = new BigDecimal(0.00);
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BALANCE_1);
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, BALANCE_2);
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, BALANCE_3);

    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private JdbcAccountDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }


    @Test(expected = NullPointerException.class)
    public void getAccountById_given_invalid_Id(){
        Account account = sut.getAccountById(-1);
        Assert.assertNull(account);
    }

    @Test
    public void getAccountById_valid_user_id_returns_account(){
        Account account = sut.getAccountById(ACCOUNT_1.getId());
        Assert.assertTrue("getAccountById does not return the correct Account.",account.equals(ACCOUNT_1));
    }

    @Test(expected = NumberFormatException.class)
    public void getAccountById_given_null_Id(){
        int value = Integer.parseInt(null);
        Account account = sut.getAccountById(value);
        Assert.assertNull(account);
    }

    @Test(expected = NullPointerException.class)
    public void getAccountByUserId_given_invalid_userId(){
        Account account = sut.getAccountByUserId(-1);
        Assert.assertNull(account);
    }

    @Test
    public void getAccountByUserId_given_valid_userId_returns_account(){
        Account account = sut.getAccountByUserId(ACCOUNT_2.getUserId());
        Assert.assertTrue("getAccountByUserId does not return the correct Account", account.equals(ACCOUNT_2));
    }

    @Test(expected = NumberFormatException.class)
    public void getAccountByUserId_given_null_userId(){
        int value = Integer.parseInt(null);
        Account account = sut.getAccountByUserId(value);
        Assert.assertNull(account);
    }

    @Test
    public void getListOfUserAndId_given_null_username_returns_all_users(){

        Map<Integer, String> actual = sut.getListOfUserAndId(null);
        Map<Integer, String> expected = new HashMap<>();
        expected.put(ACCOUNT_1.getId(), USER_1.getUsername());
        expected.put(ACCOUNT_2.getId(), USER_2.getUsername());
        expected.put(ACCOUNT_3.getId(), USER_3.getUsername());

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getListOfUserAndId_given_valid_username(){
        Map<Integer, String> actual = sut.getListOfUserAndId("user3");
        Map<Integer, String> expected = new HashMap<>();
        expected.put(ACCOUNT_3.getId(), USER_3.getUsername());

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getListOfUserAndId_given_invalid_username(){
        Map<Integer, String> actual = sut.getListOfUserAndId("user");
        Map<Integer, String> expected = new HashMap<>();

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getListOfUserAndId_given_empty_string_username(){
        Map<Integer, String> actual = sut.getListOfUserAndId("");
        Map<Integer, String> expected = new HashMap<>();

        Assert.assertEquals(actual, expected);
    }
}
