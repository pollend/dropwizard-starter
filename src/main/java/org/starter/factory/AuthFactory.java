package org.starter.factory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

public class AuthFactory {
    @NotNull
    protected String secrete;

    @NotNull
    protected String salt;

    @JsonProperty("secrete")
    public String getSecrete() {
        return secrete;
    }

    @JsonProperty("salt")
    public String getSalt() {
        return salt;
    }


    public Algorithm getHMAC256() throws UnsupportedEncodingException {
        return Algorithm.HMAC256(secrete);
    }

    public JWTCreator.Builder build() {
        return JWT.create();
    }

    public Verification verify(Algorithm algorithm) {
        return JWT.require(algorithm);
    }

}
