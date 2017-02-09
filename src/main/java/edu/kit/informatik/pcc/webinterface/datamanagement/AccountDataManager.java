package edu.kit.informatik.pcc.webinterface.datamanagement;

import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.mailservice.MailService;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.apache.commons.mail.EmailException;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 * The AccountDataManager manages all operations which contain account dates.
 */
public class AccountDataManager {

    private static final String HOST = "http://laubenstone.de:2222/";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String WRONGACCOUNT = "WRONG ACCOUNT";
    //attributes
    private static Account account = null;
    private static ResourceBundle errors = ResourceBundle.getBundle("ErrorMessages");
    private static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

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

        UUID id = UUID.randomUUID();
        account = new Account(mail, password);
        String ret = ServerProxy.createAccount(account, id);

        switch (ret) {
            case SUCCESS:
                startVerification(id);
                return true;
            case FAILURE:
                MessageBox.createInfo()
                        .withMessage(errors.getString("createFail"))
                        .open();
                return false;
            case "ACCOUNT EXISTS":
                MessageBox.createInfo()
                        .withMessage(errors.getString("existingAccount"))
                        .open();
                return false;
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(errors.getString("createFail"))
                        .open();
                return false;
        }
    }

    /**
     * This method starts the verification by creating uuid and sending it
     * per mail to the user and per ServerProxy to the database.
     *
     */
    private static void startVerification(UUID id) {
        //TODO: verification

        String text = messages.getString("mailText");
        String subject = messages.getString("mailSubject");
        String link = "http://laubenstone.de:2222/webservice/verifyAccount?uuid=" + id.toString();
        text += link;
        String to = account.getMail();
        String from = messages.getString("mail");
        try {
            MailService.send(from, to, subject, text);
        } catch (EmailException | IOException e) {
            e.printStackTrace();
        }
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
        account = new Account(mail, password);
        String ret = ServerProxy.authenticateUser(account);

        switch (ret) {
            case "NOT EXISTING":
                MessageBox.createInfo()
                        .withMessage(errors.getString("noSuchAccount"))
                        .open();
                break;
            case "WRONG PASSWORD":
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongPassword"))
                        .open();
                break;
            case "NOT VERIFIED":
                MessageBox.createInfo()
                        .withMessage(errors.getString("notVerified"))
                        .open();
                break;
            case SUCCESS:
                return true;
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(errors.getString("authenticateFail"))
                        .open();
                break;
        }

        account = null;
        return false;
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
                    .withMessage(errors.getString("changeFail"))
                    .open();
            return false;
        }

        if (!password.equals(account.getPassword())) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("wrongPassword"))
                    .open();
            return false;
        }

        Account newAccount = new Account(mailNew, passwordNew);
        ret = ServerProxy.changeAccount(account, newAccount);

        switch (ret) {
            case WRONGACCOUNT:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongAccount"))
                        .open();
                break;
            case SUCCESS:
                MessageBox.createInfo()
                        .withMessage(errors.getString("accountChanged"))
                        .open();
                return true;
            case FAILURE:
            default:
                MessageBox.createInfo()
                        .withMessage(errors.getString("changeFail"))
                        .open();
                break;
        }
        return true;
    }

    /**
     * This method sends a delete order for the account attribute to the ServerProxy.
     *
     * @return true on success false else
     */
    public static boolean deleteAccount() {

        if (account == null) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("deleteFail"))
                    .open();
            return false;
        }

        String ret = ServerProxy.deleteAccount(account);

        switch (ret) {
            case WRONGACCOUNT:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongAccount"))
                        .open();
                break;
            case SUCCESS:
                MessageBox.createInfo()
                        .withMessage(errors.getString("accountDeleted"))
                        .open();
                account = null;
                return true;
            case FAILURE:
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(errors.getString("deleteFail"))
                        .open();
                break;
        }
        return false;
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

    public static void setAccount(Account account) {
        AccountDataManager.account = account;
    }

    public static void removeAccount() {
        account = null;
    }

}
