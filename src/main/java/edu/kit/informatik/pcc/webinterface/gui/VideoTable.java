package edu.kit.informatik.pcc.webinterface.gui;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.datamanagement.Video;
import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;

import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * @author Josh Romanowski, Christoph Hörtnagl
 */
public class VideoTable extends Table {

    private static final String tableId = "VideoTable";
    private static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
    private LinkedList<Video> videos;

    public VideoTable () {
        super();
    }

    public void update() {
        videos = VideoDataManager.getVideos();
        this.addContainerProperty(messages.getString(tableId + "name"), String.class, null);
        this.addContainerProperty(messages.getString(tableId + "download"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "info"), Button.class, null);
        this.addContainerProperty(messages.getString(tableId + "delete"), Button.class, null);
        this.removeAllItems();
        prepareEntries();
    }

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

    private void showFileDownloadDialogue(FileDownloader fileDownloader) {
        Window subWindow = new Window("Bestätigen Sie den Download");
        subWindow.setHeight("100px");
        subWindow.setWidth(300, Sizeable.Unit.PIXELS);
        VerticalLayout subLayout = new VerticalLayout();
        Button button = new Button("Download starten");
        button.addClickListener(
                (Button.ClickListener) event -> Notification.show("you clicked it")
        );
        subLayout.setSizeFull();
        subLayout.setMargin(true);
        subLayout.addComponent(button);
        fileDownloader.extend(button);
        subWindow.setContent(subLayout);
        subWindow.center();
        getUI().addWindow(subWindow);
    }
}
