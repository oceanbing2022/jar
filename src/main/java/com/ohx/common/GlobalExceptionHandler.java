package com.ohx.common;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author 贾榕福
 * @date 2022/11/11
 */
@ControllerAdvice(annotations = {RestController.class, Configuration.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult exceptionHandler(IllegalArgumentException ex) {
        log.error("参数异常：{}", ex.getMessage());

        return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseResult exceptionHandler(ExpiredJwtException ex){
//        log.error("异常：{}",ex.getClaims());
//
//        return ResponseResult.error("出现异常");
//    }


    @ExceptionHandler(MalformedJwtException.class)
    public ResponseResult exceptionHandler(MalformedJwtException ex) {
        log.error("token无效异常：{}", ex.getMessage());

        return ResponseResult.error(BizCodeEnum.INVALID_TOKEN);
    }

//    @ExceptionHandler(IOException.class)
//    public ResponseResult exceptionHandler(IOException ex) {
//        log.error("连接异常：{}", ex.getMessage());
//
//        return ResponseResult.error(BizCodeEnum.DISCONNECTED);
//    }
    

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseResult exceptionHandler(IncorrectResultSizeDataAccessException ex) {
        log.error("获取到的返回值问题：{}", ex.getMessage());

        return ResponseResult.error(BizCodeEnum.NEO4J_DUPLICATE_DATA);
    }
}
