package com.maga.center.service;

import com.maga.center.entity.ApiResult;
import com.maga.center.entity.Log;
import com.maga.center.repository.LogRepository;
import com.maga.center.util.ApiResultBuilder;
import com.maga.center.util.SignUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ApiService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LogRepository logRepository;

    public ApiResult handleRequest(HttpServletRequest resquest, JSONObject params) {
        String api = params.getString("api");
        String accessKey = params.getString("accessKey");
        ApiResult apiResult = new ApiResult();
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

    public boolean checkSign(JSONObject params) {
        JSONObject json = new JSONObject();
        json.put("api", "100106");
        json.put("accessKey", params.getString("accessKey"));
        ApiResult result = customerService.handleRequest(json);
        if (!result.isSuccess()) {
            return false;
        }
        return SignUtil.checkSign(params, result.getData().getString("accessSecret"));
    }
}
