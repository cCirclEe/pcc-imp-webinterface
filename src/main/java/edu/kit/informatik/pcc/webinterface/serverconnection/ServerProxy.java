package edu.kit.informatik.pcc.webinterface.serverconnection;

import edu.kit.informatik.pcc.webinterface.datamanagement.Account;
import elemental.json.Json;
import elemental.json.impl.JsonUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by chris on 17.01.2017.
 */
public class ServerProxy {

    public static String getVideosbyAccount(Account a) {
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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("getVideosByAccount");
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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("videoInfo");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String videoDownload(int videoID, Account a) {
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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("videoDownload");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
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
        f.param("id", Integer.toString(videoID));
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("videoDelete");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("authenticate");
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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("createAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

    public static String changeAccount(Account oldAccount, Account newAccount) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ oldAccount.getMail() +"\",\n" +
                "    \"password\": \""+ newAccount.getPassword() +"\",\n" +
                "  }\n" +
                "}";

        String newJson = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ oldAccount.getMail() +"\",\n" +
                "    \"password\": \""+ newAccount.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", json);
        f.param("newData", newJson);
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("changeAccount");
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
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("delteAccount");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return response.readEntity(String.class);
    }

}
