package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.util.ApiResultBuilder;
import com.maga.center.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    public ApiResult handleRequest(JSONObject params) {
        //校验参数
        if (!params.containsKey("token")) {
            return ApiResultBuilder.failure("请传入管理员令牌");
        }
        int api = Integer.parseInt(params.getString("api"));
        String token = params.getString("token");
        ApiResult apiResult = null;
        //校验权限
        if (api < 10290) {
            String permission = "";
            if (api <= 100202) {
                permission = "OPT_ADMIN";
            } else if (api <= 100205) {
                permission = "VIEW_ADMIN";
            } else if (api <= 100206) {
                permission = "OPT_ITEM";
            } else if (api <= 100207) {
                permission = "VIEW_ITEM";
            } else if (api <= 100206) {
                permission = "OPT_ROLE";
            } else if (api <= 100207) {
                permission = "VIEW_ROLE";
            } else if (api <= 100206) {
                permission = "OPT_PERMISSION";
            } else if (api <= 100207) {
                permission = "VIEW_PERMISSION";
            }
            apiResult = checkPermission(token, permission);
            if (!apiResult.isSuccess()) {
                return apiResult;
            }
        }
        //逻辑处理
        switch (api) {
            case 100200:
                apiResult = handle100200(params);
                break;
            case 100201:
                apiResult = handle100201(params);
                break;
            case 100202:
                apiResult = handle100202(params);
                break;
            case 100203:
                apiResult = handle100203(params);
                break;
            case 100204:
                apiResult = handle100204(params);
                break;
            case 100205:
                apiResult = handle100205(params);
                break;
            case 100297:
                apiResult = handle100297(params);
                break;
            case 100298:
                apiResult = handle100298(params);
                break;
            default:
                break;
        }
        return apiResult;
    }


    public ApiResult handle100200(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100201(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100202(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100203(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100204(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100205(JSONObject params) {
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100297(JSONObject params) {
        if (!params.containsKey("loginName") || StringUtils.isEmpty(params.getString("loginName"))) {
            return ApiResultBuilder.failure("登录名不能为空");
        }
        if (!params.containsKey("password") || StringUtils.isEmpty(params.getString("password"))) {
            return ApiResultBuilder.failure("密码不能为空");
        }
        String loginName = params.getString("loginName");
        String password = params.getString("password");
        String accessKey = params.getString("accessKey");
        String ip = params.getString("ip");
        String url = SystemConstants.servers.get("100297") + "?loginName=" + loginName + "&password=" + password + "&accessKey=" + accessKey + "&ip=" + ip;
        return HttpUtil.doGet(url);
    }

    public ApiResult handle100298(JSONObject params) {
        if (!params.containsKey("token") || StringUtils.isEmpty(params.getString("token"))) {
            return ApiResultBuilder.failure("令牌不能为空");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", params.getString("token"));
        String accessKey = params.getString("accessKey");
        String url = SystemConstants.servers.get("100298") + "?accessKey=" + accessKey;
        return HttpUtil.doGet(url, header);
    }

    public ApiResult checkPermission(String token, String permission) {
        Map<String, String> header = new HashMap<>();
        header.put("token", token);
        String url = SystemConstants.servers.get("100299") + "/checkPermission?permission=" + permission;
        return HttpUtil.doGet(url, header);
    }
}
