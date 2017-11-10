package org.starter.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.starter.dao.UserDAO;
import org.starter.db.User;
import org.starter.services.AuthService;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class JWTAuth implements Authenticator<String,User> {

    private AuthService authService;
    private UserDAO userDAO;

    @Inject
    public JWTAuth(AuthService authService, UserDAO userDAO){
        this.authService = authService;
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        try {
            return Optional.ofNullable(userDAO.findById(this.authService.verifyAuthToken(credentials)));
        } catch (UnsupportedEncodingException e) {
            throw new AuthenticationException("Token is invalid");
        }
    }
}
