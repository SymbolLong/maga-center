package com.maga.center.constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SystemConstants {

    public static Map<String, String> servers;

    static {
        servers = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("100000", "http://127.0.0.1:8080");
        JSONArray array = new JSONArray();
        array.add(object);
        loadServers();
        System.out.println(servers.toString());
    }

    public static void loadServers() {
        servers.put("100000", "http://127.0.0.1:8080");
    }

    public static void updateServer(String api, String path){
        servers.put(api, path);
    }


}
