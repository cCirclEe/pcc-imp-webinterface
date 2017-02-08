package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by chris on 17.01.2017.
 */
public class VideoView extends VerticalLayout implements View{

    //attributes
    public static final String viewID = "VideoView";
    private VideoTable videoTable;

    //constructors
    public VideoView() {
        videoTable = new VideoTable();
        this.addComponent(videoTable);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
