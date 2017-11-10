package org.starter.dao;

import dagger.Module;
import dagger.Provides;
import io.dropwizard.hibernate.HibernateBundle;
import org.starter.Config;

@Module
public class DataAccessObjectModule {

    private final HibernateBundle<Config> hibernate;

    public DataAccessObjectModule(HibernateBundle<Config> hibernate) {
        this.hibernate = hibernate;
    }

    @Provides
    public UserDAO provideUserDAO() {
        return new UserDAO(hibernate.getSessionFactory());
    }

    @Provides
    public UserSessionDAO provideSessionDAO() {
        return new UserSessionDAO(hibernate.getSessionFactory());
    }
}
