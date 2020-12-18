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
	
	// old contents are overwritten
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
	
	public static void main(String[] args) {
		
		try {
			
			
		} catch (Exception e) {
			System.out.println("Error doing IO processing");
			e.printStackTrace();
		}
		
	}

}
