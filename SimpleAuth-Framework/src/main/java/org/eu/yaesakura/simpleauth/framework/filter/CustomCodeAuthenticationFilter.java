package org.eu.yaesakura.simpleauth.framework.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eu.yaesakura.simpleauth.framework.CustomAuthenticationDetails;
import org.eu.yaesakura.simpleauth.framework.CustomCodeAuthenticationToken;
import org.eu.yaesakura.simpleauth.framework.domain.dto.CodeLoginDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 自定义验证码认证过滤器
 *
 * @author YaeSakura
 */

public class CustomCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            // 从request中解析出JSON数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }

            CodeLoginDTO codeLoginDTO = new ObjectMapper().readValue(stringBuilder.toString(), CodeLoginDTO.class);

            String emailOrPhone = codeLoginDTO.getEmailOrPhone();
            String code = codeLoginDTO.getCode();
            request.setAttribute("principal", emailOrPhone);
            request.setAttribute("remember", codeLoginDTO.getRemember());

            CustomCodeAuthenticationToken authRequest = CustomCodeAuthenticationToken.unauthenticated(emailOrPhone, code);
            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
