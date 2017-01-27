package edu.kit.informatik.pcc.webinterface.datamanagement;

import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;

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

        ret = ServerProxy.videoDownload(videoID, AccountDataManager.getAccount());

        //TODO implement switch case for video download.
        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString(""))
                        .open();
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString(""))
                        .open();
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString(""))
                        .open();
            default:
                MessageBox.createInfo()
                        .withMessage(messages.getString(""))
                        .open();
        }
    }

    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID) {
        String ret = "";

        ret = ServerProxy.videoDelete(videoID,AccountDataManager.getAccount());

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleteFail"))
                        .open();
            case "FAILURE":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleteFail"))
                        .open();
            case "SUCCESS":
                MessageBox.createInfo()
                        .withMessage(messages.getString("videoDeleted"))
                        .open();
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
        ret = ServerProxy.getVideosbyAccount(AccountDataManager.getAccount());


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
        LinkedList videoList = new LinkedList();
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
            if (info != null) {
                v.setInfo(info);
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

        switch (ret) {
            case "WRONG ACCOUNT":
                MessageBox.createInfo()
                        .withMessage(messages.getString("nfoFail"))
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
        if (videos == null) {
            updateVideosAndInfo();
        }
        return videos;
    }

    public static void removeVideos() {
        VideoDataManager.videos = null;
    }

}
