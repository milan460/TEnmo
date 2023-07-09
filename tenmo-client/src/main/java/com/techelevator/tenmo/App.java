package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    public static final int EXIT_APP = 0;
    public static final int VIEW_CURRENT_BALANCE = 1;
    public static final int VIEW_PAST_TRANSFERS = 2;
    public static final int VIEW_PENDING_REQUESTS = 3;
    public static final int VIEW_ACTIONABLE_REQUESTS = 4;
    public static final int SEND_TE_BUCKS = 5;
    public static final int REQUEST_TE_BUCKS = 6;
    public static final int REGISTER_USER = 1;
    public static final int LOGIN_USER = 2;
    public static final int SELECT_APPROVAL = 1;
    public static final int SELECT_REJECTION = 2;

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private final AccountService accountService = new AccountService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            // TODO: Instantiate services that require the current user to exist here
            viewCurrentBalance();
            countActionableRequests();
            mainMenu();

        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != EXIT_APP && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == REGISTER_USER) {
                handleRegister();
            } else if (menuSelection == LOGIN_USER) {
                handleLogin();
            } else if (menuSelection != EXIT_APP) {
                consoleService.printMessage("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        consoleService.printMessage("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printMessage("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != EXIT_APP) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == VIEW_CURRENT_BALANCE) {
                viewCurrentBalance();
            } else if (menuSelection == VIEW_PAST_TRANSFERS) {
                viewTransferHistory();
            } else if (menuSelection == VIEW_PENDING_REQUESTS) {
                viewPendingRequests();
            } else if (menuSelection == VIEW_ACTIONABLE_REQUESTS) {
                viewActionableRequests();
            } else if (menuSelection == SEND_TE_BUCKS) {
                sendBucks();
            } else if (menuSelection == REQUEST_TE_BUCKS) {
                requestBucks();
            } else if (menuSelection == EXIT_APP) {
                continue;
            } else {
                consoleService.printMessage("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        Account userAccount = accountService.getUserAccount(currentUser);
        consoleService.printUserAccountBalance(userAccount.getBalance());
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        Transfer[] myTransferArray = accountService.getMyTransfers(currentUser);
        for( Transfer myTransfer : myTransferArray){
            consoleService.printMessage("TransferId: " + myTransfer.getId() + " |  Transfer type: " + myTransfer.getTransferType() + " |  Transfer Status: " + myTransfer.getStatus() + " |  from AccountId: " + myTransfer.getAccountFrom() + " |  to AccountId: " + myTransfer.getAccountTo() + " |  Amount: " + myTransfer.getAmount());
        }

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        Transfer[] myTransferArray = accountService.getPendingTransfers(currentUser);
        for( Transfer myTransfer : myTransferArray){
            consoleService.printMessage("TransferId: " + myTransfer.getId() + " |  Transfer type: " + myTransfer.getTransferType() + " |  Transfer Status: " + myTransfer.getStatus() + " |  from AccountId: " + myTransfer.getAccountFrom() + " |  to AccountId: " + myTransfer.getAccountTo() + " |  Amount: " + myTransfer.getAmount());
        }
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        int transferId = 0;
        Map<String, Integer> mapOfAccounts = accountService.getListOfAccounts(currentUser);
        for(Map.Entry<String, Integer> entry: mapOfAccounts.entrySet()){
            consoleService.printMessage("UserName: " + entry.getValue() + ", AccountId: " + entry.getKey());
        }
        int accountToId = consoleService.promptForInt("Enter AccountId you want to send TEbucks to: " );
        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you want to send: ");

        transferId = accountService.sendMoney(currentUser, accountToId, amount);

        if(transferId == 0){
            consoleService.printErrorMessage();
        }
        consoleService.printMessage("Transfer Completed! TransferId: " + transferId);
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        int transferId = 0;
        Map<String, Integer> mapOfAccounts = accountService.getListOfAccounts(currentUser);
        for(Map.Entry<String, Integer> entry: mapOfAccounts.entrySet()){
            consoleService.printMessage("UserName: " + entry.getValue() + ", AccountId: " + entry.getKey());
        }
        int accountToId = consoleService.promptForInt("Enter AccountId you want to Request TEbucks from: " );
        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you want to request: ");

        transferId = accountService.requestMoney(currentUser, accountToId, amount);

        if(transferId == 0){
            consoleService.printErrorMessage();
        }
        consoleService.printMessage("Request Made! Now awaiting approval! TransferId: " + transferId);
    }

    private void viewActionableRequests() {
        // TODO Not an Auto-generated method stub
        Transfer[] myTransferArray = accountService.getActionableTransfers(currentUser);
        if (myTransferArray.length==0){
            consoleService.printMessage("You have no actionable requests at this time.");
        } else {
            for (Transfer myTransfer : myTransferArray) {
                consoleService.printMessage("TransferId: " + myTransfer.getId() + " |  Transfer type: " + myTransfer.getTransferType() + " |  Transfer Status: " + myTransfer.getStatus() + " |  from AccountId: " + myTransfer.getAccountFrom() + " |  to AccountId: " + myTransfer.getAccountTo() + " |  Amount: " + myTransfer.getAmount());
            }
            if(consoleService.takeAction()){
                takeAction();
            }
        }
    }
    private void countActionableRequests() {
        Transfer[] myTransferArray = accountService.getActionableTransfers(currentUser);
        int count= myTransferArray.length;
        if (count !=0) {
            System.out.println("You have " + count + " requests to take action on");

        }
    }

    private void takeAction(){
        int transferId =consoleService.promptForInt("Which transaction ID do you want to take action on?  ");

        int menuSelection = -1;

        while (menuSelection== -1) {
            consoleService.printApproveMenu();
            menuSelection= consoleService.promptForMenuSelection("Select how you would like to proceed:  ");
        }

        if (menuSelection == 1){
            boolean isApproved = consoleService.approval();
            if (isApproved){
                // sent update request with approval status
                accountService.takeActionOnActionableRequest(currentUser, transferId,"approve");
            }
        } else if (menuSelection==2) {
            consoleService.printMessage("Rejecting request");
            accountService.takeActionOnActionableRequest(currentUser,transferId,"reject");
            // send update request with denial status
        } else if (menuSelection==0) {
            consoleService.printMessage("proceeding with exit");
        }else if (!List.of(1,2,0).contains(menuSelection)){
            consoleService.printMessage("invalid selection.  Proceeding with exit");
        }
    }

}
