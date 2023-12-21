package org.eu.yaesakura.simpleauth.framework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author YaeSakura
 */

@RestController

public class OAuth2Controller {
    @RequestMapping("/login/oauth2/callback/google")
    public void googleOAuth2(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request);
        System.out.println(response);
    }

    @RequestMapping("/login/oauth2/callback/github")
    public void githubOAuth2(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request);
        System.out.println(response);
    }
}
