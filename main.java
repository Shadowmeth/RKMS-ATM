import java.io.*;
import java.util.Scanner;
public class main {

    // extract the username from account line and return it
    private static String parseUsername(String account) {
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

    public static void main(String[] args) {

        try {
            File f = new File("accounts.txt");
            Scanner accounts = new Scanner(f);

            while (accounts.hasNextLine()) {
                // main program code comes here and then other functions are called that do all the processing
                // note that each function should be named according to what it's doing. For example addAccount is a better function name
                // than add or adda or addacc or acc. Each function should do a little thing and it should do it well. For example
                // parseMoney should only and ONLY extract the money in amount and return it. It shouldn't display the money, or call
                // any other function such as deposit or withdraw that modifies the money. Also each function should be properly
                // comments. The comments should describe what, rather than how as given above.

                // Even the menu displays should be turned into functions. So for example to display the main menu you would call
                // mainMenu();

                // We are using files to store account information. Each line in the file represents an account
                // accounts are stored the in format username:password#money
                // Also note that we choose the format ourselves, there is no restriction and we can in fact change it if desired.
                // But this format to store account information was chosen because it's probably the most simple

                // String account = accounts.nextLine();
                // System.out.println(parseMoney(account));
            }

            accounts.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
