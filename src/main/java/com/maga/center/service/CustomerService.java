package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.util.ApiResultBuilder;
import com.maga.center.util.CheckUtil;
import com.maga.center.util.HttpUtil;
import com.maga.center.util.SignUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private static Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private AdminService adminService;

    public ApiResult handleRequest(JSONObject params) {
        //校验参数
        if (!params.containsKey("token")) {
            return ApiResultBuilder.failure("请传入管理员令牌");
        }
        int api = Integer.parseInt(params.getString("api"));
        String token = params.getString("token");
        String permission = "OPT_CUSTOMER";
        //校验权限
        if (api > 100104) {
            permission = "VIEW_CUSTOMER";
        }
        ApiResult apiResult = adminService.checkPermission(token, permission);
        if (!apiResult.isSuccess()) {
            return apiResult;
        }
        //逻辑处理
        switch (api) {
            case 100100:
                apiResult = handle100100(params);
                break;
            case 100101:
                apiResult = handle100101(params);
                break;
            case 100102:
                apiResult = handle100102(params);
                break;
            case 100103:
                apiResult = handle100103(params);
                break;
            case 100104:
                apiResult = handle100104(params);
                break;
            case 100105:
                apiResult = handle100105(params);
                break;
            case 100106:
                apiResult = handle100106(params);
                break;
            case 100107:
                apiResult = handle100107(params);
                break;
            default:
                break;
        }
        return apiResult;
    }

    private ApiResult handle100100(JSONObject params) {
        // 参数是否完整（非空，格式）
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("名称不能为空");
        }
        if (!params.containsKey("mobile") || CheckUtil.isMobileNO(params.getString("mobile"))) {
            return ApiResultBuilder.failure("手机号格式错误");
        }
        String name = params.getString("name");
        String mobile = params.getString("mobile");
        String remark = params.getString("remark");
        //请求server, 返回数据
        String url = SystemConstants.servers.get("100100") + "?name=" + name + "&mobile=" + mobile + "&remark=" + remark;
        return HttpUtil.doPost(url);
    }

    private ApiResult handle100101(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        if (!params.containsKey("name") || StringUtils.isEmpty(params.getString("name"))) {
            return ApiResultBuilder.failure("名称不能为空");
        }
        if (!params.containsKey("mobile") || CheckUtil.isMobileNO(params.getString("mobile"))) {
            return ApiResultBuilder.failure("手机号格式错误");
        }
        String id = params.getString("id");
        String name = params.getString("name");
        String mobile = params.getString("mobile");
        String remark = params.getString("remark");
        String url = SystemConstants.servers.get("100101") + "/" + id + "?name=" + name + "&mobile=" + mobile + "&remark=" + remark;
        return HttpUtil.doPut(url);
    }

    private ApiResult handle100102(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        String url = SystemConstants.servers.get("100102") + "/" + params.getString("id");
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100103(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        String url = SystemConstants.servers.get("100103") + "/" + params.getString("id");
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100104(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        String url = SystemConstants.servers.get("100104") + "/" + params.getString("id");
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100105(JSONObject params) {
        if (!params.containsKey("id") || StringUtils.isEmpty(params.getString("id"))) {
            return ApiResultBuilder.failure("ID不能为空");
        }
        String url = SystemConstants.servers.get("100105") + "/" + params.getString("id");
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100106(JSONObject params) {
        String accessKey = params.getString("accessKey");
        String url = SystemConstants.servers.get("100106") + "?accessKey=" + accessKey;
        return HttpUtil.doGet(url);
    }

    private ApiResult handle100107(JSONObject params) {
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
        String url = SystemConstants.servers.get("100107") + "?name=" + name + "&page=" + page + "&size=" + size;
        return HttpUtil.doGet(url);
    }

}
