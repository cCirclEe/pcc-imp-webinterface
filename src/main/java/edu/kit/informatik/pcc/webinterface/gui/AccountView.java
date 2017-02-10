package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;

import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 * This View shows account data and options to change
 *
 * @author chris
 */
public class AccountView extends VerticalLayout implements View{

    //attributes
    public final static String viewID = "AccountView";
    private Label mailLabel;
    private TextField mailChangeField;
    private PasswordField passwordChangeField;
    private PasswordField passwordField;
    private Button changeButton;
    private Button deleteButton;

    //constructors
    public AccountView(MyUI ui) {
        //initialization
        this.setSpacing(true);
        this.setMargin(true);
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
        String mail = AccountDataManager.getAccount().getMail();
        mailLabel = new Label(mail);
        mailChangeField = new TextField(messages.getString(viewID + "mailChangeField"));
        passwordChangeField = new PasswordField(messages.getString(viewID + "passwordChangeField"));
        passwordField = new PasswordField(messages.getString(viewID + "passwordField"));
        changeButton = new Button(messages.getString(viewID + "changeButton"));
        deleteButton = new Button(messages.getString(viewID + "deleteButton"));

        changeButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (AccountDataManager.changeAccount(passwordField.getValue(), mailChangeField.getValue(), passwordChangeField.getValue())) {
                            ui.initializeGraphicalComponents();
                        }
                    }
                }
        );

        deleteButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (AccountDataManager.deleteAccount()) {
                            ui.initializeGraphicalComponents();
                        }
                    }
                }
        );

        this.addComponent(mailLabel);
        this.addComponent(mailChangeField);
        this.addComponent(passwordChangeField);
        this.addComponent(passwordField);
        this.addComponent(changeButton);
        this.addComponent(deleteButton);
    }

    //methods
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
