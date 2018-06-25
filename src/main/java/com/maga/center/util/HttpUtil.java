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
        return doGet(url, null);
    }

    public static ApiResult doGet(String url, Map<String, String> header) {
        return doGet(url, header, 20000, null);
    }


    public static ApiResult doGet(String url, Map<String, String> headers, int readTimeout, String charset) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        // Create a method instance.
        GetMethod get = new GetMethod(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if (charset == null) {
            get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
        } else {
            get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }
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

    //----POST PUT JSON ----

    public static ApiResult doPostJson(String url, String json) {
        return doPostJson(url, json, null);
    }

    public static ApiResult doPostJson(String url, String json, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        return getApiResult(url, httpClient, post);
    }

    public static ApiResult doPutJSON(String url, String string, Map<String, String> headers) {
        return doPut(url, null);
    }

    //----PUT-----

    public static ApiResult doPut(String url) {
        return doPut(url, null);
    }

    public static ApiResult doPut(String url, Map<String, String> params) {
        return doPut(url, params, null, null, 20000);
    }

    public static ApiResult doPut(String url, Map<String, String> params, String charset) {
        return doPut(url, params, null, charset, 20000);
    }

    public static ApiResult doPut(String url, Map<String, String> params, Map<String, String> headers, String charset, int readTimeout) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        PutMethod put = new PutMethod(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                put.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if (params != null && !params.isEmpty()) {
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

    //----DELETE----

    public static ApiResult doDelete(String url) {
        return doDelete(url, null);
    }

    public static ApiResult doDelete(String url, Map<String, String> headers) {
        return doDelete(url, headers, null);
    }

    public static ApiResult doDelete(String url, Map<String, String> headers, String charset) {
        return doDelete(url, headers, charset, 20000);
    }

    public static ApiResult doDelete(String url, Map<String, String> headers, String charset, int readTimeout) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        DeleteMethod delete = new DeleteMethod(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                delete.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if (StringUtils.isNotEmpty(charset)) {
            delete.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }
        return getApiResult(url, httpClient, delete);
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
