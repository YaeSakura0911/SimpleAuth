package org.eu.yaesakura.simpleauth.framework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证失败处理器
 *
 * @author YaeSakura
 */

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        ResponseResult<Object> responseResult = ResponseResult.error(500, exception.getMessage());

        String s = new ObjectMapper().writeValueAsString(responseResult);

        PrintWriter writer = response.getWriter();
        writer.write(exception.getMessage());
        writer.close();
    }
}
