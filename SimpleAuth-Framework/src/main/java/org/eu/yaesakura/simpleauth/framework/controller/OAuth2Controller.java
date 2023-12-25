package org.eu.yaesakura.simpleauth.framework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author YaeSakura
 */

@RestController
public class OAuth2Controller {
    @RequestMapping("/login/oauth2/google")
    public void googleOAuth2Login(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://accounts.google.com/o/oauth2/v2/auth?client_id=648723572402-842982hcrr4ln1i6gd0lk206lel4h60a.apps.googleusercontent.com&redirect_uri=http://127.0.0.1:8080/login/oauth2/google/callback&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid");
    }

    @RequestMapping("/login/oauth2/google/callback")
    public void googleOAuth2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request);
        Map<String, String[]> parameterMap = request.getParameterMap();

        RestTemplate restTemplate = new RestTemplate();

        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", "648723572402-842982hcrr4ln1i6gd0lk206lel4h60a.apps.googleusercontent.com");
        requestBody.put("client_secret", "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");
        requestBody.put("code", parameterMap.get("code")[0]);
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("redirect_uri", "http://127.0.0.1:8080/login/oauth2/google/callback");

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(requestBody);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token", requestBody, String.class);
        /*
          返回JSON数据
          {
           access_token
           expires_in
           scope
           token_type
           id_token
          }
         */
        System.out.println(responseEntity);

        // TODO: 通过https://www.googleapis.com/oauth2/v3/userinfo获取用户信息，认证采用Bearer Token，返回结果为JSON

        response.sendRedirect("http://localhost:5173/register");
    }

    @RequestMapping("/login/oauth2/github")
    public void githubOAuth2Login(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://github.com/login/oauth/authorize?client_id=bd5e47d435659a43c04d&redirect_uri=http://127.0.0.1:8080/login/oauth2/github/callback");
    }

    @RequestMapping("/login/oauth2/github/callback")
    public void githubOAuth2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request);

        Map<String, String[]> parameterMap = request.getParameterMap();

        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", "bd5e47d435659a43c04d");
        requestBody.put("client_secret", "6d7bf3672b4010e098a5eb6935c07786b95e4c37");
        requestBody.put("code", parameterMap.get("code")[0]);
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("redirect_uri", "http://127.0.0.1:8080/login/oauth2/github/callback");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

//        ResponseEntity<String> responseEntity = restTemplate.exchange("https://github.com/login/oauth/access_token", HttpMethod.POST, httpEntity, String.class);
        GithubAccessToken githubAccessToken = restTemplate.postForObject("https://github.com/login/oauth/access_token", httpEntity, GithubAccessToken.class);
        System.out.println(githubAccessToken);

        // TODO: 通过https://api.github.com/user获取用户信息，认证采用Bearer Token，返回结果为JSON
        /*
        返回数据格式
        {
            login: ""
            id: ""
            avatar_url: ""
            name: ""
            email: ""
        }
         */

        HttpHeaders getHeader = new HttpHeaders();
        getHeader.setBearerAuth(githubAccessToken.getAccess_token());

        HttpEntity<Object> getHttpEntity = new HttpEntity<>(getHeader);

//        restTemplate.getForObject("https://api.github.com/user", getHttpEntity, )

        response.sendRedirect("http://localhost:5173/register");
    }

    @Data
    public static class GithubAccessToken {
        private String access_token;
        private String scope;
        private String token_type;
    }

    @Data
    public static class GithubUserInfo {
        private String email;
        private String login;
//        private
    }
}
