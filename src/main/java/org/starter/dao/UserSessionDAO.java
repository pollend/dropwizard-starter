package org.starter.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.starter.db.User;
import org.starter.db.UserSession;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserSessionDAO extends AbstractDAO<UserSession> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserSessionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public UserSession findSessionByToken(String token)
    {
        return uniqueResult( this.criteria().add(Restrictions.eq("token",token)));
    }

    public List<UserSession> getSessionsByTypeAndUser(User user, UserSession.SessionType sessionType)
    {
        return list(this.criteria().add(Restrictions.and(
                Restrictions.eq("type",sessionType),
                Restrictions.eq("user",user.getId()))));
    }

    public UserSession upsert(UserSession session){
        return persist(session);
    }
}
