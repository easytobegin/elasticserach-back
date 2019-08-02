package com.panport.logCloud.utils;

/**
 * Created by minisheep on 2019/7/25.
 */
public class UniUtil {
    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }
}
