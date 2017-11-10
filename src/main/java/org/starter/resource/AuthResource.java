package org.starter.resource;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.starter.api.AuthToken;
import org.starter.api.Login;
import org.starter.dao.UserDAO;
import org.starter.dao.UserSessionDAO;
import org.starter.db.User;
import org.starter.db.UserSession;
import org.starter.services.AuthService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Path("/auth/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private final Executor executor = Executors.newFixedThreadPool(50);
    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    private final AuthService authService;


    @Inject
    public AuthResource(UserDAO userDAO,UserSessionDAO userSessionDAO,AuthService authService){
        this.userSessionDAO = userSessionDAO;
        this.userDAO = userDAO;
        this.authService = authService;
    }

    @POST
    @Path("login")
    @UnitOfWork
    public AuthToken login(@Valid Login login) {
        User user = userDAO.findByEmail(login.getEmail());
        if(user == null)
        {
            if(authService.checkPassword(login.getPassword(),user.getPassword()))
            {
                 try {
                     UserSession userSession = new UserSession();
                     userSession.setUser(user);
                     String sessionToken = authService.updateCreateUserSession(userSession);
                     String authToken = authService.generateAuthToken(user);
                     userSessionDAO.upsert(userSession);
                     return new AuthToken(authToken,sessionToken);
                } catch (UnsupportedEncodingException e) {
                     throw new WebApplicationException("Failed logging in", Response.Status.BAD_REQUEST);
                }
            }
        }
        throw new WebApplicationException("Failed logging in", Response.Status.BAD_REQUEST);
    }

    @POST
    @Path("refresh")
    @Timed
    @UnitOfWork
    public AuthToken refresh(@PathParam("token") String token) {
        try {
            UserSession userSession = authService.verifyUserSession(token);
            String sessionToken =  authService.updateCreateUserSession(userSession);
            String authToken =  authService.generateAuthToken(userSession.getUser());

            //update usersession into database
            userSessionDAO.upsert(userSession);
            return new AuthToken(authToken,sessionToken);
        }
        catch (TokenExpiredException e){
            throw new WebApplicationException("Token has expired", Response.Status.BAD_REQUEST);
        }
        catch (UnsupportedEncodingException e) {
            throw new WebApplicationException("Failed exchanging token", Response.Status.BAD_REQUEST);
        }
    }



    /**
     * Exchange short term session to long term session
     */
    @POST
    @Path("exchange")
    @Timed
    @UnitOfWork
    public AuthToken exchangeToken(@Auth User user, @PathParam("token") String token)  {

        try {
            UserSession userSession = authService.verifyUserSession(token);
            if(!userSession.getUser().equals(user))
                throw new WebApplicationException("Failed exchanging token", Response.Status.BAD_REQUEST);

            userSession.setType(UserSession.SessionType.LONG_TTL);
            String sessionToken =  authService.updateCreateUserSession(userSession);
            String authToken =  authService.generateAuthToken(userSession.getUser());

            //update usersession into database
            userSessionDAO.upsert(userSession);
            return new AuthToken(authToken,sessionToken);
        }
        catch (TokenExpiredException e){
            throw new WebApplicationException("Token has expired", Response.Status.BAD_REQUEST);
        }
        catch (UnsupportedEncodingException e) {
            throw new WebApplicationException("Failed exchanging token", Response.Status.BAD_REQUEST);
        }
    }


    @GET
    @Path("sessions")
    public void getSessions(){

    }


}
