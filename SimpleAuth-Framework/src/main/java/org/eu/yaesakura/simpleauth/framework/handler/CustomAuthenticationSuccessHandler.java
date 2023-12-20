package org.eu.yaesakura.simpleauth.framework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.ResponseResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证成功处理器
 *
 * @author YaeSakura
 */

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        ResponseResult<String> responseResult = ResponseResult.success("认证成功！");

        String s = new ObjectMapper().writeValueAsString(responseResult);

        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.close();
    }
}
