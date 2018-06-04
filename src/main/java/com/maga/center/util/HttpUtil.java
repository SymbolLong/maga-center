package com.maga.center.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);


    public static String doGet(String url) {
        return doGet(url, 20000);
    }

    public static String doGet(String url, String charset) {
        return doGet(url, 3000, 20000, charset);
    }


    public static String doGet(String url, int readTimeout) {
        return doGet(url, 3000, readTimeout);
    }


    public static String doGet(String url, int connTimeout, int readTimeout) {
        return doGet(url, connTimeout, readTimeout, null);
    }

    public static String doGetByUTF8(String url, int connTimeout, int readTimeout) {
        return doGet(url, connTimeout, readTimeout, "UTF-8");
    }

    public static String doGet(String url, int connTimeout, int readTimeout, String charset) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeout);
        client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        String res = null;
        // Create a method instance.
        GetMethod method = null;
        try {
            method = new GetMethod(url);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader reader;
                if (charset == null) {
                    reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                }
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                res = stringBuffer.toString();
            } else {
                logger.info("Response Code: " + statusCode);
            }
        } catch (Exception e) {
            logger.error("url=" + url + "\r\n", e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }

        return res;
    }

    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, "", 20000);
    }

    public static String doPost(String url, Map<String, String> params, String charset) {
        return doPost(url, params, charset, 20000);
    }


    public static String doPost(String url, Map<String, String> params, String charset, int readTimeout) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        PostMethod post = new PostMethod(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> key : params.entrySet()) {
            NameValuePair e = new NameValuePair(key.getKey(), key.getValue());
            nvps.add(e);
        }
        NameValuePair[] nameValues = new NameValuePair[params.size()];
        nameValues = nvps.toArray(nameValues);
        post.setRequestBody(nameValues);
        if (StringUtils.isNotEmpty(charset)) {
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }

        try {
            httpClient.executeMethod(post);
            // 获取二进制的byte流
            byte[] b = post.getResponseBody();
            String str = new String(b, "UTF-8");

            return str;
        } catch (Exception e) {
            logger.error("url=" + url + "\r\n", e);
            return null;
        } finally {
            post.releaseConnection();
        }
    }

    public static String doPost(String url, String jsonString) {
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(url);

        try {

            StringRequestEntity entity = new StringRequestEntity(jsonString, "application/json", "UTF-8");

            post.setRequestEntity(entity);

            httpClient.executeMethod(post);
            // 获取二进制的byte流
            byte[] b = post.getResponseBody();
            String str = new String(b, "UTF-8");

            return str;
        } catch (Exception e) {
            logger.error("", e);

            return null;
        } finally {
            post.releaseConnection();
        }
    }
    public static String doPost(String url, String string, String type) {
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(url);

        try {

            StringRequestEntity entity = new StringRequestEntity(string, "application/"+type, "UTF-8");

            post.setRequestEntity(entity);

            httpClient.executeMethod(post);
            // 获取二进制的byte流
            byte[] b = post.getResponseBody();
            String str = new String(b, "UTF-8");

            return str;
        } catch (Exception e) {
            logger.error("", e);

            return null;
        } finally {
            post.releaseConnection();
        }
    }

    public static String postRequest(String url, Map<String, String> params, HttpMethodParams attachParams) {
        HttpClient client = new HttpClient();
        client.setConnectionTimeout(5000);
        PostMethod method = new PostMethod(url);
        method.setParams(attachParams);
//		Set<String> keys = params.keySet();
        for (Map.Entry<String, String> key : params.entrySet()) {
            method.setParameter(key.getKey(), key.getValue());
        }
        try {
            client.executeMethod(method);
            return new String(method.getResponseBody(), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

}
