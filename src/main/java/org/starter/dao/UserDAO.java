package org.starter.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.starter.db.User;
import org.hibernate.SessionFactory;

public class UserDAO extends AbstractDAO<User> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User findById(Long id){
        return get(id);
    }

    public User findByName(String name)
    {
        return (User) currentSession().createQuery("FROM User e where e.name = :name")
                .setParameter("name",name)
                .uniqueResult();
    }


    public User findByEmail(String name)
    {
        return (User) currentSession().createQuery("FROM User e where e.email = :email")
                .setParameter("email",name)
                .uniqueResult();
    }

    public User findByEmailOrUsername(String name){
        return (User) currentSession().createQuery("FROM User e where e.email = :email OR e.name = :name")
                .setParameter("email",name)
                .setParameter("name", name)
                .uniqueResult();
    }

    public User upsert(User user){
        return persist(user);
    }


}
