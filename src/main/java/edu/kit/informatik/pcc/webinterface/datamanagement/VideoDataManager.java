package edu.kit.informatik.pcc.webinterface.datamanagement;

import java.util.LinkedList;

/**
 * Created by chris on 17.01.2017.
 * This class handles all operations with video data.
 */
public class VideoDataManager {

    //attributes
    public static LinkedList videos;

    //methods

    /**
     * This method sends a request to download a video via the ServerProxy.
     *
     * @param videoID the id of the video to download
     */
    public static void downloadVideo(int videoID) {

    }

    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID) {

    }

    /**
     * This method updates the videos attribute by using methods to fetch
     * the data from the Server and parsing it.
     */
    public static void updateVideosAndInof() {

    }

    /**
     * This method fetches the videos from the Server via ServerProxy
     */
    private static String getVideosFromServer() {
        return "";
    }

    /**
     * This method parses the String and creates a list of Video Objects.
     *
     * @param videos the String which contains the videos
     */
    private static LinkedList createVideoList(String videos) {
        LinkedList videoList = new LinkedList();
        return videoList;
    }

    /**
     * This method adds the meta-info to every video object in the list.
     *
     * @param videos a list containing video objects
     */
    private static LinkedList addInfoToVideoList(LinkedList videos) {
        return videos;
    }

    /**
     * This method sends a request to fetch a videos meta-info via the ServerProxy.
     *
     * @param videoID the id of the video to fetch the info from
     */
    private static String getMetaInfFromServer(int videoID) {
        return "";
    }

}
