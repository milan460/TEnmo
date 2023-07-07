package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDtoDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDtoDaoTests extends BaseDaoTests {
    private static final BigDecimal BALANCE_1 = new BigDecimal(1000.00);
    private static final BigDecimal BALANCE_2 = new BigDecimal(500.00);
    private static final BigDecimal BALANCE_3 = new BigDecimal(0.00);
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BALANCE_1);
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, BALANCE_2);
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, BALANCE_3);

    protected static final TransferDto TRANSFER_1 = new TransferDto(3001, 2, 2, 2001, 2002, new BigDecimal(100.50));
    protected static final TransferDto TRANSFER_2 = new TransferDto(3002, 2, 2, 2003, 2001, new BigDecimal(1000.00));
    protected static final TransferDto TRANSFER_3 = new TransferDto(3003, 2, 2, 2002, 2001, new BigDecimal(99.99));
    protected static final TransferDto TRANSFER_4 = new TransferDto(3004, 2, 2, 2001, 2002, new BigDecimal(50.00));
    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private JdbcTransferDtoDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDtoDao(jdbcTemplate);
    }

    @Test
    public void sendTransfer_given_valid_id_and_amount_returns_correct_transfer_amount(){
        TransferDto actual = sut.sendTransfer(TRANSFER_1);

        Assert.assertTrue(actual.equals(TRANSFER_1));
    }

    @Test(expected = NumberFormatException.class)
    public void sendTransfer_given_null_id_and_null_amount(){
        int idNull = Integer.parseInt(null);
        BigDecimal bigDecimalNull = null;
        TransferDto expected = new TransferDto(3050, 2, 2, 2001, idNull, bigDecimalNull);
        TransferDto actual = sut.sendTransfer(expected);

        Assert.assertNull(actual);

    }

    @Test(expected = DaoException.class)
    public void sendTransfer_given_valid_id_and_null_amount(){
        BigDecimal bigDecimalNull = null;
        TransferDto expected = new TransferDto(3050, 2, 2, 2001, 2002, bigDecimalNull);
        TransferDto actual = sut.sendTransfer(expected);

        Assert.assertNull(actual);

    }


}
