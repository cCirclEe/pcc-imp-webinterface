package edu.kit.informatik.pcc.webinterface.datamanagement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The attributes of an account combined in one class for simpler usability.
 *
 * @author Josh Romanowski, Christoph Hörtnagl
 */
public class Account {

    // JSON keys
    private static final String JSON_KEY_MAIL = "mail";
    private static final String JSON_KEY_PASSWORD = "password";

    //attributes
    private String mail;
    private String password;

    //constructors
    public Account(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    //getter/setter
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    /**
     * A getter that gives you the Account attributes in a json object.
     *
     * @return the json object
     */
    public String getAsJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_KEY_MAIL, this.mail);
            json.put(JSON_KEY_PASSWORD, this.password);
        } catch (JSONException e) {
            return "";
        }
        return json.toString();
    }
}
