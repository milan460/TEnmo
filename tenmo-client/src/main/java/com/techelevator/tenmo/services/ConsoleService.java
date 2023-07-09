package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: View requests requiring approval");
        System.out.println("5: Send TE bucks");
        System.out.println("6: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ").toLowerCase();
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printUserAccountBalance(BigDecimal balance){
        System.out.print("Here is your current balance: ");
        System.out.println(balance);
    }

    public boolean takeAction() {
        boolean action = false;
        for (int i=0;true;i++){
            String response = promptForString("Do you wish to take action on any requests? (y/n): ").toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                action = true;
            }
            if (!List.of("y", "yes", "n", "no").contains(response)) {
                System.out.println("invalid response");
                if (i<3) {
                    continue;
                }
                System.out.println("Cancelling action");
            }
            break;
        }
        return action;
    }

    public boolean approval() {
        String approvalString = promptForString("to confirm, please type 'Approve'").toLowerCase();
        if (approvalString.equals("approve")){
            System.out.println("Approved");
            return true;
        } else {
            System.out.println("not approved");
            return false;
        }
    }

    public void printApproveMenu() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Exit");
        System.out.println();
    }


}
