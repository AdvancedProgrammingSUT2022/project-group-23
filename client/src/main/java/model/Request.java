package model;

import java.util.HashMap;

public class Request {
    private String type;
    private HashMap<String, String> info;

    public Request(String type) {
        this.type = type;
        this.info = new HashMap<>();
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }
}
