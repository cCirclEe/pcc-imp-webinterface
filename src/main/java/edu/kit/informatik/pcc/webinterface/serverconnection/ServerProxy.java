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
 */
public class ServerProxy {

    private static final String HOST = "http://laubenstone.de:2222/";

    public static String getVideosByAccount(Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("getVideosByAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String videoInfo(int videoID, Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        f.param("id", Integer.toString(videoID));
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoInfo");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static File videoDownload(int videoID, Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        f.param("videoId", Integer.toString(videoID));
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoDownload");
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE),Response.class);
        InputStream inputStream = response.readEntity(InputStream.class);
        File downloadfile = null;
        if (response.getStatus() == 200) {
            //TODO: more user downloads
            downloadfile = new File("C://Users/chris/Desktop/PSE/pcc-imp-webinterface/temp/test");
            try {
                Files.copy(inputStream, downloadfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return downloadfile;
    }

    public static String videoDelete(int videoID, Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        String string = Integer.toString(videoID);
        System.out.println(string);
        f.param("id", string);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("videoDelete");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        System.out.println(a.getMail());
        System.out.println(a.getPassword());

        return response.readEntity(String.class);
    }

    public static String authenticateUser(Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("authenticate");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String createAccount(Account a, UUID id) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        f.param("uuid", id.toString());
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("createAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String changeAccount(Account oldAccount, Account newAccount) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ oldAccount.getMail() +"\",\n" +
                "    \"password\": \""+ oldAccount.getPassword() +"\",\n" +
                "  }\n" +
                "}";

        String newJson = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ newAccount.getMail() +"\",\n" +
                "    \"password\": \""+ newAccount.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        f.param("newData", newJson);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("changeAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String deleteAccount(Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(HOST).path("webservice").path("delteAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

}
