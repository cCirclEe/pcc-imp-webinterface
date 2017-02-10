package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 */
public class ImpressumView extends VerticalLayout implements View {

    public static final String viewID = "ImpressumView";
    ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

    public ImpressumView() {
        Label label = new Label(messages.getString(viewID + "Text"));
        this.addComponent(label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
