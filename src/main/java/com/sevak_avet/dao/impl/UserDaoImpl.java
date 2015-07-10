package com.sevak_avet.dao.impl;

import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 6:00 PM
 */

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserInfo(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where username=:username");
        query.setParameter("username", username);

        List result = query.list();
        if (!result.isEmpty()) {
            return (User) result.get(0);
        }

        return null;
    }

    @Override
    public void save(User info) {
        Session session = sessionFactory.getCurrentSession();
        session.save(info);
    }

    @Override
    public void update(User newInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.update(newInfo);
    }

    @Override
    public List<User> getAllUsersInfo() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(User.class).list();
    }
}
