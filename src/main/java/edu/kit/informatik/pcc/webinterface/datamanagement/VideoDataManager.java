package edu.kit.informatik.pcc.webinterface.datamanagement;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by chris on 17.01.2017.
 * This class handles all operations with video data.
 *
 * @author chris
 */
public class VideoDataManager {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String WRONGACCOUNT = "WRONG ACCOUNT";
    //attributes
    private static LinkedList<Video> videos = null;
    private static ResourceBundle errors = ResourceBundle.getBundle("ErrorMessages");

    /**
     * This method sends a request to download a video via the ServerProxy.
     * Then creates a filedownloader and gives it back.
     *
     * @param videoID the id of the video to download
     * @return filedownloader
     */
    public static FileDownloader downloadVideo(int videoID) {
        InputStream stream = ServerProxy.videoDownload(videoID, AccountDataManager.getAccount());

        if (stream == null) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("videoDownloadFail"))
                    .open();
            return null;
        }

        StreamResource resource = new StreamResource(
                (StreamResource.StreamSource) () -> stream, "nameHere.avi");
        return new FileDownloader(resource);
    }


    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID) {
        String ret = ServerProxy.videoDelete(videoID, AccountDataManager.getAccount());

        switch (ret) {
            case SUCCESS:
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleted"))
                        .open();
                break;
            case WRONGACCOUNT:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongAccount"))
                        .open();
                break;
            case FAILURE:
            default:
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDeleteFail"))
                        .open();
                return;
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
     * This method gives the videos in form of a list
     *
     * @return LinkedList of Videos
     */
    public static LinkedList<Video> getVideos() {
        updateVideosAndInfo();
        return videos;
    }

    public static void removeVideos() {
        VideoDataManager.videos = null;
    }

    // Helper Methods

    /**
     * This method fetches the videos from the Server via ServerProxy
     *
     * @return the videos as json string
     */
    private static String getVideosFromServer() {
        String ret = ServerProxy.getVideos(AccountDataManager.getAccount());

        switch (ret) {
            case WRONGACCOUNT:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongAccount"))
                        .open();
                break;
            case FAILURE:
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
    private static LinkedList<Video> createVideoList(String videos) {
        LinkedList<Video> videoList = new LinkedList<>();

        JSONArray jsonArray = new JSONArray(videos);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            int id = jsonObject.getInt("id");

            Video video = new Video(name, id);
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
     * @return the metainfo as string
     */
    private static String getMetaInfFromServer(int videoID) {
        String response = ServerProxy.videoInfo(videoID, AccountDataManager.getAccount());
        String ret;

        switch (response) {
            case WRONGACCOUNT:
                MessageBox.createInfo()
                        .withMessage(errors.getString("wrongAccount"))
                        .open();
                ret = errors.getString("wrongAccount");
                break;
            case FAILURE:
                MessageBox.createInfo()
                        .withMessage(errors.getString("noMeta"))
                        .open();
                ret = errors.getString("noMeta");
                break;
            default:
                ret = parseMetaJSON(response);
        }

        return ret;
    }

    /**
     * This method parses the meta object to a String
     * @param ret meta info as json
     * @return the created string
     */
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

        final StringBuilder builder = new StringBuilder();
        builder.append("Date: ").append(date).append("\n");
        builder.append("Trigger type: ").append(triggerType).append("\n");
        builder.append("G-Force (X): ").append(gForceX).append("\n");
        builder.append("G-Force (Y): ").append(gForceY).append("\n");
        builder.append("G-Force (Z): ").append(gForceZ);
        return builder.toString();
    }
}
