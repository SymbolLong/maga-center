package com.maga.center.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 签名工具类
 *
 * @author zhangsl
 */
public class SignUtil {


    public static boolean checkSign(JSONObject params, String accessSecret) {
        Map<String, String> map = toMap(params);
        String sign = map.remove("sign");
        String serverSign = Md5Util.MD5(toJoinString(map, accessSecret) );
        return sign.equals(serverSign);
    }

    public static Map<String, String> toMap(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            String value = jsonObject.getString(key);
            if (StringUtils.isNotEmpty(value) && !value.equalsIgnoreCase("null")) {
                map.put(key, value);
            }
        }
        return map;
    }

    public static String toJoinString(Map<String, String> map, String accessSecret) {
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String key : list) {
            sb.append(key).append("=").append(map.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


}
