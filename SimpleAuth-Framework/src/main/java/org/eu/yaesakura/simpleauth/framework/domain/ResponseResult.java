package org.eu.yaesakura.simpleauth.framework.domain;

import lombok.Data;

/**
 * 通用响应对象
 *
 * @author YaeSakura
 */

@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = 200;
        responseResult.message = "请求成功";
        responseResult.data = data;

        return responseResult;
    }

    public static <T> ResponseResult<T> error(Integer code, String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = code;
        responseResult.message = message;

        return responseResult;
    }
}
