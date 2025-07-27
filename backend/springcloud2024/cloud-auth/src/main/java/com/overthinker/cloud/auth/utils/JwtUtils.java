package com.overthinker.cloud.auth.utils;


import cn.hutool.jwt.JWT;

import com.overthinker.cloud.auth.entity.PO.LoginUser;

import com.overthinker.cloud.redis.utils.MyRedisCache;
import jakarta.annotation.Resource;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * JWT utility class for the auth service.
 */
@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    private String key;

    @Value("${spring.security.jwt.expire}")
    private int expire;

    @Resource
    private MyRedisCache myRedisCache;

    public boolean invalidateJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return false;
        Ed25519.Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private boolean deleteToken(String uuid) {
        if (isInvalidToken(uuid)) return false;
        myRedisCache.deleteObject(RedisConst.JWT_WHITE_LIST + uuid);
        return true;
    }

    private boolean isInvalidToken(String uuid) {
        return !myRedisCache.isHasKey(RedisConst.JWT_WHITE_LIST + uuid);
    }

    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Ed25519.Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            if (this.isInvalidToken(verify.getId())) return null;
            Date expiresAt = verify.getExpiresAt();
            return new Date().after(expiresAt) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String createJwt(UserDetails details, Long id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = expireTime();
        Date now = new Date();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String jwt = JWT.create()
                .withJWTId(uuid)
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)
                .withIssuedAt(now)
                .sign(algorithm);
        myRedisCache.setCacheObject(RedisConst.JWT_WHITE_LIST + uuid, jwt, (int) (expire.getTime() - now.getTime()), TimeUnit.MILLISECONDS);
        return jwt;
    }

    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    public Long toId(DecodedJWT jwt) {
        return jwt.getClaim("id").asLong();
    }

    public String toName(DecodedJWT jwt) {
        return jwt.getClaim("name").asString();
    }

    public List<SimpleGrantedAuthority> toAuthorities(DecodedJWT jwt) {
        List<String> authorities = jwt.getClaim("authorities").asList(String.class);
        return authorities == null ? new ArrayList<>() : authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public UserDetails toUser(DecodedJWT jwt) {
        LoginUser user = new LoginUser();
        user.setUser(new User().setId(toId(jwt)).setUsername(toName(jwt)));
        user.setAuthorities(toAuthorities(jwt));
        return user;
    }

    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith(SecurityConst.TOKEN_PREFIX))
            return null;
        return headerToken.substring(SecurityConst.TOKEN_PREFIX.length());
    }
}
