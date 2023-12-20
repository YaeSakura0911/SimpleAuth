package org.eu.yaesakura.simpleauth.framework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义访问拒接处理器
 *
 * @author YaeSakura
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        ResponseResult<Object> responseResult = ResponseResult.error(403, accessDeniedException.getMessage());

        String s = new ObjectMapper().writeValueAsString(responseResult);

        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.close();
    }
}
