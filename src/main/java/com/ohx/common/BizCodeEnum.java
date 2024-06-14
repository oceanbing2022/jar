package com.ohx.common;

public enum BizCodeEnum {
    SUCCEED(200, "登录成功"),
    INVALID_TOKEN(100002,"token无效"),
    TOKEN_TIMEOUT(100003,"accessToken已经过期,请刷新token"),
    PARAM_ERROR1(100004,"系统参数错误"),
    INVALID_APPKEY(100005,"无效的appKey"),
    ACCESSTOKEN_NOT_FIND(100006,"accessToken不存在"),
    REFRESHTOKEN_NOT_FIND(100007,"refreshToken是null"),
    REFRESHTOKEN_TIMEOUT(100008,"refreshToken已经过期,请重新登陆"),
    PARAMETER_ERROR(100010,"参数错误"),
    PATH_FORMAT_ERROR(100011,"路径错误"),
    NEO4J_DUPLICATE_DATA(100012,"neo4j数据库中有重复数据"),
    PARAM_ERROR2(200001,"业务参数错误"),
    PORT_NOT(200002,"暂无此港口"),
    BOAT_NOT(200003,"暂无此船"),
    ACT_NOT(200018,"暂无此动作"),
    LOGIN_FIND_ERROR(200004,"登录失败,用户不存在"),
    LOGIN_ERROR(200005,"登录失败,用户名或密码不正确"),
    PASSWORD_ERROR(200006,"原密码错误"),
    USER_NULL(200007,"用户信息不存在"),
    USER_UPDATE_FAILED(200008,"用户信息更新失败"),
    BUSY_SERVICE(200009,"AIS读取超时"),
    WRONG_VERIFY_CODE(200010,"验证码错误"),
    USER_EMAIL_REPEATED(200011,"用户电子邮件重复"),
    USER_EMAIL_NOT_REPEATED(200012,"用户电子邮件未重复"),
    USER_SIGNUP_FAILED(200013,"用户注册失败"),
    FAIL_SEND(200014,"发送失败"),
    MESSAGE_FORMAT_ERROR(200015,"邮件格式错误"),
    START_TIME_ERROR(200016,"开始时间错误"),
    END_TIME_ERROR(200017,"结束时间错误"),
    ;
    

    
    
    private final Integer code;

    private final String msg;

    BizCodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}

