package com.ohx.interceptor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.ResponseResult;
import com.ohx.filter.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.internal.shaded.reactor.util.annotation.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Objects;

@CrossOrigin(origins = "*")
@Slf4j
@Component
public class QueryIntercepor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }else{
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()){
                String str = enumeration.nextElement();
                String str_value = request.getParameter(str);
                log.info("参数名： " +str + "参数值： " + str_value);
            }

            log.info(request.getParameterNames().toString());
            return true;
        }



//        String accessToken0 = "null";//必需    Token
//        String appkey0 = "123";//必需    Key
//        String version0 = "100";//必需    version
//        String sign0 = "0";//必需    sign
//        String timestamp0 = "0";//必需    timeStamp
//        String sourcetype0 = "1";//必需    sourceType
//        String languageid0 = "0";// 非必需
//        String timezone0 = "8";// 非必需
//
//        //如果是POST请求
//        if (HttpMethod.POST.matches(request.getMethod())) {
//            accessToken0 = request.getHeader("accessToken");
//            appkey0=request.getHeader("appKey");
//            String read = null;
//            try {
//                RequestWrapper requestWrapper = new RequestWrapper(request);
//                read = requestWrapper.getBody();
//                log.info("body:{}",read);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            JSONObject jsonObject = JSON.parseObject(read);
//            version0 = jsonObject.getString("version");
//            sign0 = jsonObject.getString("sign");
//            timestamp0 = jsonObject.getString("timeStamp");
//            sourcetype0 = jsonObject.getString("sourceType");
//            languageid0 = jsonObject.getString("languageId");
//            timezone0 = jsonObject.getString("timezone");
//
//        }else {
//            accessToken0 = request.getHeader("accessToken");
//            appkey0 = request.getHeader("appKey");
//            version0 = request.getParameter("version");
//            sign0 = request.getParameter("sign");
//            timestamp0 = request.getParameter("timeStamp");
//            sourcetype0 = request.getParameter("sourceType");
//
//            languageid0 = request.getParameter("languageId");
//            timezone0 = request.getParameter("timezone");
//        }
//
//        log.info("拦截到查询请求:"+request.getRequestURI()+"    {accessToken:" + accessToken0
//                + " appKey:" + appkey0 + " version:" + version0
//                + " sign:" + sign0 + " timeStamp:" + timestamp0
//                + " languageId:" + languageid0
//                + " sourceType:" + sourcetype0
//                + " timezone:" + timezone0 + "}");
//
//        /**
//         * 检查必需参数是否存在
//         * **/
//        if(accessToken0==null || appkey0==null || version0==null ||
//                sign0==null || timestamp0==null || sourcetype0==null ||
//                Objects.equals(accessToken0, "") || Objects.equals(appkey0, "") ||
//                Objects.equals(version0, "") || Objects.equals(sign0, "") ||
//                Objects.equals(timestamp0, "") || Objects.equals(sourcetype0, "")){
//            log.info("警告: 100004 Param error, see doc for more info 1");
//            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//            return false;
//        }
//        /**
//         * 类型转换
//         * **/
//        String accesstoken;
//        String appkey;
//        Integer version;
//        String sign;
//        Long timestamp;
//        Integer sourcetype;
//        try {
//            accesstoken = accessToken0;
//            appkey = appkey0;
//            version = Integer.valueOf(version0);
//            sign = sign0;
//            timestamp = Long.valueOf(timestamp0);
//            sourcetype = Integer.valueOf(sourcetype0);
//        } catch (Exception e) {
//            log.info("警告: 100004 Param error, see doc for more info 2");
//            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//            return false;
//        }
//        if(version<100){
//            log.info("警告: 100004 Param error, see doc for more info 3");
//            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//            return false;
//        }
//        if(sourcetype!=1 && sourcetype!=2 && sourcetype!=3){
//            log.info("警告: 100004 Param error, see doc for more info 4");
//            response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//            return false;
//        }
//
//        if(languageid0!=null && !Objects.equals(languageid0, "")){
//            Integer languageid;
//            try{
//                languageid=Integer.valueOf(languageid0);
//            }catch (Exception e){
//                log.info("警告: 100004 Param error, see doc for more info 5");
//                response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//                return false;
//            }
//            if(languageid!=0 && languageid!=1){
//                log.info("警告: 100004 Param error, see doc for more info 6");
//                response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//                return false;
//            }
//
//        }
//        if(timezone0!=null && !Objects.equals(timezone0, "")){
//            Integer timezone;
//            try{
//                timezone=Integer.valueOf(timezone0);
//            }catch (Exception e){
//                log.info("警告: 100004 Param error, see doc for more info 7");
//                response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//                return false;
//            }
//            if(timezone<-12 || timezone>12){
//                log.info("警告: 100004 Param error, see doc for more info 8");
//                response.getWriter().write(JSON.toJSONString(ResponseResult.error(BizCodeEnum.PARAM_ERROR1)));
//                return false;
//            }
//
//        }
//        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}