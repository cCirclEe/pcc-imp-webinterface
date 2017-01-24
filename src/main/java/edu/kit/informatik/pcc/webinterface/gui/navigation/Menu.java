package edu.kit.informatik.pcc.webinterface.gui.navigation;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.MenuBar.MenuItem;
import edu.kit.informatik.pcc.webinterface.gui.MyUI;

/**
 * Created by chris on 17.01.2017.
 * The Menu class shows Buttons to navigate on the website.
 *
 */
public class Menu extends VerticalLayout {

    //attributes
    private MyUI ui;
    private CssLayout menuItemsLayout;
    private MenuBar userMenu;
    private MenuItem userItem;
    private Label menuCaption;

    //constructors
    public Menu(MyUI myUI) {
        super();
        this.addStyleName(ValoTheme.UI_WITH_MENU);
        this.ui = myUI;
        menuItemsLayout = new CssLayout();
        userMenu = new MenuBar();


        this.setPrimaryStyleName("valo-menu");

        menuCaption = new Label("Menu", ContentMode.HTML);
        menuCaption.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(menuCaption);
        logoWrapper.setComponentAlignment(menuCaption, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        this.addComponent(logoWrapper);

        userMenu.addStyleName("user-menu");
        userItem = userMenu.addItem("", null);

        this.addComponent(userMenu);

        this.addComponent(menuItemsLayout);
    }

    //methods

    public void addMenuItem(String caption, String viewID) {

        Button button = new Button(caption, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ui.getNavigator().navigateTo(viewID);
            }
        });
        button.setPrimaryStyleName("valo-menu-item");
        menuItemsLayout.addComponent(button);
    }

    public void addUserMenu(String text) {
        if (text == null) {
            return;
        }
        userItem.setText(text);
    }
}
