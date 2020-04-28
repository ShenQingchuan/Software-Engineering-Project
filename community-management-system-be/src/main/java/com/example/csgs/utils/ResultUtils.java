package com.example.csgs.utils;

import lombok.Data;

public class ResultUtils {

    @Data
    private static class Result {
        private Object data;//返回数据
        private String msg;//返回信息
        private String resultCode;//返回结果代码

    }
    /**
     * 成功返回
     *
     * @param data  需要返回的 JSON 格式数据
     * @param msg   需要返回的文字提示信息
     * @return  返回固定 JSON 格式的 Result
     */
    public static Object success(Object data, String msg) {
        Result result = new Result();
        result.setResultCode("200");
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static Object success(String msg) {
        Result result = new Result();
        result.setResultCode("200");
        result.setMsg(msg);
        return result;
    }

    public static Object success(Object data) {
        Result result = new Result();
        result.setResultCode("200");
        result.setData(data);
        return result;
    }

    public static Object success() {
        Result result = new Result();
        result.setResultCode("200");
        return result;
    }

    /**
     * 错误返回
     * @return  返回固定 JSON 格式的 Result
     */
    public static Object error() {
        Result result = new Result();
        result.setResultCode("0");
        return result;
    }

    public static Object error(String msg) {
        Result result = new Result();
        result.setResultCode("0");
        result.setMsg(msg);
        return result;
    }

    public static Object error(String msg, String resultCode) {
        Result result = new Result();
        result.setMsg(msg);
        result.setResultCode(resultCode);
        return result;
    }

}
