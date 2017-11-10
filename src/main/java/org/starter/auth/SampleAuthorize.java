package org.starter.auth;

import io.dropwizard.auth.Authorizer;
import org.starter.db.User;

public class SampleAuthorize implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        return true;
    }
}
