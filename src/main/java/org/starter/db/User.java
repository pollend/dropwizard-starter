package org.starter.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Principal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "username", nullable = false,length = 100)
    private String username;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "update_at", nullable = false)
    private Date updatedAt;

    @Column(name = "suspended", nullable = false)
    @JsonIgnore
    private boolean suspended = false;

    @Column(name = "firstName", nullable = false,length = 100)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false,length = 500)
    @JsonIgnore
    private String password;


    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="user_id", nullable=false,insertable = false,updatable = false)
    private List<UserSession> userSession = new ArrayList<>();

    public User(){

    }

    public long getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return firstName;
    }

    public void addUserSession(UserSession session){
        userSession.add(session);
        session.setUser(this);
    }

    public void removeUserSession(UserSession session){
        userSession.remove(session);
        session.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User)
        {
            return this.getId() == ((User) o).getId();
        }
        return super.equals(o);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
