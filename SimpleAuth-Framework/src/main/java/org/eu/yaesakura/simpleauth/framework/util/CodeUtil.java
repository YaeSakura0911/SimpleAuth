package org.eu.yaesakura.simpleauth.framework.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 验证码工具类
 *
 * @author YaeSakura
 */

@Component
public class CodeUtil {

    @Value("${simple-auth.config.code-length}")
    private Integer length;

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    public String generateCode() {
        if (length < 4) {
            length = 4;
        }
        else if (length > 6) {
            length = 6;
        }

        int code = (int) (Math.random() * Math.pow(10, length));

        return String.valueOf(code);
    }
}
