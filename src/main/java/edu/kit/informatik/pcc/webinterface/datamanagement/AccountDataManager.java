package edu.kit.informatik.pcc.webinterface.datamanagement;

import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 * The AccountDataManager manages all operations which contain account dates.
 */
public class AccountDataManager {

    private static final String MAINADRESS = "http://laubenstone.de:2222/";
    //attributes
    private static Account account = null;
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

        UUID id = UUID.randomUUID();
        account = new Account(mail, password);
        ret = ServerProxy.createAccount(account,id);
        System.out.println(ret);

        switch (ret) {
            case "SUCCESS":
                startVerification(id);
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
                System.out.println(ret);
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
     */
    private static void startVerification(UUID id) {
        //TODO: verification
        //language=JSON
        String json = account.getAsJson();
        Form f = new Form();
        f.param("data", json);
        f.param("uuid", id.toString());
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(MAINADRESS).path("webservice").path("verifyAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
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

        account = new Account(mail, password);
        ret = ServerProxy.authenticateUser(account);

        switch (ret) {
            case "NOT EXISTING":
                MessageBox.createInfo()
                        .withMessage(messages.getString("noSuchAccount"))
                        .open();
                break;
            case "WRONG PASSWORD":
                MessageBox.createInfo()
                        .withMessage(messages.getString("wrongPassword"))
                        .open();
                break;
            case "NOT VERIFIED":
                MessageBox.createInfo()
                        .withMessage(messages.getString("notVerified"))
                        .open();
                break;
            case "SUCCESS":
                return true;
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(messages.getString("authenticateFail"))
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
        System.out.println(AccountDataManager.getAccount().getMail());
        System.out.println(AccountDataManager.getAccount().getPassword());
        System.out.println(newAccount.getMail());
        System.out.println(newAccount.getPassword());
        System.out.println(ret);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
                        .open();
                break;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
                        .open();
                break;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("accountChanged"))
                        .open();
                return true;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("changeFail"))
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
                break;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("deleteFail"))
                        .open();
                break;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("accountDeleted"))
                        .open();
                account = null;
                return true;
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(messages.getString("deleteFail"))
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
