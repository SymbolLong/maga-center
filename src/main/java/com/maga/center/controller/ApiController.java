package com.maga.center.controller;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.service.ApiService;
import com.maga.center.util.ApiResultBuilder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @PostMapping
    public ApiResult handleRequest(HttpServletRequest resquest, @RequestBody JSONObject params) {
        //基本参数校验
        if (!params.containsKey("api") || !params.containsKey("accessKey") || !params.containsKey("sign")) {
            return ApiResultBuilder.failure("请传入基本参数！");
        }
        String api = params.getString("api");
        String accessKey = params.getString("accessKey");
        String sign = params.getString("sign");
        if (StringUtils.isEmpty(api) || StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(sign)) {
            return ApiResultBuilder.failure("请传入基本参数！");
        }
        if (api.length() != 6 || accessKey.length() != 32 || sign.length() != 32) {
            return ApiResultBuilder.failure("基本参数有误！");
        }
        String server = SystemConstants.servers.get(api);
        if (StringUtils.isEmpty(server)) {
            return ApiResultBuilder.failure("服务器繁忙，请稍后重试！");
        }
        //签名校验
        if (!apiService.checkSign(params)) {
            return ApiResultBuilder.failure("签名有误，请检查签名！");
        }
        //处理业务逻辑
        return apiService.handleRequest(resquest, params);
    }

}
