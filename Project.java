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
    private static String parseUserName(String account) {
    	// saba:thedumbestdumbo#21516
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

    // get number of accounts
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

	private static void removeAccount(String account) throws IOException {
		accounts = deleteArrayElement(accounts, account);
		writeFile(accounts);
	}

	private static void appendAccount(String account) throws IOException {
		accounts = addArrayElement(accounts, account);
		writeFile(accounts);
	}

	// old contents are overwritten by contents of array accounts
	private static void writeFile(String[] accounts) throws IOException {
		PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		for (int i = 0; i < accounts.length; i++) {
			file.println(accounts[i]);
		}
		file.close();
	}

	// read contents of file in accounts
	private static void readFile() throws FileNotFoundException {
		accounts = new String[getAccountCount()];
		File file = new File(fileName);
		Scanner acc = new Scanner(file);
		
		for(int i = 0; i < accounts.length; i++) {
			accounts[i] = acc.nextLine();
		}
		acc.close();
	}

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

	private static String getPassword() {
		Scanner input = new Scanner(System.in);
		String password;
		do {
			System.out.print("Enter password: ");
			password = input.nextLine();
			if (password.length() < 3) {
				System.out.println("Invalid Password entered.");
			}
		} while (password.length() < 3);
		return password;
	}

	private static int getMoney() {
		Scanner input = new Scanner(System.in);
		int money;
		do {
			System.out.print("Enter money: ");
			money = input.nextInt();
			if (money < 10000) {
				System.out.println("Minimum 10k initial deposit is required.");
			}
		} while (money < 10000);
		return money;
	}

	// returns true if the user exists in the file already
	private static boolean userExists(String userName) {
		for (int i = 0; i < accounts.length; i++) {
			if (parseUserName(accounts[i]).equals(userName)) {
				return true;
			}
		}
		return false;
	}

	private static String constructAccount(String userName, String password, int money) {
		return userName + ":" + password + "#" + String.valueOf(money);
	}

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
	private static boolean createAccount() throws IOException {
		String userName = getUserName();
		if (userExists(userName)) { 
			return false;
		}

		String password = getPassword();
		int money = getMoney();
		String account = constructAccount(userName, password, money);
		appendAccount(account);
		
		return true;
	}
	
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
	
	private static int getInitialMoney(String userName) {
		for (int i = 0; i < accounts.length; i++) { // traverse the accounts array
			if (parseUserName(accounts[i]).equals(userName)) {
				return parseMoney(accounts[i]); // return the money for userName
			}
		}
		return -1;
	}
	
	private static int inputMoney() {
		int money;
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("Enter amount to deposit: ");
			money = input.nextInt();
			if (money < 0) {
				System.out.println("Invalid value entered");
			}
		} while(money < 0);
		input.close();
		return money;
	}
	
	private static int getAccountIndex(String userName) {
		for (int i = 0; i < accounts.length; i++) {
				if (parseUserName(accounts[i]).equals(userName)) {
					return i;
				}
		}
		return -1; // THIS SHOULD NEVER BE EXECUTED: IF THE FUNCTION IS RETURNING -1 THEN THERE'S AN ERROR
	}
	
	private static boolean depositMoney(boolean printBill) throws IOException {
		String userName = getUserName();
		if (!userExists(userName)) {
			return false; // there is no such user, we can't deposit money
		}
		
		String password = getPassword();
		if (!matchUserNameAndPassword(userName, password)) {
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
			System.out.println("Money deposited\t\t" + "$" + addMoney);
			System.out.println("________________________________");
			System.out.println("Total Money\t\t" + "$" + (initialMoney + addMoney));
			System.out.println("-------------------------------------------");
		}
		
		return true;
	}
	
	private static int enterMoneyToWithdraw(int initialMoney) {
		Scanner input = new Scanner(System.in);
		int money = 0;
		do {
			System.out.println("Enter amount you want to withdraw: ");
			money = input.nextInt();
			if(money > initialMoney) {
				System.out.println("The amount you have entered is greater than the amount in your account");
			} else if (money < 0) {
				System.out.println("You entered negative amount");
			}
		}while(money > initialMoney || money < 0);
		
		return money;
	}
	
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

	private static boolean withDrawMoney(boolean printBill) throws IOException {
		String userName = getUserName();
		if (!userExists(userName)) {
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
			System.out.println("Money withdrawn\t\t" + "$" + deleteMoney);
			System.out.println("________________________________");
			System.out.println("Total Money\t\t" + "$" + (initialMoney - deleteMoney));
			System.out.println("-------------------------------------------");
		}
		
		return true;
	}

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

	public static void main(String[] args) {

		try {
			readFile(); // VERY FIRST STEP FOR PROGRAM TO WORK
			// We can do nothing if we don't have an array containing the file for processing
			

		} catch (Exception e) {
			System.out.println("Error doing IO processing");
			e.printStackTrace();
		}

	}

}
