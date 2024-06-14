package com.ohx.util;

import org.springframework.stereotype.Component;

/**
 * @author 贾榕福
 * @date 2022/10/28
 */

@Component
public class SetIdUtil {

    public static String setId(String id) {
        //截取头部字母编号
        String head = id.substring(0, id.indexOf("0"));
        //截取尾部数字
        String tail = id.substring(head.length(), id.length());
        //尾部数字 +1
        int num = Integer.valueOf(tail) + 1;
        //填充 0
        String s = null;
        for (int i = 0; i <= id.length(); i++) {
            s += "0";
        }
        //合并字符串
        s = s + num;
        s = s.substring(s.length() - tail.length(), s.length());
        return head + s;
    }

}
