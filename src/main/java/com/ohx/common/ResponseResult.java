package com.ohx.common;

import java.util.List;

public class ResponseResult<T>{

    /**
     * 错误码
     */
    private Integer errorCode;


    /**
     * 错误信息
     */
    private String errorDesc;

    /**
     * 返回操作码
     */
    private Integer resultCode;

    /**
     * 错误信息
     */
    private T resultData;

    public ResponseResult(){

    }

    public ResponseResult(Integer errorCode, String errorDesc, Integer resultCode){
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.resultCode = resultCode;
    }
    

    public ResponseResult(Integer errorCode, String errorDesc, Integer resultCode, T resultData){
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.resultCode = resultCode;
        this.resultData = resultData;
    }
    
    

    public ResponseResult(BizCodeEnum biz, Integer resultCode, T resultData){
        this.errorCode = biz.getCode();
        this.errorDesc = biz.getMsg();
        setResultCode(resultCode);
        this.resultData = resultData;
    }

    public ResponseResult(BizCodeEnum biz, Integer resultCode, List<T> resultData){
        this.errorCode = biz.getCode();
        this.errorDesc = biz.getMsg();
        setResultCode(resultCode);
        this.resultData = (T) resultData;
    }

    public static <T> ResponseResult<T> success(){
        ResponseResult<T> Result = new ResponseResult<>();

        Result.errorCode = 0;
        Result.errorDesc = "";
        Result.setResultCode(0);
        Result.resultData = null;
        return Result;
    }
    
    


    public static <T> ResponseResult<T> success(T resultData){
        ResponseResult<T> Result = new ResponseResult<>();

        Result.errorCode = 0;
        Result.errorDesc = "";
        Result.setResultCode(0);
        Result.resultData = resultData;
        return Result;
    }

    public static <T> ResponseResult<T> success(List<T> resultData){
        ResponseResult<T> Result = new ResponseResult<>();

        Result.errorCode = 0;
        Result.errorDesc = "";
        Result.setResultCode(0);
        Result.resultData = (T) resultData;
        return Result;
    }

    public static <T> ResponseResult<T> success(BizCodeEnum biz){
        ResponseResult<T> Result = new ResponseResult<>();

        Result.errorCode = biz.getCode();
        Result.errorDesc = biz.getMsg();
        Result.setResultCode(0);
        Result.resultData = null;
        return Result;
    }

    public static <T> ResponseResult<T> error(BizCodeEnum biz){
        ResponseResult Result = new ResponseResult();
        Result.errorCode = biz.getCode();
        Result.errorDesc = biz.getMsg();
        Result.setResultCode(1);
        return Result;
    }

    public static <T> ResponseResult<T> error(String msg){
        ResponseResult Result = new ResponseResult();
        Result.errorCode = 100004;
        Result.errorDesc = msg;
        Result.setResultCode(1);
        return Result;
    }

    public Integer getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(Integer errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorDesc(){
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc){
        this.errorDesc = errorDesc;
    }

    public Integer getResultCode(){
        return resultCode;
    }

    public void setResultCode(Integer resultCode){
        this.resultCode = resultCode;
    }
    
    public T getResultData(){
        return resultData;
    }

    public void setResultData(T resultData){
        this.resultData = resultData;
    }
    

}