package edu.kit.informatik.pcc.webinterface.datamanagement;

/**
 * Created by chris on 17.01.2017.
 * The attributes of an account combined in one class for simpler useabilty.
 */
public class Account {

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
}
