package org.eu.yaesakura.simpleauth.framework.util;

import com.blueconic.browscap.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * 浏览器工具类
 *
 * @author YaeSakura
 */

@Component
public class BrowserUtil {

    private final UserAgentParser parser;

    public BrowserUtil() {
        try {
            this.parser = new UserAgentService().loadParser(Arrays.asList(
                    BrowsCapField.BROWSER,
                    BrowsCapField.BROWSER_MAJOR_VERSION,
                    BrowsCapField.PLATFORM,
                    BrowsCapField.PLATFORM_VERSION));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取浏览器信息
     *
     * @return 浏览器信息，格式：浏览器名称 浏览器版本
     */
    public String getBrowser(String userAgent) {
        Capabilities capabilities = parser.parse(userAgent);
        String browser = capabilities.getBrowser();
        String browserMajorVersion = capabilities.getBrowserMajorVersion();

        if (browser.equals("Unknown") && browserMajorVersion.equals("Unknown")) {
            return "未知浏览器";
        }

        return browser + " " + browserMajorVersion;
    }

    /**
     * 获取操作系统信息
     *
     * @return 操作系统信息，格式：操作系统名称 操作系统版本
     */
    public String getPlatform(String userAgent) {
        Capabilities capabilities = parser.parse(userAgent);
        String platform = capabilities.getPlatform();
        String platformVersion = capabilities.getPlatformVersion();

        if (platform.equals("Unknown") && platformVersion.equals("Unknown")) {
            return "未知操作系统";
        }

        return platform + " " + platformVersion;
    }
}
