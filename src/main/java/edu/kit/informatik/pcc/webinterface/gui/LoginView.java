package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;

import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 * The View which is showed after every start of the website.
 */
public class LoginView extends VerticalLayout implements View {

    public final static String viewID = "LoginView";
    private TextField mailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;

    //Constructor
    public LoginView(MyUI ui) {
        //initialization
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
        mailField = new TextField(messages.getString(viewID + "mailField"));
        passwordField = new PasswordField(messages.getString(viewID + "passwordField"));
        loginButton = new Button(messages.getString(viewID + "loginButton"));
        registerButton = new Button(messages.getString(viewID + "registerButton"));

        loginButton.addClickListener(
                (ClickListener) event -> {
                    if (AccountDataManager.authenticateAccount(mailField.getValue(), passwordField.getValue())) {
                        ui.login();
                    }
                }
        );

        registerButton.addClickListener(
                (ClickListener) event -> {
                    if (AccountDataManager.createAccount(mailField.getValue(),passwordField.getValue())) {
                        MessageBox.createInfo()
                                .withMessage(messages.getString(viewID + "registerInfo"))
                                .open();
                    }
                }
        );

        this.addComponent(mailField);
        this.addComponent(passwordField);
        this.addComponent(loginButton);
        this.addComponent(registerButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
