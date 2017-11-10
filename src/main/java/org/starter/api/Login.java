package org.starter.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Login {

    private String email;
    private String password;

    public Login(){
    }

    public  Login(
            @NotEmpty @JsonProperty("email") String email,
            @NotEmpty @JsonProperty("password") String password){
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
