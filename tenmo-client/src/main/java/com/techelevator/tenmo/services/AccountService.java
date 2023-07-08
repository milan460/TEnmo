package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private User user = null;
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Account getUserAccount(AuthenticatedUser authenticatedUser){
        Account currentUserAccount = null;
        try{
            ResponseEntity<Account> response =  restTemplate.exchange(baseUrl + "account", HttpMethod.GET, makeAuthToken(authenticatedUser), Account.class);
            currentUserAccount = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return currentUserAccount;
    }

    private HttpEntity<Void> makeAuthToken(AuthenticatedUser authenticatedUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }

}
