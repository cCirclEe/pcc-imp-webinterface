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

/**
 * Created by chris on 17.01.2017.
 */
public class ServerProxy {


    public String getVideosbyAccount(Account a) {
        //language=JSON
        String json = "{\n" +
                "  \"account\": {\n" +
                "    \"mail\": \""+ a.getMail() +"\",\n" +
                "    \"password\": \""+ a.getPassword() +"\",\n" +
                "  }\n" +
                "}";
        Form f = new Form();
        f.param("data", "sdf");
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:2222/").path("webservice").path("videoInfo");
        System.out.println(webTarget.getUri());
        Response response = webTarget.request().post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        return null;
    }
}
