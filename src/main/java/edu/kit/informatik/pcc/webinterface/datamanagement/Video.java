package edu.kit.informatik.pcc.webinterface.datamanagement;

/**
 * Created by chris on 17.01.2017.
 * The attributes of a video combined in one class for simpler useabilty.
 */
public class Video {

    //attributes
    private String name;
    private int id;
    private String info;

    //constructors
    public Video(String name, int id) {
        this(name, id, "");
    }

    public Video(String name, int id, String info) {
        this.name = name;
        this.id = id;
        this.info = info;
    }

    //getter/setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
