package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.util.ApiResultBuilder;
import com.maga.center.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {

    private String secret = "zhangshenglong";

    @Autowired
    private AdminService adminService;

    public ApiResult handleRequest(JSONObject params) {
        //校验参数
        if (!params.containsKey("token")) {
            return ApiResultBuilder.failure("请传入管理员令牌");
        }
        int api = Integer.parseInt(params.getString("api"));
        String token = params.getString("token");
        ApiResult apiResult = null;
        //校验权限
        String permission = "";
        if (api <= 100202) {
            permission = "OPT_SERVER";
        } else {
            permission = "VIEW_SERVER";
        }
        apiResult = adminService.checkPermission(token, permission);
        if (!apiResult.isSuccess()) {
            return apiResult;
        }
        //逻辑处理
        switch (api) {
            case 100300:
                apiResult = handle100300(params);
                break;
            case 100301:
                apiResult = handle100301(params);
                break;
            case 100302:
                apiResult = handle100302(params);
                break;
            case 100303:
                apiResult = handle100303(params);
                break;
            case 100304:
                apiResult = handle100304(params);
                break;
            default:
                break;
        }
        return apiResult;
    }


    private ApiResult handle100300(JSONObject params) {
        //校验参数
        if (!params.containsKey("api") || StringUtils.isEmpty(params.getString("api"))) {
            return ApiResultBuilder.failure("API不能为空");
        }
        if (!params.containsKey("path") || StringUtils.isEmpty(params.getString("path"))) {
            return ApiResultBuilder.failure("路径不能为空");
        }
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("名称不能为空");
        }
        if (!params.containsKey("remark")){
            params.put("remark", "");
        }
        //发送请求
        Map<String, String> header = new HashMap<>();
        header.put("token", secret);
        String url = SystemConstants.servers.get("100300");
        return HttpUtil.doPostJson(url, params.toString(), header);
    }

    private ApiResult handle100301(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        if (!params.containsKey("api") || StringUtils.isEmpty(params.getString("api"))) {
            return ApiResultBuilder.failure("API不能为空");
        }
        if (!params.containsKey("path") || StringUtils.isEmpty(params.getString("path"))) {
            return ApiResultBuilder.failure("路径不能为空");
        }
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("名称不能为空");
        }
        if (!params.containsKey("remark")){
            params.put("remark", "");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", secret);
        String url = SystemConstants.servers.get("100301")+"/"+params.getLong("id");
        return HttpUtil.doPutJSON(url, params.toString(), header);
    }

    private ApiResult handle100302(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", secret);
        String url = SystemConstants.servers.get("100302")+"/"+params.getLong("id");
        return HttpUtil.doDelete(url, header);
    }

    private ApiResult handle100303(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", secret);
        String url = SystemConstants.servers.get("100303")+"/"+params.getLong("id");
        return HttpUtil.doGet(url, header);
    }

    private ApiResult handle100304(JSONObject params) {
        String page = SystemConstants.DEFAULT_PAGE;
        String size = SystemConstants.DEFAULT_PAGE_SIZE;
        String api = "";
        if (params.containsKey("page")) {
            page = params.getString("page");
        }
        if (params.containsKey("size")) {
            size = params.getString("size");
        }
        if (params.containsKey("api")) {
            api = params.getString("api");
        }
        Map<String, String> header = new HashMap<>();
        header.put("token", secret);
        String url = SystemConstants.servers.get("100304") + "?api=" + api + "&page=" + page + "&size=" + size;
        return HttpUtil.doGet(url, header);
    }


}
