package org.eu.yaesakura.simpleauth.framework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.domain.ResponseResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义注销处理器
 *
 * @author YaeSakura
 */

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        ResponseResult<String> responseResult = ResponseResult.success("注销成功！");

        String s = new ObjectMapper().writeValueAsString(responseResult);

        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.close();
    }
}
