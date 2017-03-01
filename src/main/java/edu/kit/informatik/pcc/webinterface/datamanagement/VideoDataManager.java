package edu.kit.informatik.pcc.webinterface.datamanagement;

import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import de.steinwedel.messagebox.MessageBox;
import edu.kit.informatik.pcc.webinterface.gui.MyUI;
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
    private static ResourceBundle errors = ResourceBundle.getBundle("ErrorMessages");

    /**
     * Creates a file proxy that will provide the video file to download upon request.
     * Therefore it loads the file from the server.
     *
     * @param videoID Video id of the file to download.
     * @param videoName Video name of the file to download.
     * @return Returns a StreamResources used for the downloader.
     */
    public static StreamResource createDownloadFileProxy(int videoID, String videoName, VaadinSession session) {
        return new StreamResource((StreamResource.StreamSource) () -> {
            Account account = (Account) session.getAttribute(MyUI.SESSION_KEY_ACCOUNT);

            InputStream stream = ServerProxy.videoDownload(videoID, account);

            if (stream == null) {
                MessageBox.createInfo()
                        .withMessage(errors.getString("videoDownloadFail"))
                        .open();
                return null;
            }
            return stream;
        }, videoName + Video.EXTENSION);
    }

    /**
     * This method sends a request to delete a video via the ServerProxy.
     *
     * @param videoID the id of the video to delete
     */
    public static void deleteVideo(int videoID, VaadinSession session) {
        Account account = (Account) session.getAttribute(MyUI.SESSION_KEY_ACCOUNT);

        String ret = ServerProxy.videoDelete(videoID, account);

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
        }
    }

    /**
     * This method updates the videos attribute by using methods to fetch
     * the data from the Server and parsing it.
     */
    public static LinkedList<Video> getVideos(VaadinSession session) {
        String videoString = getVideosFromServer(session);
        if (videoString != null) {
            LinkedList<Video> videos = createVideoList(videoString);
            return addInfoToVideoList(videos, session);
        }
        return null;
    }

    // Helper Methods

    /**
     * This method fetches the videos from the Server via ServerProxy
     *
     * @return the videos as json string
     */
    private static String getVideosFromServer(VaadinSession session) {
        Account account = (Account) session.getAttribute(MyUI.SESSION_KEY_ACCOUNT);
        String ret = ServerProxy.getVideos(account);

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
    private static LinkedList<Video> addInfoToVideoList(LinkedList<Video> videos, VaadinSession session) {
        LinkedList<Video> ret = new LinkedList<>();

        if (videos == null) {
            MessageBox.createInfo()
                    .withMessage(errors.getString("infoFail"))
                    .open();
            return null;
        }

        for (Video v : videos) {
            String info = getMetaInfFromServer(v.getId(), session);
            v.setInfo(info);
            ret.add(v);
        }
        return ret;
    }

    /**
     * This method sends a request to fetch a videos meta-info via the ServerProxy.
     *
     * @param videoID the id of the video to fetch the info from
     * @return the metainfo as string
     */
    private static String getMetaInfFromServer(int videoID, VaadinSession session) {
        Account account = (Account) session.getAttribute(MyUI.SESSION_KEY_ACCOUNT);
        String response = ServerProxy.videoInfo(videoID, account);

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
        String date;
        double gForceX;
        double gForceY;
        double gForceZ;
        String triggerType;
        String editingDate = null;

        try {
            JSONObject obj = new JSONObject(ret);
            date = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy")
                    .format(new Date(obj.getLong("date")));
            triggerType = obj.getString("triggerType");
            gForceX = obj.getDouble("triggerForceX");
            gForceY = obj.getDouble("triggerForceY");
            gForceZ = obj.getDouble("triggerForceZ");

            if (obj.has("editingDate")) {
                editingDate = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy")
                        .format(new Date(obj.getLong("editingDate")));
            }
        } catch (JSONException e) {
            return errors.getString("metadataFail");
        }

        final StringBuilder builder = new StringBuilder();
        builder.append("Date: ").append(date).append("\n");
        builder.append("Trigger type: ").append(triggerType).append("\n");
        builder.append("G-Force (X): ").append(gForceX).append("\n");
        builder.append("G-Force (Y): ").append(gForceY).append("\n");
        builder.append("G-Force (Z): ").append(gForceZ);
        if (editingDate != null) {
            builder.append("\n").append("Editing Date: ").append(editingDate);
        }

        return builder.toString();
    }

}
