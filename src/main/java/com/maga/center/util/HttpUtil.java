package com.maga.center.util;

import com.maga.center.entity.ApiResult;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static String DEFAULT_CHARSET = "UTF-8";


    public static ApiResult doGet(String url) {
        return doGet(url, 20000);
    }

    public static ApiResult doGet(String url, String charset) {
        return doGet(url, 3000, 20000, charset);
    }

    public static ApiResult doGet(String url, Map<String, String> header) {
        return doGet(url, 3000, 20000, null, header);
    }

    public static ApiResult doGet(String url, int readTimeout) {
        return doGet(url, 3000, readTimeout);
    }

    public static ApiResult doGet(String url, int connTimeout, int readTimeout) {
        return doGet(url, connTimeout, readTimeout, null);
    }

    public static ApiResult doGet(String url, int connTimeout, int readTimeout, String charset) {
        return doGet(url, connTimeout, readTimeout, charset, null);
    }

    public static ApiResult doGet(String url, int connTimeout, int readTimeout, String charset, Map<String, String> headers) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeout);
        client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        // Create a method instance.
        GetMethod get = new GetMethod(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
        return getApiResult(url, client, get);
    }

    //----POST-----

    public static ApiResult doPost(String url) {
        return doPost(url, null);
    }

    public static ApiResult doPost(String url, Map<String, String> params) {
        return doPost(url, params, null, 20000);
    }

    public static ApiResult doPost(String url, Map<String, String> params, String charset) {
        return doPost(url, params, charset, 20000);
    }

    public static ApiResult doPost(String url, Map<String, String> params, String charset, int readTimeout) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        PostMethod post = new PostMethod(url);
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> key : params.entrySet()) {
                NameValuePair e = new NameValuePair(key.getKey(), key.getValue());
                nvps.add(e);
            }
            NameValuePair[] nameValues = new NameValuePair[params.size()];
            nameValues = nvps.toArray(nameValues);
            post.setRequestBody(nameValues);
        }
        if (StringUtils.isNotEmpty(charset)) {
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }
        return getApiResult(url, httpClient, post);
    }

    //----POST JSON ----

    public static ApiResult doPostJson(String url, String json) {
        return doPostJson(url, json, null);
    }

    public static ApiResult doPostJson(String url, String json, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(url);
        return getApiResult(url, httpClient, post);
    }

    //----PUT-----

    public static ApiResult doPut(String url) {
        return doPut(url, null);
    }

    public static ApiResult doPut(String url, Map<String, String> params) {
        return doPut(url, params, null, 20000);
    }

    public static ApiResult doPut(String url, Map<String, String> params, String charset) {
        return doPut(url, params, charset, 20000);
    }

    public static ApiResult doPut(String url, Map<String, String> params, String charset, int readTimeout) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        PutMethod put = new PutMethod(url);
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            for (Map.Entry<String, String> key : params.entrySet()) {
                NameValuePair e = new NameValuePair(key.getKey(), key.getValue());
                nameValuePairList.add(e);
            }
            NameValuePair[] nameValues = new NameValuePair[params.size()];
            nameValues = nameValuePairList.toArray(nameValues);
            put.setQueryString(nameValues);
        }
        if (StringUtils.isNotEmpty(charset)) {
            put.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }
        return getApiResult(url, httpClient, put);
    }

    //------公共----

    private static ApiResult getApiResult(String url, HttpClient httpClient, HttpMethod method) {
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                // 获取二进制的byte流
                byte[] b = method.getResponseBody();
                String response = new String(b, DEFAULT_CHARSET);
                return (ApiResult) JSONObject.toBean(JSONObject.fromObject(response), ApiResult.class);
            } else {
                logger.info("服务调用失败 Response Code: " + statusCode);
            }
        } catch (Exception e) {
            logger.error("url=" + url + "\r\n", e);
        } finally {
            method.releaseConnection();
        }
        ApiResult apiResult = new ApiResult();
        apiResult.setMessage("服务调用失败");
        return apiResult;
    }


}
