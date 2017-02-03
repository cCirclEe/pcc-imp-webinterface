package edu.kit.informatik.pcc.webinterface.serverconnection;

import edu.kit.informatik.pcc.webinterface.datamanagement.Account;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 *
 * This class handles all comunication with the Server.
 */
public class ServerProxy {

    private static final String HOST = "http://laubenstone.de:2222/";
    private static final String PARAM = "account";

    /**
     * Method does a Server Request to get the Videos from account a.
     * Then gives the answer back.
     *
     * @param a Account
     * @return Answer from Server.
     */
    public static String getVideosByAccount(Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(PARAM, json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("getVideosByAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
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
        f.param(PARAM, json);
        f.param("videoId", Integer.toString(videoID));
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoInfo");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    /**
     * This method gets the Video as File from the Server
     *
     * @param videoID Video which is wanted
     * @param a Account to which the video is referred
     * @return Answer from the Server
     */
    public static File videoDownload(int videoID, Account a) {
        String json = a.getAsJson();
        Form f = new Form();
        f.param(PARAM, json);
        f.param("videoId", Integer.toString(videoID));
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoDownload");
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        InputStream inputStream = response.readEntity(InputStream.class);
        File downloadfile = null;
        if (response.getStatus() == 200) {
            //TODO: more user downloads
            downloadfile = new File("C://Users/chris/Desktop/PSE/pcc-imp-webinterface/temp/test1");
            try {
                Files.copy(inputStream, downloadfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return downloadfile;
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
        f.param(PARAM, json);
        String string = Integer.toString(videoID);
        System.out.println(string);
        f.param("id", string);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoDelete");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
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
        f.param(PARAM, json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("authenticate");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
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
        f.param(PARAM, json);
        f.param("uuid", id.toString());
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("createAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    /**
     * This method calls the server to change account date from old to new.
     *
     * @param oldAccount
     * @param newAccount
     * @return Answer form the server
     */
    public static String changeAccount(Account oldAccount, Account newAccount) {
        String json = oldAccount.getAsJson();
        String newJson = newAccount.getAsJson();
        Form f = new Form();
        f.param(PARAM, json);
        f.param("newAccount", newJson);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("changeAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
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
        f.param(PARAM, json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("deleteAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

}
