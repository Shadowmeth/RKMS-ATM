import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

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

		System.out.println("Your account has been created sucessfully");
		return true;
	}

	public static void main(String[] args) {

		try {
			readFile(); // VERY FIRST STEP FOR PROGRAM TO WORK
			// We can do nothing if we don't have an array containing the file for processing
			if (!createAccount()) {
				System.out.println("Error creating account: Your account exists already");
			}


		} catch (Exception e) {
			System.out.println("Error doing IO processing");
			e.printStackTrace();
		}

	}

}
