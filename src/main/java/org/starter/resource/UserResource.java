package org.starter.resource;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import org.starter.api.ChangePassword;
import org.starter.dao.UserDAO;
import org.starter.db.User;
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

@Path("/user/{key}")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserDAO userDAO;
    private AuthService authService;

    @Inject
    public UserResource(UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.authService = authService;
    }

    @GET
    public User getUser(@PathParam("key") NonEmptyStringParam userKey) {
        return new User();
    }

    @POST
    public User RegisterUser() {
        return null;
    }

    @POST
    @Path("change-password")
    @UnitOfWork
    public Response changePassword(@Auth User user, @Valid ChangePassword changePassword) {
        if (authService.checkPassword(changePassword.getOldPassword(), user.getPassword())) {
            user.setPassword(changePassword.getNewPassword());
            userDAO.upsert(user);
            return Response.status(Response.Status.ACCEPTED)
                    .entity("Password Changed")
                    .build();
        }
        throw new WebApplicationException("Failed exchanging token", Response.Status.NOT_ACCEPTABLE);
    }
}
