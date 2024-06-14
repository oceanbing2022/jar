package com.ohx.interceptor;

import com.alibaba.fastjson2.JSON;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.entity.User;
import com.ohx.entity.UserRecord;
import com.ohx.service.UserRecordService;
import com.ohx.service.UserService;
import com.ohx.util.IPUtils;
import com.ohx.util.RedisUtils;
import com.ohx.util.SHAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ohx.common.Constant.EXPIRE_TIME;
import static com.ohx.common.Constant.LOGIN_USER_ACCESSTOKEN;

/**
 * 自定义token拦截器
 * @Author: 贾榕福
 * @Date: 2022/10/25
 */

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRecordService userRecordService;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return true;
        String requestURI = request.getRequestURI();
        log.info("过滤到请求：{}", requestURI);
        response.setCharacterEncoding("UTF-8");
        //获取accessToken
        String accessToken = request.getHeader("accessToken");
        log.info("accessToken:{}", accessToken);
        //获取refreshToken
        String refreshToken = request.getHeader("refreshToken");
        log.info("refreshToken:{}", refreshToken);


        //判断是否是跨域请求，并且是options的请求，直接返回true
        if (request.getHeader(HttpHeaders.ORIGIN) != null && HttpMethod.OPTIONS.matches(request.getMethod())) {
            log.info("options的请求");

            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();


            //检查方法是否有passtoken注解，有则跳过认证，直接通过
            if (method.isAnnotationPresent(PassToken.class)) {
                PassToken passToken = method.getAnnotation(PassToken.class);
                if (passToken.required()) {
                    log.info("{}请求已被放行", requestURI);
                    return true;
                }
            }

            //检查有没有需要用户权限的注解
            if (method.isAnnotationPresent(UserLoginToken.class)) {
                UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
                if (userLoginToken.required()) {
                    // 执行认证
                    //token为null
                    if (accessToken == null || "".equals(accessToken)) {
                        response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.ACCESSTOKEN_NOT_FIND)));
                        return false;
                    }

                    List<String> appKeyList = new ArrayList<>();
                    appKeyList.add("b2h4X29jZWFuc3RlbGxhcg==");
                    String appKey = request.getHeader("appKey");
                    String version = request.getParameter("version");
                    String sign = request.getParameter("nid");
                    String timestamp = request.getParameter("eid");
                    String sourceType = request.getParameter("sourceType");
                    String languageId = request.getParameter("languageId");
                    String timezone = request.getParameter("timezone");
                    if(appKey ==null || version == null || sign == null || timestamp == null || sourceType == null || languageId == null || timezone == null ){
                        response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAMETER_ERROR)));
                        return false;
                    } else {
//                        log.info("参数很全");
                        if(!appKeyList.contains(appKey)){
                            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.INVALID_APPKEY)));
                            return false;
                        }
//                        log.info("appKey 正确");
                        if(Math.abs(System.currentTimeMillis() - Long.valueOf(timestamp)) > 2 * 60 * 1000){
                            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAMETER_ERROR)));
                            return false;
                        }
//                        log.info("时间符合范围");
                        Base64.Encoder encoder = Base64.getEncoder();
                        if(!encoder.encodeToString((SHAUtil.getSHA256WithSalt((appKey + timestamp), "ohx")).getBytes(StandardCharsets.UTF_8)).equals(sign)){
                            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
                            return false;
                        }
//                        log.info("签名正确");
                    }

                    //accessToken和refreshToken都没有过期
                    String accessTokenKey = LOGIN_USER_ACCESSTOKEN + accessToken;
                    if (RedisUtils.hasKey(accessTokenKey)) {
                        String ipAddress = IPUtils.getRealIP(request);
                        log.error("ipAddress : " + ipAddress);
                        String uri = request.getRequestURI();
                        String[] tempStr = uri.split("/");
                        uri = tempStr[tempStr.length - 1];
                        Date date = new Date();
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timeStr = dateFormat.format(date);

                        UserRecord userRecord = new UserRecord();
//                        System.out.println(RedisUtils.getStrValue(accessTokenKey));
                        userRecord.setUserName(((User) RedisUtils.getStrValue(accessTokenKey)).getUsername());
                        userRecord.setIp(ipAddress);
                        userRecord.setMethod(uri);

                        java.util.Date dates = new Date();//获得当前时间
                        Timestamp t = new Timestamp(dates.getTime());//将时间转换成Timestamp类型，这样便可以存入到Mysql数据库中

                        userRecord.setDtime(t);

                        userRecordService.save(userRecord);

                        return true;
                    }

                    //accessToken过期
                    if (!RedisUtils.hasKey(accessTokenKey)){
                        response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.TOKEN_TIMEOUT)));
                        return false;
                    }

                    response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.INVALID_TOKEN)));
                    return false;
                }
            }


        }

        response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PATH_FORMAT_ERROR)));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("请求响应完成");
    }
}
