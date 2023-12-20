package org.eu.yaesakura.simpleauth.framework.advice;

import org.eu.yaesakura.simpleauth.framework.domain.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author YaeSakura
 */

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseResult<String> exception(Exception ex) {
        return ResponseResult.error(500, ex.getMessage());
    }

}
