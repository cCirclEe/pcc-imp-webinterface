package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.ui.Table;

import java.util.LinkedList;

/**
 * Created by chris on 17.01.2017.
 */
public class VideoTable extends Table {

    private LinkedList downloadButtonList;
    private LinkedList infoButtonList;
    private LinkedList deleteButtonList;
    private LinkedList videos;

    public VideoTable () {
        super();
        prepareVideos();
        prepareButtons();
    }

    private void prepareVideos() {

    }

    private void prepareButtons() {

    }


}
