package org.eu.yaesakura.simpleauth.framework;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 持久化记住我令牌
 * @author YaeSakura
 */

@Getter
public class CustomPersistentRememberMeToken {
    private final Long id;
    private final String series;
    private final String tokenValue;
    private final LocalDateTime date;

    public CustomPersistentRememberMeToken(Long id, String series, String tokenValue, LocalDateTime date) {
        this.id = id;
        this.series = series;
        this.tokenValue = tokenValue;
        this.date = date;
    }
}
