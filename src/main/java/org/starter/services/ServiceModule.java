package org.starter.services;

import dagger.Module;
import dagger.Provides;
import io.dropwizard.setup.Environment;
import org.starter.Config;
import org.starter.dao.UserDAO;
import org.starter.dao.UserSessionDAO;

@Module
public class ServiceModule {

    private Config config;
    private Environment environment;


    public ServiceModule(Config config, Environment environment){
        this.config = config;
        this.environment= environment;
    }

    @Provides
    public MailService provideMailService(){
        return new MailService(config.getMailFactory(),environment);
    }

    @Provides
    public AuthService provideAuth(UserSessionDAO userSessionDAO, UserDAO userDAO){
        return new AuthService(config.getAuthFactory(),userSessionDAO,userDAO);
    }
    
}
