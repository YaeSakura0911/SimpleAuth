package org.eu.yaesakura.simpleauth.framework;

import com.blueconic.browscap.ParseException;
import org.eu.yaesakura.simpleauth.framework.util.BrowserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class FrameworkApplicationTest {

    @Autowired
    private BrowserUtil browserUtil;

    @Test
    public void contextLoad() {
        System.out.println(browserUtil.getBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));
        System.out.println(browserUtil.getPlatform("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));
    }
}
