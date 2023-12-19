package org.eu.yaesakura.simpleauth.framework;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLastUsed(token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        mapper.insert(persistentLogin);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLastUsed(lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        LambdaUpdateWrapper<PersistentLogin> persistentLoginUpdateWrapper = new LambdaUpdateWrapper<>();
        persistentLoginUpdateWrapper.eq(PersistentLogin::getSeries, series);

        mapper.update(persistentLogin, persistentLoginUpdateWrapper);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        LambdaQueryWrapper<PersistentLogin> persistentLoginQueryWrapper = new LambdaQueryWrapper<>();
        persistentLoginQueryWrapper.eq(PersistentLogin::getSeries, seriesId);
        PersistentLogin persistentLogin = mapper.selectOne(persistentLoginQueryWrapper);

        return new PersistentRememberMeToken(
                persistentLogin.getUsername(),
                persistentLogin.getSeries(),
                persistentLogin.getToken(),
                Date.from(persistentLogin.getLastUsed().atZone(ZoneId.systemDefault()).toInstant()));

    }

    @Override
    public void removeUserTokens(String username) {
        LambdaQueryWrapper<PersistentLogin> persistentLoginQueryWrapper = new LambdaQueryWrapper<>();
        persistentLoginQueryWrapper.eq(PersistentLogin::getUsername, username);
        mapper.delete(persistentLoginQueryWrapper);
    }
}
