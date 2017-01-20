package edu.kit.informatik.pcc.webinterface.datamanagement;

/**
 * Created by chris on 17.01.2017.
 * The AccountDataManager manages all operations which contain account dates.
 */
public class AccountDataManager {

    //attributes
    public static Account account = null;

    //methods

    /**
     * This method creates an account by sending the order to the ServerProxy.
     *
     * @param mail mail address
     * @param password password
     * @return true on success false else
     */
    public static boolean createAccount(String mail, String password) {
        account = new Account(mail, password);
        return true;
    }

    /**
     * This method startes the verification by creating uuid and sending it
     * per mail to the user and per ServerProxy to the database.
     *
     * @param mail mail address
     * @param password password
     */
    private static void startVerification(String mail, String password) {

    }

    /**
     * This method fetches account data via ServerProxy and compares it to
     * the given parameters.
     *
     * @param mail mail address
     * @param password password
     * @return true on success false else
     */
    public static boolean authenticateAccount(String mail, String password) {
        return true;
    }

    /**
     * This method checks if the given account is verified via the ServerProxy.
     *
     * @param mail mail address
     * @param password password
     * @return true on success false else
     */
    public static boolean checkVerification(String mail, String password) {
        return true;
    }

    /**
     * This method checkes if the password is correct and then changes the account data
     * via the ServerProxy.
     *
     * @param mailNew new mail address
     * @param passwordNew new password
     * @param password password
     * @return true on success false else
     */
    public static boolean changeAccount(String password, String mailNew, String passwordNew) {
        return true;
    }

    /**
     * This method sends a delete order for the account attribute to the ServerProxy.
     *
     * @return true on success false else
     */
    public static boolean deleteAccount() {
        account = null;
        return true;
    }

    /**
     * This method sends the verification mail to user.
     *
     * @param mail the user mail
     */
    private static void sendMail(String mail) {

    }

}
