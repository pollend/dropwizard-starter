package org.starter.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.net.HostAndPort;
import io.dropwizard.setup.Environment;
import org.starter.health.SocketHealthCheck;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;


import javax.validation.constraints.NotNull;

public class MailFactory {
    @NotNull
    protected HostAndPort server;

    protected String username;

    protected String password;

    @NotNull
    protected TransportStrategy transport;

    public MailFactory() {
        this.server = HostAndPort.fromParts("localhost", 25);
        this.username = null;
        this.password = null;
        this.transport = TransportStrategy.SMTP_PLAIN;
    }

    @JsonProperty("server")
    public final HostAndPort getServer() {
        return server;
    }

    @JsonProperty("server")
    public final void setServer(final HostAndPort server) {
        this.server = server;
    }

    @JsonProperty("username")
    public final String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public final void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public final String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public final void setPassword(final String password) {
        this.password = password;
    }

    @JsonProperty("transport")
    public final TransportStrategy getTransport() {
        return transport;
    }

    @JsonProperty("transport")
    public final void setTransport(final TransportStrategy transport) {
        this.transport = transport;
    }

    public final Mailer build(final Environment environment, final String name) {
        final Mailer mailer;

        mailer = new Mailer(server.getHost(), server.getPort(), username, password, transport);
        // TODO figure out how to collect email metrics
        environment.healthChecks().register(name, new SocketHealthCheck(server));
        return mailer;
    }
}
