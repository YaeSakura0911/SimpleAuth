package org.eu.yaesakura.simpleauth.framework;

import org.eu.yaesakura.simpleauth.framework.util.SMSUtil;
import org.eu.yaesakura.simpleauth.framework.util.CodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class FrameworkApplicationTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SMSUtil smsUtil;
    @Autowired
    private CodeUtil codeUtil;

    @Test
    public void contextLoad() {
        String code = codeUtil.generateCode();
        String phoneNumber = "15277323166";


        stringRedisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES);

//        try {
//            smsUtil.sendSMS(phoneNumber, code);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
