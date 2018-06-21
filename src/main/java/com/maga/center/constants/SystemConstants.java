package com.maga.center.constants;

import com.maga.center.entity.ApiResult;
import com.maga.center.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SystemConstants {


    public static String DEFAULT_PAGE = "1";
    public static String DEFAULT_PAGE_SIZE = "10";

    public static String CONFIG_SERVER = "http://127.0.0.1:8003/server/list?page=1&size=1000";

    public static Map<String, String> servers;

    static {
        servers = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("token", "zhangshenglong");
        ApiResult apiResult = HttpUtil.doGet(CONFIG_SERVER, header);
        JSONObject jsonObject = apiResult.getData();
        JSONArray array = jsonObject.getJSONObject("data").getJSONArray("servers");
        for (int i = 0; i < array.size(); i++) {
            JSONObject server = array.getJSONObject(i);
            servers.put(server.getString("api"), server.getString("path"));
        }
    }

    public static void updateServer(String api, String path) {
        servers.put(api, path);
    }

    public static void remove(String api) {
        if (servers.containsKey(api)) {
            servers.remove(api);
        }
    }


}
