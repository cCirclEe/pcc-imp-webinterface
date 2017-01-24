package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;
import edu.kit.informatik.pcc.webinterface.gui.navigation.Menu;

import javax.servlet.annotation.WebServlet;
import java.util.ResourceBundle;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("valo")
public class MyUI extends UI {

    //attributes
    private HorizontalLayout background;
    private VerticalLayout menuArea;
    private VerticalLayout contentArea;
    private Menu menu;
    private Navigator navigator;

    /**
     *  The Servlet in which the site runs.
     */
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


    //methods
    /**
     * This method is called whenever somebody openes the UI, we do the initialization of
     * our prameters here.
     *
     * @param vaadinRequest the request
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        background = new HorizontalLayout();
        menuArea = new VerticalLayout();
        contentArea = new VerticalLayout();
        Responsive.makeResponsive(this);

        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

        menu = new Menu(this);
        menu.addMenuItem(messages.getString(AccountView.viewID), AccountView.viewID);

        navigator = new Navigator(this, contentArea);
        navigator.addView(LoginView.viewID, new LoginView(this));
        navigator.addView(AccountView.viewID, new AccountView());
        this.setNavigator(navigator);


        background.addComponent(contentArea);
        setContent(background);

        navigator.navigateTo(LoginView.viewID);
    }

    public void login() {
        menu.addUserMenu(AccountDataManager.getAccount().getMail());
        menuArea.addComponent(menu);
        menuArea.setHeight("800px");
        background.addComponentAsFirst(menuArea);
        navigator.navigateTo(AccountView.viewID);
    }

}
