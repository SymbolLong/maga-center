package com.maga.center.controller;

import com.maga.center.constants.SystemConstants;
import com.maga.center.entity.ApiResult;
import com.maga.center.entity.Log;
import com.maga.center.repository.LogRepository;
import com.maga.center.service.AdminService;
import com.maga.center.service.ConfigService;
import com.maga.center.service.CustomerService;
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
    private LogRepository logRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ConfigService configService;


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
        if (api.length() != 6 || accessKey.length() != 32 || sign.length() != 32){
            return ApiResultBuilder.failure("基本参数有误！");
        }
        String server = SystemConstants.servers.get(api);
        if (StringUtils.isEmpty(server)){
            return ApiResultBuilder.failure("服务器繁忙，请稍后重试！");
        }
        //签名校验
        if(!customerService.checkSign(params)){
            return ApiResultBuilder.failure("签名有误，请检查签名！");
        }
        //处理业务逻辑
        ApiResult apiResult = null;
        Long start = System.currentTimeMillis();
        String apiGroup = api.substring(0, 4);
        switch (apiGroup) {
            case "1001":
                apiResult = customerService.handleRequest(params);
                break;
            case "1002":
                if (api.equals("100297")){
                    params.put("ip", resquest.getRemoteHost());
                }
                apiResult = adminService.handleRequest(params);
                break;
            case "1003":
                apiResult = configService.handleRequest(params);
                break;
            default:
                break;
        }
        //记录日志并返回结果
        Log log = new Log();
        log.setApi(api);
        log.setAccessKey(accessKey);
        log.setTime((System.currentTimeMillis() - start) + "毫秒");
        if (apiResult == null) {
            logRepository.save(log);
            return ApiResultBuilder.failure("请求api不存在");
        }
        log.setResult(apiResult.toString());
        logRepository.save(log);
        return apiResult;
    }

}
