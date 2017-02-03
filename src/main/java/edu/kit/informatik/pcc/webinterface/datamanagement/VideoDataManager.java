package edu.kit.informatik.pcc.webinterface.datamanagement;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.gui.MyUI;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 * This class handles all operations with video data.
 */
public class VideoDataManager {

    //attributes
    private static LinkedList<Video> videos = null;
    private static ResourceBundle errors = ResourceBundle.getBundle("ErrorMessages");
    private static MyUI ui;

    //methods

    public static void setUI(MyUI myui) {
        ui = myui;
    }

    /**
     * This method sends a request to download a video via the ServerProxy.
     *
     * @param videoID the id of the video to download
     */
    public static void downloadVideo(int videoID) {
        String ret = "";
        File file = new File("");

        file = ServerProxy.videoDownload(videoID, AccountDataManager.getAccount());

        if (file == null) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("videoDownloadFail"))
                    .open();
            return;
        }

        //TODO: implement download !!!!
        FileResource resource = new FileResource(file);

        FileDownloader fileDownloader = new FileDownloader(resource);
        Window subWindow = new Window("Download");
        VerticalLayout subLayout = new VerticalLayout();
        Button button = new Button("start Download");
        button.addClickListener(
                (Button.ClickListener) event -> Notification.show("you clicked it")
        );
        subLayout.addComponent(button);
        fileDownloader.extend(button);
        subWindow.setContent(subLayout);
        subWindow.center();
        ui.addWindow(subWindow);
    }

    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID) {
        String ret = "";

        ret = ServerProxy.videoDelete(videoID, AccountDataManager.getAccount());
        System.out.println(ret);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleteFail"))
                        .open();
                return;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleteFail"))
                        .open();
                return;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleted"))
                        .open();
                break;
            default:
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleteFail"))
                        .open();
        }

        updateVideosAndInfo();
    }

    /**
     * This method updates the videos attribute by using methods to fetch
     * the data from the Server and parsing it.
     */
    public static void updateVideosAndInfo() {
        String videoString = getVideosFromServer();
        if (videoString != null) {
            videos = createVideoList(videoString);
            addInfoToVideoList();
        }
    }

    /**
     * This method fetches the videos from the Server via ServerProxy
     */
    private static String getVideosFromServer() {
        String ret = "";
        ret = ServerProxy.getVideosByAccount(AccountDataManager.getAccount());
        System.out.println(ret);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(errors.getString("getVideoFail"))
                        .open();
                break;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(errors.getString("getVideoFail"))
                        .open();
                break;
            default:
                return ret;
        }
        return null;
    }

    /**
     * This method parses the String and creates a list of Video Objects.
     *
     * @param videos the String which contains the videos
     */
    private static LinkedList createVideoList(String videos) {
        LinkedList<Video> videoList = new LinkedList();

        JSONArray jsonArray = new JSONArray(videos);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            int id = jsonObject.getInt("id");

            Video video = new Video(name, id, "");
            videoList.add(video);
        }

        return videoList;
    }

    /**
     * This method adds the meta-info to every video object in the list.
     */
    private static void addInfoToVideoList() {

        if (videos == null) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("infoFail"))
                    .open();
        }

        for (Video v : videos) {
            String info = getMetaInfFromServer(v.getId());
            v.setInfo(info);
        }
    }

    /**
     * This method sends a request to fetch a videos meta-info via the ServerProxy.
     *
     * @param videoID the id of the video to fetch the info from
     */
    private static String getMetaInfFromServer(int videoID) {
        String response = ServerProxy.videoInfo(videoID, AccountDataManager.getAccount());
        String ret;

        switch (response) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(errors.getString("infoFail"))
                        .open();
                ret = errors.getString("noMeta");
                break;
            case "FAILURE":
                ret = errors.getString("noMeta");
                break;
            default:
                ret = parseMetaJSON(response);
        }

        return ret;
    }

    public static LinkedList<Video> getVideos() {
        updateVideosAndInfo();
        return videos;
    }

    public static void removeVideos() {
        VideoDataManager.videos = null;
    }

    private static String parseMetaJSON(String ret) {
        JSONObject obj = new JSONObject(ret);
        String date;
        double gForceX;
        double gForceY;
        double gForceZ;
        String triggerType;

        try {
            date = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy").format(new Date(obj.getLong("date")));
            triggerType = obj.getString("triggerType");
            gForceX = obj.getDouble("triggerForceX");
            gForceY = obj.getDouble("triggerForceY");
            gForceZ = obj.getDouble("triggerForceZ");
        } catch (JSONException e) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Date: " + date + "\n");
        builder.append("Trigger type: " + triggerType + "\n");
        builder.append("G-Force (X): " + gForceX + "\n");
        builder.append("G-Force (Y): " + gForceY + "\n");
        builder.append("G-Force (Z): " + gForceZ);
        return builder.toString();
    }
}
