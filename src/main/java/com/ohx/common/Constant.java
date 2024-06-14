package com.ohx.common;

public class Constant {
    /**
     * 秘钥
     */
    public static final String signKey="abcd";
    /**
     * 有效期3天
     */
    public static final long EXPIRE_TIME = 5 * 24 * 60 * 60 * 1000;
//    public static final long EXPIRE_TIME = 1 * 60 * 1000;
    /**
     * 刷新时间7天
     */
    public static final long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000;
//    public static final long REFRESH_TIME = 3 * 60 * 1000;

    /**
     * 邮箱匹配的正则表达式
     */
    public static final String MAILBOX_REGULAR = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    

    /**
     * 邮箱验证码有效期5分钟
     */
    public static final long EMAIL_TIME = 5 * 60 * 1000;

    /**
     * 验证码邮件标题
     */
    public static final String TITLE = "您的注册验证码来了";

    /**
     * 默认用户昵称前缀
     */
    public static final String USER_NICK_NAME_PREFIX = "ohx_";

    /**
     * redis的accessToken前缀
     */
    public static final String LOGIN_USER_ACCESSTOKEN = "tinghai_login:accessToken:";

    /**
     * redis的refreshToken前缀
     */
    public static final String LOGIN_USER_REFRESHTOKEN = "tinghai_login:refreshToken:";

    /**
     * redis的验证码前缀
     */
    public static final String VERIFY_CODE = "user:code:";
    
    public static final String SERVER_PREFIX = "https://msmt.oceanstellar.com/api";
}


