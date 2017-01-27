package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.ui.Button;

/**
 * Created by chris on 27.01.2017.
 */
public class TableEntry {

    private String name;
    private Button download;
    private Button info;
    private Button delete;

    public TableEntry(String name, Button download, Button info, Button delete) {
        this.name = name;
        this.download = download;
        this.delete = delete;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public Button getDownload() {
        return download;
    }

    public Button getInfo() {
        return info;
    }

    public Button getDelete() {
        return delete;
    }
}
