package org.starter.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.starter.dao.UserDAO;
import org.starter.dao.UserSessionDAO;
import org.starter.db.User;
import org.starter.db.UserSession;
import org.starter.factory.AuthFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthService {
    private AuthFactory authFactory;
    private BytesKeyGenerator sessionKeyGenerator;

    public static final long LONG_SESSION_EXPIRE = TimeUnit.DAYS.toMillis(100);
    public static final long SHORT_SESSION_EXPIRE = TimeUnit.DAYS.toMillis(1);

    public static final long AUTH_EXPIRE = TimeUnit.HOURS.toMillis(2);

    public static final String SESSION_KEY = "session_key";

    private final UserSessionDAO userSessionDAO;
    private final UserDAO userDAO;


    public AuthService(AuthFactory authFactory, UserSessionDAO userSessionDAO,UserDAO userDAO) {
        this.authFactory = authFactory;
        sessionKeyGenerator = KeyGenerators.secureRandom(10);
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
        
    }
    
    public String hashPassword(String password) {
        return BCrypt.hashpw(password + this.authFactory.getSalt(), BCrypt.gensalt());
    }
    
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password + this.authFactory.getSalt(), hashedPassword);
    }

    public long verifyAuthToken(String token) throws UnsupportedEncodingException, JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(authFactory.getHMAC256()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("id").asLong();
    }

    public String generateAuthToken(User user) throws UnsupportedEncodingException {
        Date current = new Date();
        Date expire = new Date(current.getTime() + AUTH_EXPIRE);
        return JWT.create()
                .withIssuedAt(current)
                .withExpiresAt(expire)
                .withClaim("id", user.getId())
                .sign(authFactory.getHMAC256());
    }

    public UserSession verifyUserSession(String sessionToken) throws UnsupportedEncodingException, JWTVerificationException {

        JWTVerifier jwtVerifier =  JWT.require(authFactory.getHMAC256()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(sessionToken);
        return userSessionDAO.findSessionByToken(decodedJWT.getClaim(AuthService.SESSION_KEY).asString());
    }
    
    public String updateCreateUserSession(UserSession session) throws UnsupportedEncodingException {
        Date current = new Date();
        Date expire;
        switch (session.getType()) {
            case LONG_TTL:
                expire = new Date(current.getTime() + LONG_SESSION_EXPIRE);
                break;
            case SHORT_TTL:
                expire = new Date(current.getTime() + SHORT_SESSION_EXPIRE);
                break;
            default:
                throw new UnsupportedOperationException("TTL not set");
        }
    
        String key = new String(Hex.encode(sessionKeyGenerator.generateKey()));
    
        if (session.getCreatedAt() == null)
            session.setCreatedAt(current);
        session.setLastUsed(current);
        session.setExpiredOn(expire);
        session.setToken(key);
    
        return JWT.create()
                .withIssuedAt(current)
                .withClaim(AuthService.SESSION_KEY, key)
                .withExpiresAt(expire)
                .sign(authFactory.getHMAC256());
    
    }
    
}
