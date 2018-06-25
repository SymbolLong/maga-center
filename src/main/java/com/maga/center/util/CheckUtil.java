package com.maga.center.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

    /**
     * 判断字符串是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 隐藏中间4位手机号
     * @param mobiles
     * @return
     */
    public static String hideMidMobileNo(String mobiles) {
        return mobiles.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 密码合法性校验
     */
    public static boolean isValidatePassword(String password){
        Pattern p = Pattern.compile(" ^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
