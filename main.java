package com.project;

import java.io.*;
import java.util.Scanner;
public class Main {
	
	private static void DEBUG(String msg) {
		System.out.println("(DEBUG) " + msg);
	}
	
    // extract the username from account line and return it
    private static String parseUserName(String account) {
        int colonIndex = account.indexOf(':');
        return account.substring(0, colonIndex);
    }

    // extract the password from account line and return it
    private static String parsePassword(String account) {
        int colonIndex = account.indexOf(':');
        int hashIndex = account.indexOf('#');
        return account.substring(colonIndex + 1, hashIndex);
    }

    // extract the money from account line and return it
    private static int parseMoney(String account) {
        int hashIndex = account.indexOf('#');
        String money = account.substring(hashIndex + 1);
        return Integer.parseInt(money);
    }
    
    private static int getMainMenuOption() { // Function takes an integer in range 1-6 and returns it
    	Scanner input = new Scanner(System.in);
    	int option;
    	do { // Input validation  for main menu
    		System.out.print("Enter option: ");
    		option = input.nextInt();
    		if (option <= 0 || option >= 7) {
    			System.out.println("Invalid option entered");
    		}
    	} while (option <= 0 || option >= 7);
    	return option;
    }
    
    private static void mainMenu() {
    	System.out.println("~~~~~~~~~ Welcome to RKMS ATM ~~~~~~~~~~~");
    	System.out.println("1. Create Account");
    	System.out.println("2. Delete Account");
    	System.out.println("3. Deposit Money");
    	System.out.println("4. Withdraw Money");
    	System.out.println("5. Edit Profile");
    	System.out.println("6. Exit");
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private static void createAccount(String fileName) throws IOException {
    	PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    	Scanner input = new Scanner(System.in);
    	String userName = input.nextLine();
    	String userPassword = input.nextLine();
    	int money = input.nextInt();
    	file.println(userName + ":" + userPassword + "#" + String.valueOf(money));
    	input.close();
    	file.close();
    }
    
    public static void main(String[] args) throws IOException {

        try {
            File fileAccounts = new File("accounts.txt");
            Scanner accounts = new Scanner(fileAccounts);
            
            // Main Processing Here 
            mainMenu();
            int mainMenuOption = getMainMenuOption();
            
            switch (mainMenuOption) {
            case 1:
            	createAccount("accounts.txt");
            	break;
            //case 2:
            	//deleteAccount();
            	//break;
            //case 3:
            	//depositMoney();
            	//break;
            //case 4:
            	//withdrawMoney();
            	//break;
            //case 5:
            	//editProfile();
            	//break;
            //case 6:
            	//exit();
            	//break;
            default: {
            		DEBUG("This should never be reached");
            	}
            
            }
            

            
            
            
            DEBUG("PROGRAM FINISHED EXECUTING");
            
            // Close accounts after doing all the processing with it
            accounts.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: Try checking the file for existence or permissions");
            e.printStackTrace();
        }
    }
}
