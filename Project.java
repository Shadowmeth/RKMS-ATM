import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Project {
	private static String fileName = "accounts.txt";
	private	static String[] accounts;

	// extract the username from account line and return it
	// PARSEUSERNAME
    private static String parseUserName(String account) {
    	// saba:thedumbestdumbo#21516
        int colonIndex = account.indexOf(':');
        return account.substring(0, colonIndex);
    }

    // extract the password from account line and return it
    // PARSEPASSWORD
    private static String parsePassword(String account) {
        int colonIndex = account.indexOf(':');
        int hashIndex = account.indexOf('#');
        return account.substring(colonIndex + 1, hashIndex);
    }

    // extract the money from account line and return it
    // PARSEMONEY
    private static int parseMoney(String account) {
        int hashIndex = account.indexOf('#');
        String money = account.substring(hashIndex + 1);
        return Integer.parseInt(money);
    }

    // get number of accounts
    // GETACCOUNTCOUNT
	private static int getAccountCount() throws FileNotFoundException { 
		File file = new File(fileName);
		Scanner acc = new Scanner(file);
		int count = 0;
		while (acc.hasNextLine()) {
			acc.nextLine();
			count++;
		}
		acc.close();
		return count;
	}

	// DELETEARRAYELEMENT
	private static String[] deleteArrayElement(String[] arr, String ele) {
		String[] temp = new String[arr.length - 1];
		for (int j = 0, i = 0; i < temp.length;) {
			if (!arr[j].equals(ele)) {
				temp[i++] = arr[j++];
			} else {
				j++;
			}
		}
		return temp;
	}

	// REMOVEACCOUNT
	private static void removeAccount(String account) throws IOException {
		accounts = deleteArrayElement(accounts, account);
		writeFile(accounts);
	}

	// APPENDACCOUNT
	private static void appendAccount(String account) throws IOException {
		accounts = addArrayElement(accounts, account);
		writeFile(accounts);
	}

	// old contents are overwritten by contents of array accounts
	// WRITEFILE
	private static void writeFile(String[] accounts) throws IOException {
		PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		for (int i = 0; i < accounts.length; i++) {
			file.println(accounts[i]);
		}
		file.close();
	}

	// read contents of file in accounts
	// READFILE
	private static void readFile() throws FileNotFoundException {
		accounts = new String[getAccountCount()];
		File file = new File(fileName);
		Scanner acc = new Scanner(file);
		
		for(int i = 0; i < accounts.length; i++) {
			accounts[i] = acc.nextLine();
		}
		acc.close();
	}

	// GETUSERNAME
	private static String getUserName() {
		Scanner input = new Scanner(System.in);
		String userName;
		do {
			System.out.print("Enter username: ");
			userName = input.nextLine();
			if (userName.length() < 3) {
				System.out.println("Invalid Username entered.");
			}
		} while (userName.length() < 3);
		return userName;
	}

	// GETPASSWORD
	private static String getPassword() {
		Scanner input = new Scanner(System.in);
		String password;
		do {
			System.out.print("Enter password: ");
			password = input.nextLine();
			if (password.length() < 3 || password.indexOf('#') != -1) { // password can't contain # otherwise the file format
				System.out.println("Invalid Password entered."); // breaks
			}
		} while (password.length() < 3 || password.indexOf('#') != -1);
		return password;
	}

	// GETMONEY
	private static int getMoney() {
		Scanner input = new Scanner(System.in);
		int money = 0;
		
		do {
			System.out.print("Enter money: ");
			try {
				money = input.nextInt();
				input.nextLine(); // clear input buffer
			} catch (Exception e) {
				System.out.println("Type mismatch occured");
				input.nextLine(); // this will clear the input buffer
				continue;
			}
			if (money < 10000) {
				System.out.println("Minimum 10k initial deposit is required.");
			}
		} while (money < 10000);
		return money;
	}

	// returns true if the user exists in the file already
	// USEREXISTS
	private static boolean userExists(String userName) {
		for (int i = 0; i < accounts.length; i++) {
			if (parseUserName(accounts[i]).equals(userName)) {
				return true;
			}
		}
		return false;
	}

	// CONSTRUCTACCOUNT
	private static String constructAccount(String userName, String password, int money) {
		return userName + ":" + password + "#" + String.valueOf(money);
	}

	// ADDARRAYELEMENT
	private static String[] addArrayElement(String[] arr, String ele) {
		String[] temp = new String[arr.length + 1];
		int c = 0;
		for (; c < arr.length; c++) {
			temp[c] = arr[c];
		}
		temp[c] = ele;
		return temp;
	}

	// takes inputs, create an account, updates the file
	// CREATEACCOUNT
	private static boolean createAccount() throws IOException {
		String userName = getUserName();
		if (userExists(userName)) { // can't create duplicate user
			System.out.println("User already exists");
			return false;
		}

		String password = getPassword();
		int money = getMoney();
		String account = constructAccount(userName, password, money);
		appendAccount(account);
		System.out.println("Account created successfully");
		return true;
	}
	
	// MATCHUSERNAMEANDPASSWORD
	private static boolean matchUserNameAndPassword(String userName, String password) {
		for (int i = 0; i < accounts.length; i++) {
			if (parseUserName(accounts[i]).equals(userName)) {
				if (parsePassword(accounts[i]).equals(password)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// GETINITALMONEY
	private static int getInitialMoney(String userName) {
		for (int i = 0; i < accounts.length; i++) { // traverse the accounts array
			if (parseUserName(accounts[i]).equals(userName)) {
				return parseMoney(accounts[i]); // return the money for userName
			}
		}
		return -1;
	}

	// INPUTMONEY
	private static int inputMoney() {
		int money;
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("Enter amount to deposit: ");
			money = input.nextInt();
			input.nextLine(); // clear input buffer
			if (money < 0) {
				System.out.println("Invalid value entered");
			}
		} while(money < 0);
		return money;
	}

	// GETACCOUNTINDEX
	private static int getAccountIndex(String userName) {
		for (int i = 0; i < accounts.length; i++) {
				if (parseUserName(accounts[i]).equals(userName)) {
					return i;
				}
		}
		return -1; // THIS SHOULD NEVER BE EXECUTED: IF THE FUNCTION IS RETURNING -1 THEN THERE'S AN ERROR
	}
	
	// DEPOSITMONEY
	private static boolean depositMoney(boolean printBill) throws IOException {
		String userName = getUserName();
		if (!userExists(userName)) {
			System.out.println("Account username: \"" + userName + "\" doesn't exists");
			return false; // there is no such user, we can't deposit money
		}
		
		String password = getPassword();
		if (!matchUserNameAndPassword(userName, password)) {
			System.out.println("Incorrect username/password entered!");
			return false; // username and password weren't matched
		}

		int initialMoney = getInitialMoney(userName);
		// instead of deleting and creating elements in the accounts array
		// what if the changed the array element in-place
		int addMoney = inputMoney();
		accounts[getAccountIndex(userName)] = constructAccount(userName, password, initialMoney + addMoney);
		writeFile(accounts);

		if (printBill) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd             HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			System.out.println("------------------------------------------");
			System.out.println(dtf.format(now));
			System.out.println("\t\tRKMS ATM");
			System.out.println("Account User Name: " + userName);
			System.out.println("Initial Money\t\t" + "$" + initialMoney);
			System.out.println("Money deposited\t\t" + "$" + addMoney + "+");
			System.out.println("________________________________");
			System.out.println("Total Money\t\t" + "$" + (initialMoney + addMoney));
			System.out.println("-------------------------------------------");
		}
		
		return true;
	}
	
	// ENTERMONEYTOWITHDRAW
	private static int enterMoneyToWithdraw(int initialMoney) {
		Scanner input = new Scanner(System.in);
		int money = 0;
		do {
			System.out.println("Enter amount you want to withdraw: ");
			money = input.nextInt();
			input.nextLine(); // clear input buffer
			if(money > initialMoney) {
				System.out.println("The amount you have entered is greater than the amount in your account");
			} else if (money < 0) {
				System.out.println("You entered negative amount");
			}
		}while(money > initialMoney || money < 0);
		return money;
	}
	
	// GETWITHDRAWAMOUNT
	private static int getWithdrawAmount(int initialMoney) {
		Scanner input = new Scanner(System.in);
		int option;

		do {
			System.out.println("1. 1000");
			System.out.println("2. 5000");
			System.out.println("3. 10000");
			System.out.println("4. 25000");
			System.out.println("5. 50000");
			System.out.println("6. Other");
			System.out.println("7. Cancel");
			System.out.print("Enter the option: ");
			option = input.nextInt();
			input.nextLine(); // clear input buffer
			switch (option) {
			case 1:
				return 1000;
			case 2:
				return 5000;
			case 3:
				return 10000;
			case 4:
				return 25000;
			case 5:
				return 50000;
			case 6:
				return enterMoneyToWithdraw(initialMoney);
			case 7:
				return -1;
			default:
				System.out.println("Invalid Option Entered");
				continue;
			}
				
		} while(option < 1 || option > 7);
		input.close();
		return -2; // this should never be returned
	}

	// WITHDRAWMONEY
	private static boolean withDrawMoney(boolean printBill) throws IOException {
		String userName = getUserName();
		if (!userExists(userName)) {
			System.out.println("Account username: \"" + userName + "\" doesn't exists");
			return false; // there is no such user, we can't withdraw money
		}
		
		String password = getPassword();
		if (!matchUserNameAndPassword(userName, password)) {
			System.out.println("Incorrect username/password entered!");
			return false; // username and password weren't matched
		}
		
		int initialMoney = getInitialMoney(userName);
		int deleteMoney = getWithdrawAmount(initialMoney);
		if (deleteMoney == -1) {
			return false;
		}
		if (deleteMoney > initialMoney) {
			System.out.println("Insufficient funds in account: " + userName);
			return false;
		}
		
		accounts[getAccountIndex(userName)] = constructAccount(userName, password, initialMoney - deleteMoney);
		writeFile(accounts);
		
		if (printBill) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd             HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			System.out.println("------------------------------------------");
			System.out.println(dtf.format(now));
			System.out.println("\t\tRKMS ATM");
			System.out.println("Account User Name: " + userName);
			System.out.println("Initial Money\t\t" + "$" + initialMoney);
			System.out.println("Money withdrawn\t\t" + "$" + deleteMoney + "-");
			System.out.println("________________________________");
			System.out.println("Total Money\t\t" + "$" + (initialMoney - deleteMoney));
			System.out.println("-------------------------------------------");
		}
		return true;
	}

	// TRANSFERFUNDS
	private static boolean transferFunds() throws IOException {
		String userName = getUserName();
		
		if (!userExists(userName)) {
			System.out.println("Account username: \"" + userName + "\" doesn't exists");
			return false;
		}
		
		String password = getPassword();
		if (!matchUserNameAndPassword(userName, password)) {
			System.out.println("Incorrect sender username/password entered!");
			return false; // username and password weren't matched
		}
		
		System.out.println("Enter reciever info");
		String recieverUserName = getUserName();
		System.out.println("Enter reciever username again for validation");
		String confirmedUserName = getUserName();
		
		if (!recieverUserName.equals(confirmedUserName)) {
			System.out.println("Usernames didn't match");
			return false;
		}
		
		if (!userExists(recieverUserName)) {
			System.out.println("Account username: \"" + recieverUserName + "\" doesn't exists");
			return false;
		}
		
		int initialMoney = parseMoney(accounts[getAccountIndex(userName)]);
		int deleteMoney = getWithdrawAmount(initialMoney);
		
		if (deleteMoney == -1) {
			System.out.println("Cancelling transfer funds");
			return false;
		}
		
		if (deleteMoney > initialMoney) {
			System.out.println(userName + " doesn't have sufficient funds for transfer");
			return false;
		} 
		
		accounts[getAccountIndex(userName)] = constructAccount(userName, password, initialMoney - deleteMoney);
		writeFile(accounts);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd             HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		System.out.println("------------------------------------------");
		System.out.println(dtf.format(now));
		System.out.println("\t\tRKMS ATM");
		System.out.println("\tTransfer Funds");
		System.out.println("Reciever is: " + recieverUserName);
		System.out.println("Account User Name: " + userName);
		System.out.println("Initial Money\t\t" + "$" + initialMoney);
		System.out.println("Money withdrawn\t\t" + "$" + deleteMoney);
		System.out.println("________________________________");
		System.out.println("Total Money\t\t" + "$" + (initialMoney - deleteMoney));
		System.out.println("-------------------------------------------");
		
		String recieverPassword = parsePassword(accounts[getAccountIndex(recieverUserName)]);
		accounts[getAccountIndex(recieverUserName)] = constructAccount(recieverUserName, recieverPassword, initialMoney + deleteMoney);
		writeFile(accounts);
		
		return true;
	}
	
	// CHANGEUSERNAME
	private static boolean changeUserName(String userName, String password) throws IOException {
		String prevUserName = getUserName();
		
		if (!userName.equals(prevUserName)) {
			System.out.println("Usernames didn't match");
			return false;
		}

		System.out.println("Taking new username");
		String newUserName = getUserName();
		System.out.println("Confirming new username");
		String confirmUserName = getUserName();

		if (!userExists(userName)) {
			System.out.println("Username already exists, can't have duplicate users");
			return false;
		}
		
		if (!newUserName.equals(confirmUserName)) {
			System.out.println("Usernames didn't match");
			return false;
		}

		int money = getInitialMoney(userName);
		
		accounts[getAccountIndex(userName)] = constructAccount(newUserName, password, money);
		writeFile(accounts);

		System.out.println("Your username has been changed successfully!");
		return true;
	}

	// CHANGEPASSWORD
	public static boolean changePassword(String userName, String password) throws IOException {
		String prevPassword = getPassword();
		
		if (!password.equals(prevPassword)) {
			System.out.println("Passwords didn't match");
			return false;
		}

		System.out.println("Taking new password");
		String newPassword = getPassword();
		System.out.println("Confirming new password");
		String confirmPassword = getPassword();

		if (!newPassword.equals(confirmPassword)) {
			System.out.println("Passwords didn't match");
			return false;
		}

		int money = getInitialMoney(userName);
		
		accounts[getAccountIndex(userName)] = constructAccount(userName, newPassword, money);
		writeFile(accounts);
		
		System.out.println("Your password has been changed successfully!");

		return true;
	}
	
	// ACCOUNTINFO
	private static void accountInfo(String userName, String password) {
		int money = getInitialMoney(userName);

		System.out.println("Username: " + userName);
		System.out.println("Password: " + password);
		System.out.println("Balance: " + money);
	}
	
	// EDITPROFILE
	private static boolean editProfile() throws IOException {
		Scanner input = new Scanner(System.in);
		String userName = getUserName();

		if (!userExists(userName)) {
			System.out.println("Account username: \"" + userName + "\" doesn't exists");
			return false;
		}

		String password = getPassword();
		if (!matchUserNameAndPassword(userName, password)) {
			System.out.println("Incorrect username/password entered!");
			return false; 
		}

		int option;
		do {
			System.out.println("1. Change Username");
			System.out.println("2. Change Password");
			System.out.println("3. View Account Information");
			System.out.println("4. Exit");
			System.out.print("Enter the option: ");
			option = input.nextInt();
			input.nextLine(); // clear input buffer
			switch (option) {
			case 1:
				changeUserName(userName, password);
				break;
			case 2:
				changePassword(userName, password);
				break;
			case 3:
				accountInfo(userName, password);
				break;
			case 4:
				break;
			default:
				System.out.println("Invalid Option Entered");
				continue;
			}
		} while(option < 1 || option > 4);
		return true;
	}
	
	// DELETEACCOUNT
	private static boolean deleteAccount() throws IOException {
		String userName = getUserName();
		
		if (!userExists(userName)) {

			System.out.println("Username doesn't exists");
			return false;
		}
		
		String userPassword = getPassword();
		
		if (!matchUserNameAndPassword(userName, userPassword)) {
			System.out.println("Failed deleting account. Username and password didn't match");
			
			return false;
		}
		
		int money = getInitialMoney(userName);
		String account = constructAccount(userName, userPassword, money);
		removeAccount(account);
		System.out.println("Account deleted successfully");
		
		return true;
	}

	// MAIN
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		try {
			readFile(); // VERY FIRST STEP FOR PROGRAM TO WORK
			// We can do nothing if we don't have an array containing the accounts for processing
			int option;
			do {
				System.out.println("\n--------- Welcome to RKMS ATM ----------");
				System.out.println("1. Create Account");
				System.out.println("2. Delete Account");
				System.out.println("3. Deposit Money");
				System.out.println("4. Withdraw Money");
				System.out.println("5. Edit Profile");
				System.out.println("6. Transfer Funds");
				System.out.println("7. Exit");
				System.out.println("----------------------------------------");
				System.out.print("Enter your choice: ");
				option = input.nextInt();
				input.nextLine(); // clear input buffer
				switch (option) {
				case 1: {
					createAccount();
					continue;
				}
				case 2: {
					deleteAccount();
					continue;
				}
				case 3: {
					depositMoney(true);
					continue;
				}
				case 4: {
					withDrawMoney(true);
					continue;
				}
				case 5: {
					editProfile();
					continue;
				}
				case 6: {
					transferFunds();
					continue;
				}
				case 7: {
					// do nothing, we'll break the main loop
					break;
				}
				default: {
					System.out.println("Invalid option entered");
					break;
				}
				}

				if (option == 7) {
					break;
				}

			} while(true);

			System.out.println("--- Thank you for visiting RKMS ATM ----");
		} catch (FileNotFoundException fNotFound) {
			System.out.println("Error: File not found");
			fNotFound.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error occurred");
			e.printStackTrace();
		}

		input.close();
	}
}
