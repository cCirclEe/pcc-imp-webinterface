package edu.kit.informatik.pcc.webinterface.datamanagement;

import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;

import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 * The AccountDataManager manages all operations which contain account dates.
 */
public class AccountDataManager {

    //attributes
    //TODO: not public
    public static Account account = null;
    private static ResourceBundle messages = ResourceBundle.getBundle("ErrorMessages");

    //methods

    /**
     * This method creates an account by sending the order to the ServerProxy.
     * Then handles the answer.
     *
     * @param mail mail address
     * @param password password
     * @return true on success false else
     */
    public static boolean createAccount(String mail, String password) {
        String ret = "";
        if (account != null) {
            MessageBox.createInfo()
                    .withMessage(messages.getString("existingAccount"))
                    .open();
            return false;
        }

        UUID id = UUID.randomUUID();
        account = new Account(mail, password);
        ret = ServerProxy.createAccount(account,id);

        switch (ret) {
            case "SUCCESS":
                startVerification(mail, password, id);
                return true;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("createFail"))
                        .open();
                return false;
            case "ACCOUNT EXISTS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("existingAccount"))
                        .open();
                return false;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("createFail"))
                        .open();
                return false;
        }
    }

    /**
     * This method starts the verification by creating uuid and sending it
     * per mail to the user and per ServerProxy to the database.
     *
     * @param mail mail address
     * @param password password
     */
    private static void startVerification(String mail, String password, UUID id) {
        //TODO: verification
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
        String ret = "";

        // This if makes it possible to use authenticate even a user has logged in
        if (account == null) {
            Account existingAccount = new Account(mail, password);
            ret = ServerProxy.authenticateUser(existingAccount);
        } else {
            ret = ServerProxy.authenticateUser(account);
        }

        switch (ret) {
            case "NO ACCOUNT ID":
                MessageBox.createInfo()
                        .withMessage(messages.getString("noSuchAccount"))
                        .open();
                return false;
            case "WRONG PASSWORD":
                MessageBox.createInfo()
                        .withMessage(messages.getString("wrongPassword"))
                        .open();
                return false;
            case "NOT VERIFIED":
                MessageBox.createInfo()
                        .withMessage(messages.getString("notVerified"))
                        .open();
                return false;
            case "SUCCESS":
                return true;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("authenticateFail"))
                        .open();
                return false;
        }
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
        String ret = "";

        if (account == null) {
            MessageBox.createInfo()
                    .withMessage(messages.getString("changeFail"))
                    .open();
            return false;
        }

        if (!password.equals(account.getPassword())) {
            MessageBox.createInfo()
                    .withMessage(messages.getString("wrongPassword"))
                    .open();
            return false;
        }

        Account newAccount = new Account(mailNew, passwordNew);
        ret = ServerProxy.changeAccount(account, newAccount);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
                        .open();
                return false;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
                        .open();
                return false;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("accountChanged"))
                        .open();
                return true;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
                        .open();
                return false;
        }
    }

    /**
     * This method sends a delete order for the account attribute to the ServerProxy.
     *
     * @return true on success false else
     */
    public static boolean deleteAccount() {
        String ret = "";

        if (account == null) {
            MessageBox.createInfo()
                    .withMessage(messages.getString("deleteFail"))
                    .open();
            return false;
        }

        ret = ServerProxy.deleteAccount(account);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("deleteFail"))
                        .open();
                return false;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("deleteFail"))
                        .open();
                return false;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("accountDeleted"))
                        .open();
                account = null;
                return true;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("deleteFail"))
                        .open();
                return false;
        }
    }

    /**
     * This method sends the verification mail to user.
     *
     * @param mail the user mail
     */
    private static void sendMail(String mail) {

    }

    public static Account getAccount() {
        return account;
    }

    public static void removeAccount() {
        account = null;
    }

}
