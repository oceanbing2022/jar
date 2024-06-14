package com.ohx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ohx.common.Constant.MAILBOX_REGULAR;

/**
 * @author 贾榕福
 * @date 2022/11/21
 */

public class CheckMailUtil {
    public static boolean checkMail(String mail){
        Pattern pattern=Pattern.compile(MAILBOX_REGULAR);
        //\w+@(\w+.)+[a-z]{2,3}
        Matcher matcher=pattern.matcher(mail);
        return matcher.matches();
    }
}

