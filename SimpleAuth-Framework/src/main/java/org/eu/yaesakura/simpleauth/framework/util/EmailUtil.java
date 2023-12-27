package org.eu.yaesakura.simpleauth.framework.util;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件工具类
 *
 * @author YaeSakura
 */

@Component
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public EmailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送邮件验证码
     * @param to 收件人地址
     * @param code 验证码
     */
    public void sendEmailCode(String to, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);

            mimeMessageHelper.setFrom("1512979396@qq.com", "SimpleAuth");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("SimpleAuth");
            mimeMessageHelper.setText("<p>您正在注册SimpleAuth</p>\n" +
                    "<p>验证码为：<strong>" + code + "</strong>\n" +
                    "<p>请勿泄露给他人，如非本人操作，请忽略此邮件。</p>", true);

            javaMailSender.send(mimeMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
