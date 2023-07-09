package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Account getUserAccount(AuthenticatedUser authenticatedUser){
        Account currentUserAccount = null;
        try{
            ResponseEntity<Account> response =  restTemplate.exchange(baseUrl + "account/me", HttpMethod.GET, makeAuthToken(authenticatedUser), Account.class);
            currentUserAccount = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return currentUserAccount;
    }

    public Map<String, Integer> getListOfAccounts(AuthenticatedUser authenticatedUser){
        Map<String, Integer> accountMap = null;
        try{
            ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "account", HttpMethod.GET, makeAuthToken(authenticatedUser), Map.class);
            accountMap = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return accountMap;
    }

    public int sendMoney(AuthenticatedUser authenticatedUser, int accountToId, BigDecimal amount){
        int transferId = 0;
        try{
           ResponseEntity<Integer> response = restTemplate.exchange(baseUrl + "send?accountToId=" + accountToId + "&amount=" + amount, HttpMethod.POST, makeAuthToken(authenticatedUser), int.class);
           transferId = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferId;
    }

    public Transfer[] getMyTransfers(AuthenticatedUser authenticatedUser){
        Transfer[] myTransferArray = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers", HttpMethod.GET, makeAuthToken(authenticatedUser), Transfer[].class);
            myTransferArray = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return myTransferArray;
    }

    private HttpEntity<Void> makeAuthToken(AuthenticatedUser authenticatedUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
    public int requestMoney(AuthenticatedUser authenticatedUser, int accountToId, BigDecimal amount){
        int transferId = 0;
        try{
            ResponseEntity<Integer> response = restTemplate.exchange(baseUrl + "request?accountToId=" + accountToId + "&amount=" + amount, HttpMethod.POST, makeAuthToken(authenticatedUser), int.class);
            transferId = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferId;
    }

    public Transfer[] getPendingTransfers(AuthenticatedUser authenticatedUser){
        Transfer[] myTransferArray = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/pending", HttpMethod.GET, makeAuthToken(authenticatedUser), Transfer[].class);
            myTransferArray = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return myTransferArray;
    }

    public Transfer[] getActionableTransfers(AuthenticatedUser authenticatedUser){
        Transfer[] myTransferArray = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/actionable", HttpMethod.GET, makeAuthToken(authenticatedUser), Transfer[].class);
            myTransferArray = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return myTransferArray;
    }

    public Transfer takeActionOnActionableRequest(AuthenticatedUser authenticatedUser,int transactionId, String approvalStatus){
        Transfer transfer=null;
        try{
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfers/"+transactionId+"/"+approvalStatus, HttpMethod.PUT, makeAuthToken(authenticatedUser), Transfer.class);
            transfer = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

}
