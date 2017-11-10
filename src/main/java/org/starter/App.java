package org.starter;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.starter.auth.SampleAuthorize;
import org.starter.auth.JWTCredentialAuthFilter;
import org.starter.dao.DataAccessObjectModule;
import org.starter.db.User;
import org.starter.db.UserSession;
import org.starter.services.ServiceModule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;


public class App extends Application<Config>{
    public static void main(String[] args) throws Exception {
       new App().run(args);
    }
    
    private final HibernateBundle<Config> hibernateBundle = new HibernateBundle<Config>(User.class, UserSession.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(Config configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    
    private final FlywayBundle<Config> flywayBundle = new FlywayBundle<Config>() {
        @Override
        public DataSourceFactory getDataSourceFactory(Config configuration) {
            return configuration.getDataSourceFactory();
        }
        
        @Override
        public FlywayFactory getFlywayFactory(Config configuration) {
            return configuration.getFlyway();
        }
    };
    
    
    @Override
    public String getName(){
        return  "starter";
    }


    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        //add flyway bundle
        bootstrap.addBundle(flywayBundle);
        bootstrap.addBundle(hibernateBundle);
        
        super.initialize(bootstrap);
    }

    @Override
    public void run(Config configuration, Environment environment) throws Exception {
        Core core = DaggerCore.builder()
                .dataAccessObjectModule(new DataAccessObjectModule(hibernateBundle))
                .serviceModule(new ServiceModule(configuration,environment)).build();


       //register jersy
        environment.jersey().register(core.userResource());
        environment.jersey().register(core.authResource());

        environment.jersey().register(new AuthDynamicFeature(new JWTCredentialAuthFilter.Builder<User>()
                .setAuthenticator(core.jwtAuth())
                .setAuthorizer(new SampleAuthorize())
                .setPrefix("Bearer")
                .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }
}
