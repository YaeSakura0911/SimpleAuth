package org.eu.yaesakura.simpleauth.framework;

import org.eu.yaesakura.simpleauth.framework.util.EmailUtil;
import org.eu.yaesakura.simpleauth.framework.util.CodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FrameworkApplicationTest {

    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private CodeUtil codeUtil;

    @Test
    public void contextLoad() {
        String code = codeUtil.generateCode();
        String phoneNumber = "15277323166";

        emailUtil.sendEmailCode("1454312923@qq.com", code);
    }
}
