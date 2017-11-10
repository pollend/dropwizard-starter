package org.starter;

import dagger.Component;
import org.starter.auth.JWTAuth;
import org.starter.dao.DataAccessObjectModule;
import org.starter.resource.AuthResource;
import org.starter.resource.UserResource;
import org.starter.services.ServiceModule;


@Component(modules = {DataAccessObjectModule.class, ServiceModule.class})
public interface Core {
    UserResource userResource();
    AuthResource authResource();
    JWTAuth jwtAuth();
}
