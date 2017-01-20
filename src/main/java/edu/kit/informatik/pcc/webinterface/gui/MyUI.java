package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import edu.kit.informatik.pcc.webinterface.gui.navigation.Menu;

import javax.servlet.annotation.WebServlet;

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
    private VerticalLayout background;
    private VerticalLayout menuArea;
    private VerticalLayout contentArea;
    private Menu menu;
    static Navigator navigator;

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
        background = new VerticalLayout();
        menuArea = new VerticalLayout();
        contentArea = new VerticalLayout();
        menu = new Menu(this);
        navigator = new Navigator(this, contentArea);
        navigator.addView(LoginView.viewID, new LoginView());
        navigator.addView(AccountView.viewID, new AccountView());
        background.addComponent(contentArea);
        navigator.navigateTo(LoginView.viewID);


        setContent(background);
    }


}
