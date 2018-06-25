package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.util.ApiResultBuilder;
import com.maga.center.util.CheckUtil;
import com.maga.center.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    public ApiResult handleRequest(JSONObject params) {

        int api = Integer.parseInt(params.getString("api"));
        ApiResult apiResult = null;

        if (api < 100290) {
            //校验参数
            if (!params.containsKey("token")) {
                return ApiResultBuilder.failure("请传入管理员令牌");
            }
            String token = params.getString("token");
            //校验权限
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
            case 100299:
                apiResult = handle100299(params);
                break;
            default:
                break;
        }
        return apiResult;
    }

    public ApiResult checkPermission(String token, String permission){
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("permission", permission);
        return handle100299(params);
    }

    private ApiResult handle100200(JSONObject params) {
        // 参数是否完整（非空，格式）
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("昵称不能为空");
        }
        if (!params.containsKey("avatar") || StringUtils.isEmpty(params.getString("avatar"))) {
            return ApiResultBuilder.failure("头像地址不能为空");
        }
        if (!params.containsKey("loginName") || StringUtils.isEmpty(params.getString("loginName"))) {
            return ApiResultBuilder.failure("登录名称不能为空");
        }
        if (!params.containsKey("password") || CheckUtil.isValidatePassword(params.getString("password"))) {
            return ApiResultBuilder.failure("密码格式错误");
        }
        //请求server, 返回数据
        String url = SystemConstants.servers.get("100200");
        return HttpUtil.doPostJson(url, params.toString());
    }

    private ApiResult handle100201(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("id不能为空");
        }
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("昵称不能为空");
        }
        if (!params.containsKey("avatar") || StringUtils.isEmpty(params.getString("avatar"))) {
            return ApiResultBuilder.failure("头像地址不能为空");
        }
        if (!params.containsKey("loginName") || StringUtils.isEmpty(params.getString("loginName"))) {
            return ApiResultBuilder.failure("登录名称不能为空");
        }
        if (!params.containsKey("password") || CheckUtil.isValidatePassword(params.getString("password"))) {
            return ApiResultBuilder.failure("密码格式错误");
        }
        String url = SystemConstants.servers.get("100201") + "/" + params.getLong("id");
        return HttpUtil.doPostJson(url, params.toString());
    }

    private ApiResult handle100202(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("id不能为空");
        }
        String url = SystemConstants.servers.get("100202") + "/" + params.getLong("id");
        return HttpUtil.doDelete(url);
    }

    private ApiResult handle100203(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("id不能为空");
        }
        String url = SystemConstants.servers.get("100203") + "/" + params.getLong("id");
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100204(JSONObject params) {
        String page = SystemConstants.DEFAULT_PAGE;
        String size = SystemConstants.DEFAULT_PAGE_SIZE;
        if (params.containsKey("page")) {
            page = params.getString("page");
        }
        if (params.containsKey("size")) {
            size = params.getString("size");
        }
        String url = SystemConstants.servers.get("100204") + "?page=" + page + "&size=" + size;
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100205(JSONObject params) {
        String page = SystemConstants.DEFAULT_PAGE;
        String size = SystemConstants.DEFAULT_PAGE_SIZE;
        String name = "";
        if (params.containsKey("page")) {
            page = params.getString("page");
        }
        if (params.containsKey("size")) {
            size = params.getString("size");
        }
        if (params.containsKey("name")) {
            name = params.getString("name");
        }
        String url = SystemConstants.servers.get("100205") + "?name=" + name + "&page=" + page + "&size=" + size;
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100297(JSONObject params) {
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

    private ApiResult handle100298(JSONObject params) {
        if (!params.containsKey("token") || StringUtils.isEmpty(params.getString("token"))) {
            return ApiResultBuilder.failure("令牌不能为空");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", params.getString("token"));
        String accessKey = params.getString("accessKey");
        String url = SystemConstants.servers.get("100298") + "?accessKey=" + accessKey;
        return HttpUtil.doGet(url, header);
    }

    private ApiResult handle100299(JSONObject params) {
        Map<String, String> header = new HashMap<>();
        header.put("token", params.getString("token"));
        String url = SystemConstants.servers.get("100299") + "/checkPermission?permission=" + params.getString("permission");
        return HttpUtil.doGet(url, header);
    }
}
