package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.util.HttpUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public ApiResult handleRequest(JSONObject params) {
        ApiResult apiResult = null;
        String api = params.getString("api");
        return apiResult;
    }

    public ApiResult checkPermission(String token, String permission) {
        String url = SystemConstants.servers.get("100299") + "/checkPermission?token=" + token + "&permission=" + permission;
        return HttpUtil.doGet(url);
    }
}
