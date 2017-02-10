package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.datamanagement.Video;
import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;

import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * This class creates a table of all videos and adds buttons for functionality.
 *
 * @author Josh Romanowski, Christoph Hörtnagl
 */
public class VideoTable extends Table {

    private static final String tableId = "VideoTable";
    private static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
    private LinkedList<Video> videos;

    public VideoTable () {
        super();
        this.setSizeFull();
    }

    /**
     * Creates the table.
     */
    public void update() {
        videos = VideoDataManager.getVideos();
        this.addContainerProperty(messages.getString(tableId + "Name"), String.class, null);
        this.addContainerProperty(messages.getString(tableId + "Download"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "Info"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "Delete"), Button.class, null);
        this.removeAllItems();
        prepareEntries();
    }

    /**
     * This method sets the table entries up.
     */
    private void prepareEntries() {
        int i = 2;

        for (Video v:videos) {
            Button download = new Button(FontAwesome.DOWNLOAD);
            download.addClickListener(
                    (ClickListener) event -> {
                        FileDownloader downloader = VideoDataManager.downloadVideo(v.getId());
                        if (downloader != null) {
                            showFileDownloadDialogue(downloader);
                        }
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

            Button delete = new Button(FontAwesome.REMOVE);
            delete.addClickListener(
                    (ClickListener) event -> {
                        VideoDataManager.deleteVideo(v.getId());
                        update();
                    }
            );
            this.addItem(new Object[] {v.getName(), download, info , delete}, i);
            i++;
        }
    }

    /**
     * This method opens a window to confirm the download.
     *
     * @param fileDownloader filedownloader
     */
    private void showFileDownloadDialogue(FileDownloader fileDownloader) {
        Window subWindow = new Window();
        subWindow.setHeight(20, Unit.PERCENTAGE);
        subWindow.setWidth(20, Unit.PERCENTAGE);
        subWindow.setResizable(false);
        VerticalLayout subLayout = new VerticalLayout();
        Button button = new Button(messages.getString(tableId + "Download"));
        subLayout.setSizeFull();
        subLayout.setMargin(true);
        subLayout.addComponent(button);
        subLayout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
        fileDownloader.extend(button);
        subWindow.setContent(subLayout);
        subWindow.center();
        getUI().addWindow(subWindow);
    }
}
