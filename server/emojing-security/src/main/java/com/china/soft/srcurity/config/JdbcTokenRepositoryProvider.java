package com.china.soft.srcurity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
/**
 * @Description: 实现持久化令牌的数据处理
 * @author MingYue
 * @Date 2024/4/19
 */

@Slf4j
public class JdbcTokenRepositoryProvider implements PersistentTokenRepository {

    // @Resource
    // private PersistentMapper persistentMapper;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        System.out.println(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        log.info(series,tokenValue,lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info(seriesId);
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        log.info(username);
    }
}
