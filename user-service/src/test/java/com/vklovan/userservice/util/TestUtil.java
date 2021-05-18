package com.vklovan.userservice.util;

import com.vklovan.userservice.dto.TransactionRequestDto;
import com.vklovan.userservice.dto.UserDto;
import com.vklovan.userservice.entity.User;
import com.vklovan.userservice.entity.UserTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtil {
    public static User user(String name, double balance) {
        User user = new User();
        user.setName(name);
        user.setBalance(new BigDecimal(balance));
        return user;
    }

    public static UserDto userDto(String name, double balance) {
        UserDto user = new UserDto();
        user.setName(name);
        user.setBalance(new BigDecimal(balance));
        return user;
    }

    public static TransactionRequestDto transactionRequest(double amount) {
        TransactionRequestDto request = new TransactionRequestDto();
        request.setAmount(new BigDecimal(amount));
        return request;
    }

    public static UserTransaction transaction(int userId, double amount) {
        UserTransaction transaction = new UserTransaction();
        transaction.setUserId(userId);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }
}
