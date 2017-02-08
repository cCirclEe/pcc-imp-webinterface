package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;
import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;
import edu.kit.informatik.pcc.webinterface.gui.navigation.Menu;

import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    private static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
    //attributes
    private Panel background;
    private CustomLayout back;
    private VerticalLayout menuArea;
    private VerticalLayout contentArea;
    private Menu menu;
    private Navigator navigator;

    /**
     * This method is called whenever somebody openes the UI, we do the initialization of
     * our prameters here.
     *
     * @param vaadinRequest the request
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        //this.setSizeUndefined();

        VideoDataManager.setUI(this);

        initializeGraphicalComponents();

    }


    //methods

    public void initializeGraphicalComponents() {

        //CustomLayout loginLayout = new CustomLayout("login.html");
        background = new Panel();
        menuArea = new VerticalLayout();
        contentArea = new VerticalLayout();
        background.setSizeFull();

        AccountDataManager.setAccount(null);
        VideoDataManager.removeVideos();

        LoginView login = new LoginView(this);
        background.setContent(login);

        setContent(background);
    }

    public void login() {
        //set User after login and add menu to the view
        //set up the menu
        background = new Panel();
        setContent(background);

        File file = new File("HTMLExample/layouts/index.html");
        try {
            back = new CustomLayout(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        back.setSizeUndefined();

        navigator = new Navigator(this, contentArea);
        navigator.addView(AccountView.viewID, new AccountView(this));
        navigator.addView(VideoView.viewID, new VideoView());

        navigator.setErrorView(new VideoView());
        this.setNavigator(navigator);

        menu = new Menu(this);
        menu.addMenuItem(messages.getString(AccountView.viewID), AccountView.viewID);
        menu.addMenuItem(messages.getString(VideoView.viewID), VideoView.viewID);
        menu.addLogout();

        menuArea.addComponent(menu);

        menu.addUserMenu(AccountDataManager.getAccount().getMail());
        menu.setSizeUndefined();
        contentArea.setSizeUndefined();

        back.addComponent(menu, "menuArea");
        back.addComponent(contentArea, "contentArea");

        background.setContent(back);
        background.setSizeUndefined();

        navigator.navigateTo(VideoView.viewID);
    }

    public void logout() {
        initializeGraphicalComponents();
    }

    /**
     * The Servlet in which the site runs.
     */
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
