package edu.kit.informatik.pcc.webinterface.datamanagement;

import com.vaadin.server.VaadinSession;
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
 *
 * @author chris
 */
public class AccountDataManager {

    private static final String HOST = "http://laubenstone.de:2222/";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String WRONGACCOUNT = "WRONG ACCOUNT";
    private static final String ACCOUNTEXISTS = "ACCOUNT EXISTS";
    private static final String NOTEXISTING = "NOT EXISTING";
    private static final String WRONGPASSWORD = "WRONG PASSWORD";
    private static final String NOTVERIFIED = "NOT VERIFIED";
    private static final int PASSWORDMIN = 6;

    //attributes
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
    public static boolean createAccount(String mail, String password, VaadinSession session) {

        UUID id = UUID.randomUUID();
        if (!MailService.isValidEmailAddress(mail)) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("noLegitMail"))
                    .open();
            return false;
        }

        if (password.length() < PASSWORDMIN) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("toShortPassword"))
                    .open();
            return false;
        }

        Account account = new Account(mail, password);
        String ret = ServerProxy.createAccount(account, id);

        switch (ret) {
            case SUCCESS:
                session.setAttribute("account", account);
                sendVerificationMail(id, session);
                return true;
            case FAILURE:
                MessageBox.createInfo()
                        .withMessage(errors.getString("createFail"))
                        .open();
                return false;
            case ACCOUNTEXISTS:
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
     * This method sends the given parameters to ServerProxy to authenticate.
     *
     * @param mail mail address
     * @param password password
     * @return true on success false else
     */
    public static boolean authenticateAccount(String mail, String password, VaadinSession session) {
        Account account = new Account(mail, password);
        String ret = ServerProxy.authenticateUser(account);

        switch (ret) {
            case NOTEXISTING:
                MessageBox.createInfo()
                        .withMessage(errors.getString("noSuchAccount"))
                        .open();
                break;
            case WRONGPASSWORD:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongPassword"))
                        .open();
                break;
            case NOTVERIFIED:
                MessageBox.createInfo()
                        .withMessage(errors.getString("notVerified"))
                        .open();
                break;
            case SUCCESS:
                session.setAttribute("account", account);
                return true;
            default:
                System.out.println(ret);
                MessageBox.createInfo()
                        .withMessage(errors.getString("authenticateFail"))
                        .open();
                break;
        }

        return false;
    }

    /**
     * This method checks if the password is correct and then changes the account data
     * via the ServerProxy.
     *
     * @param mailNew new mail address
     * @param passwordNew new password
     * @param password password
     * @return true on success false else
     */
    public static boolean changeAccount(String password, String mailNew,
                                        String passwordNew, VaadinSession session) {

        if (passwordNew.length() == 0 && mailNew.length() == 0) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("noChanges"))
                    .open();
            return false;
        }

        Account account = (Account) session.getAttribute("account");
        Account newAccount = new Account(mailNew, passwordNew);

        if (passwordNew.length() == 0) {
            newAccount = new Account(mailNew, account.getPassword());
        } else if (mailNew.length() == 0) {
            newAccount = new Account(account.getMail(), passwordNew);
        }

        if (!MailService.isValidEmailAddress(newAccount.getMail())) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("noLegitMail"))
                    .open();
            return false;
        }

        if (newAccount.getPassword().length() < PASSWORDMIN) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("toShortPassword"))
                    .open();
            return false;
        }

        if (!password.equals(account.getPassword())) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("wrongPassword"))
                    .open();
            return false;
        }


        String ret = ServerProxy.changeAccount(account, newAccount);
        session.setAttribute("account", newAccount);

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
    public static boolean deleteAccount(VaadinSession session) {

        Account account = (Account) session.getAttribute("account");
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
                session.setAttribute("account", null);
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
     * This method starts the verification by creating a link and sending it
     * per mail to the user.
     *
     * @param id the uuid of the account to verify.
     */
    private static boolean sendVerificationMail(UUID id, VaadinSession session) {
        Boolean ret = true;

        String text = messages.getString("mailText");
        String subject = messages.getString("mailSubject");
        String link = HOST + "webservice/verifyAccount?uuid=" + id.toString();
        text += link;
        Account account = (Account) session.getAttribute("account");
        String to = account.getMail();
        String from = messages.getString("mail");
        try {
            MailService.send(from, to, subject, text);
        } catch (EmailException | IOException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

}
