package edu.kit.informatik.pcc.webinterface.datamanagement;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 * This class handles all operations with video data.
 */
public class VideoDataManager {

    //attributes
    private static LinkedList<Video> videos = null;
    private static ResourceBundle messages = ResourceBundle.getBundle("ErrorMessages");

    //methods

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
                    .withMessage(messages.getString("videoDownloadFail"))
                    .open();
            return;
        }

        //TODO: implement download !!!!
        FileResource resource = new FileResource(file);

        FileDownloader fileDownloader = new FileDownloader(resource);
        Button button = new Button();
        fileDownloader.extend(button);
        button.click();
    }

    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID) {
        String ret = "";

        ret = ServerProxy.videoDelete(videoID,AccountDataManager.getAccount());
        System.out.println(ret);

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleteFail"))
                        .open();
                return;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleteFail"))
                        .open();
                return;
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleted"))
                        .open();
                break;
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleteFail"))
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
                        .withMessage(messages.getString("getVideoFail"))
                        .open();
                break;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("getVideoFail"))
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
     *
     */
    private static LinkedList createVideoList(String videos) {
        LinkedList<Video> videoList = new LinkedList();

        JSONArray jsonArray = new JSONArray(videos);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject videoInfo = jsonObject.getJSONObject("videoInfo");
            String name = videoInfo.getString("name");
            int id = Integer.parseInt(videoInfo.getString("id"));

            Video video = new Video(name, id, "");
            videoList.add(video);
        }

        return videoList;
    }

    /**
     * This method adds the meta-info to every video object in the list.
     *
     */
    private static void addInfoToVideoList() {

        if (videos == null) {
            MessageBox.createInfo()
                    .withMessage(messages.getString("infoFail"))
                    .open();
        }

        for (Video v: videos) {
            String info = getMetaInfFromServer(v.getId());
            System.out.println(info);
            if (info != null) {
                v.setInfo(info);
            } else {
                v.setInfo("asdf");
            }
        }
    }

    /**
     * This method sends a request to fetch a videos meta-info via the ServerProxy.
     *
     * @param videoID the id of the video to fetch the info from
     */
    private static String getMetaInfFromServer(int videoID) {
        String ret = "";

        ret = ServerProxy.videoInfo(videoID, AccountDataManager.getAccount());
        System.out.println(ret);
        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("infoFail"))
                        .open();
                break;
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("infoFail"))
                        .open();
                break;
            default:
                return ret;
        }

        return null;
    }


    public static LinkedList getVideos() {
        updateVideosAndInfo();
        return videos;
    }

    public static void removeVideos() {
        VideoDataManager.videos = null;
    }

}
