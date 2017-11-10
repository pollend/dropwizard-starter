package org.starter.auth;

import io.dropwizard.auth.AuthFilter;

import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;


@Priority(Priorities.AUTHENTICATION)
public class JWTCredentialAuthFilter<P extends Principal> extends AuthFilter<String, P> {
    
    public static final String JWT_ACCESS_TOKEN_PARAM = "access_token";
    
    
    public JWTCredentialAuthFilter(){
    
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String credentials = getCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        if(credentials == null)
        {
            credentials = requestContext.getUriInfo().getQueryParameters().getFirst(JWT_ACCESS_TOKEN_PARAM);
        }

        if(!authenticate(requestContext,credentials, SecurityContext.BASIC_AUTH)){
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix,realm));
        }
    }
    
    /**
     * Parses a value of the `Authorization` header in the form of `Bearer a892bf3e284da9bb40648ab10`.
     *
     * @param header the value of the `Authorization` header
     * @return a token
     */
    @Nullable
    private String getCredentials(String header) {
        if (header == null) {
            return null;
        }
        
        final int space = header.indexOf(' ');
        if (space <= 0) {
            return null;
        }
        
        final String method = header.substring(0, space);
        if (!prefix.equalsIgnoreCase(method)) {
            return null;
        }
        
        return header.substring(space + 1);
    }
    
    
    
    public static class Builder<P extends Principal> extends
            io.dropwizard.auth.AuthFilter.AuthFilterBuilder<String,P,JWTCredentialAuthFilter<P>>{
    
        @Override
        protected JWTCredentialAuthFilter<P> newInstance() {
            return new JWTCredentialAuthFilter();
        }
    }
}
