package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;
import edu.kit.informatik.pcc.webinterface.datamanagement.Video;
import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 */
public class VideoTable extends Table {

    private LinkedList<Video> videos;
    private static final String tableId = "VideoTable";
    private static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");

    public VideoTable () {
        super();
        videos = VideoDataManager.getVideos();
        this.addContainerProperty(messages.getString(tableId + "name"), String.class, null);
        this.addContainerProperty(messages.getString(tableId + "download"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "info"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "delete"), Button.class, null);
        prepareEntries();
    }

    private void prepareEntries() {
        int i = 2;

        for (Video v:videos) {

            Button download = new Button(FontAwesome.DOWNLOAD);
            download.addClickListener(
                    (ClickListener) event -> {
                        VideoDataManager.downloadVideo(v.getId());
                    }
            );

            Button info = new Button(FontAwesome.INFO);
            info.addClickListener(
                    (ClickListener) event -> {
                        MessageBox.createInfo()
                                .withMessage(v.getInfo())
                                .open();
                    }
            );

            Button delete = new Button(FontAwesome.INFO);
            delete.addClickListener(
                    (ClickListener) event -> {
                        VideoDataManager.deleteVideo(v.getId());
                    }
            );

            this.addItem(new Object[] {v.getName(), download, info , delete}, i);
        }
    }
}
