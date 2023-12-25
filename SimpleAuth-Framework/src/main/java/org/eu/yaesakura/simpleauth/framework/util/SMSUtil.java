package org.eu.yaesakura.simpleauth.framework.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 短信工具类
 *
 * @author YaeSakura
 */

@Component
public class SMSUtil {

    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;


    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @param code 验证码
     */
    public void sendSMS(String phoneNumber, String code) {
        try {
            // 创建客户端
            Config config = new Config();
            config.setAccessKeyId(accessKeyId);
            config.setAccessKeySecret(accessKeySecret);
            config.setEndpoint(endpoint);
            Client client = new Client(config);

            // 组装短信
            HashMap<String, String> smsTemplate = new HashMap<>();
            smsTemplate.put("code", code);

            // 发送短信
            SendSmsRequest request = new SendSmsRequest();
            request.phoneNumbers = phoneNumber;
            request.signName = "SimpleAuth";
            request.templateCode = "SMS_464350971";
            request.templateParam = new ObjectMapper().writeValueAsString(smsTemplate);
            SendSmsResponse response = client.sendSms(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
