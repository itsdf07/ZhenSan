package com.sdf.aso.common;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by itsdf07 on 2017/3/14 14:33.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class StringUtils {
    private StringUtils() {
        throw new UnsupportedOperationException("Sorry,Instantiation [StringUtils.java] Failure!");
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param c the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence c) {
        return TextUtils.isEmpty(c);
    }

    /**
     * Returns true if the string is available.
     * true:字符串不为null或者不为空格串
     *
     * @param str
     * @return
     */
    public static boolean isAvailable(String str) {
        return (null == str || str.trim().length() == 0);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null时转成长度为0的字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return null == s ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return null == s ? 0 : s.length();
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }
}
