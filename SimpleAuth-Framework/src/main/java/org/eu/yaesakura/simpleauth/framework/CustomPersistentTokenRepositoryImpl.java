package org.eu.yaesakura.simpleauth.framework;

import org.eu.yaesakura.simpleauth.framework.domain.entity.PersistentLogin;
import org.eu.yaesakura.simpleauth.framework.mapper.PersistentLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class CustomPersistentTokenRepositoryImpl implements PersistentTokenRepository {

    private final PersistentLoginMapper mapper;

    @Autowired
    public CustomPersistentTokenRepositoryImpl(PersistentLoginMapper persistentLoginMapper) {
        this.mapper = persistentLoginMapper;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        mapper.insertToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        mapper.updateToken(tokenValue, lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), series);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLogin persistentLogin = mapper.getTokenBySeries(seriesId);

        if (persistentLogin == null) {
            return null;
        }

        return new PersistentRememberMeToken(
                persistentLogin.getUsername(),
                persistentLogin.getSeries(),
                persistentLogin.getToken(),
                Date.from(persistentLogin.getLastUsed().atZone(ZoneId.systemDefault()).toInstant()));

    }

    @Override
    public void removeUserTokens(String username) {
        mapper.deleteTokenByUsername(username);
    }
}
