package org.starter.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class AuthToken {
    
    private String accessToken;
    private String refreshToken;
    
    public AuthToken (){
    }
    
    public AuthToken(@NotEmpty @JsonProperty("access_token") String accessToken,
                     @NotEmpty @JsonProperty("refresh_token") String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
}
