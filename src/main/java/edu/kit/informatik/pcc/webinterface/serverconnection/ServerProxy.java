package edu.kit.informatik.pcc.webinterface.serverconnection;

import edu.kit.informatik.pcc.webinterface.datamanagement.Account;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 *
 * This class handles all communication with the Server.
 *
 * @author Josh Romanowski, Fabian Wenzel
 */
public class ServerProxy {

    public static final String HOST = "http://laubenstone.de:2222/";
    //public static final String HOST = "http://localhost:2222/";

    //status strings
    //TODO: Change to server connection failure -> handle
    private static final String FAILURE = "FAILURE";

    //param strings
    private static final String VIDEO_ID = "videoId";
    private static final String ACCOUNT = "account";
    private static final String NEW_ACCOUNT = "newAccount";
    private static final String UUID = "uuid";

    /**
     * Method does a Server Request to get the Videos from account a.
     * Then gives the answer back.
     *
     * @param a Account
     * @return Answer from Server.
     */
    public static String getVideos(Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);

        Response response = post(f, "getVideos");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * This class gets the Video information from the Server.
     *
     * @param videoID Video which info is wanted
     * @param a       Account to which the video is referred
     * @return Answer from the Server
     */
    public static String videoInfo(int videoID, Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);
        f.param(VIDEO_ID, Integer.toString(videoID));

        Response response = post(f, "videoInfo");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * This method gets the Video as File from the Server
     *
     * @param videoID Video which is wanted
     * @param a Account to which the video is referred
     * @return Answer from the Server
     */
    public static InputStream videoDownload(int videoID, Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);
        f.param(VIDEO_ID, Integer.toString(videoID));

        Response response = post(f, "videoDownload");
        if (response == null)
            return null;

        return response.readEntity(InputStream.class);
    }

    /**
     * This method deletes a Video from the Server.
     *
     * @param videoID Video which shall be deleted
     * @param a Account to which the video is referred
     * @return Answer from the Server
     */
    public static String videoDelete(int videoID, Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);
        f.param(VIDEO_ID, Integer.toString(videoID));

        Response response = post(f, "videoDelete");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * This method calls the Server to authenticate the given account.
     *
     * @param a Account
     * @return Answer from the Server.
     */
    public static String authenticateUser(Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);

        Response response = post(f, "authenticate");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * This method calls the Server to create an Account.
     *
     * @param a Account to create
     * @param id a unique id
     * @return Answer from the Server
     */
    public static String createAccount(Account a, UUID id) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);
        f.param(UUID, id.toString());

        Response response = post(f, "createAccount");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * This method calls the server to change account date from old to new.
     *
     * @param oldAccount Account to change
     * @param newAccount New Account Data
     * @return Answer form the server
     */
    public static String changeAccount(Account oldAccount, Account newAccount) {
        String json = oldAccount.getAsJson();
        String newJson = newAccount.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);
        f.param(NEW_ACCOUNT, newJson);

        Response response = post(f, "changeAccount");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }

    /**
     * If you dont know what this does, dont use it.
     *
     * @param a Account to delete
     * @return Answer from the server
     */
    public static String deleteAccount(Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(ACCOUNT, json);

        Response response = post(f, "deleteAccount");
        return (response == null) ? FAILURE : response.readEntity(String.class);
    }


    private static Response post(Form f, String path) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path(path);
        try {
            return webTarget.request().post(
                    Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
        } catch (ProcessingException e) {
            return null;
        }
    }
}
