package org.starter.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_session")
public class UserSession {
    public enum SessionType{
        SHORT_TTL,
        LONG_TTL
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "token", nullable = false)
    private String token;
    
    @Column(name = "expired_on", nullable = false)
    private Date expiredOn;
    
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    
    @Column(name = "last_used", nullable = false)
    private Date lastUsed;
    
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private SessionType type;
    
    @ManyToOne()
    private User user;
    
    
    public UserSession(){
        type = SessionType.SHORT_TTL;
    }
    
    public long getId() {
        return id;
    }
    
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }
    
    public Date getExpiredOn() {
        return expiredOn;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Date getLastUsed() {
        return lastUsed;
    }
    
    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
    
    public User getUser() {
        return user;
    }
    
    public SessionType getType() {
        return type;
    }
    
    public void setType(SessionType type) {
        this.type = type;
    }
    
    public void setUser(User user) {
        this.user = user;
        user.addUserSession(this);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
