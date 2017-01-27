package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by chris on 25.01.2017.
 */
public class ErrorView extends VerticalLayout implements View {

    private Label error;

    public ErrorView() {
       error = new Label("Sth went terribly wrong");
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this.addComponent(error);
    }
}
