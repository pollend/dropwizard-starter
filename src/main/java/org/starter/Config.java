package org.starter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import org.starter.factory.AuthFactory;
import org.starter.factory.MailFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Config extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("mail")
    private MailFactory mail = new MailFactory();

    @Valid
    @NotNull
    @JsonProperty("flyway")
    private FlywayFactory flyway = new FlywayFactory();
    
    @Valid
    @NotNull
    @JsonProperty("auth")
    private AuthFactory authFactory = new AuthFactory();
    
    
    public AuthFactory getAuthFactory() {
        return authFactory;
    }
    
    public FlywayFactory getFlyway() {
        return flyway;
    }

    public MailFactory getMailFactory() {
        return mail;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

}
