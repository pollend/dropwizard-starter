package org.starter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePassword {
    private String oldPassword;
    private String newPassword;

    public ChangePassword(){
    }

    public  ChangePassword(
            @JsonProperty("old_password") String oldPassword,
            @JsonProperty("new_password") String newPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
