package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 *
 * This class shows the privacy information.
 *
 * @author chris
 */
public class PrivacyView extends VerticalLayout implements View {

    public static final String viewID = "PrivacyView";
    private ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

    public PrivacyView() {
        this.setSpacing(true);
        this.setMargin(true);
        this.setSizeFull();
        Panel panel = new Panel(messages.getString(viewID + "Privacy"));
        panel.setWidth(50, Sizeable.Unit.PERCENTAGE);
        Label label = new Label(messages.getString(viewID + "Text"));
        panel.setContent(label);
        this.addComponent(panel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
