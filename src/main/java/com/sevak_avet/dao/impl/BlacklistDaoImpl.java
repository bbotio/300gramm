package com.sevak_avet.dao.impl;

import com.sevak_avet.dao.BlacklistDao;
import com.sevak_avet.domain.Blacklist;
import com.sevak_avet.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Avetisyan Sevak
 * Date: 18.07.2015
 * Time: 19:50
 */
@Repository
@Transactional
public class BlacklistDaoImpl implements BlacklistDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Blacklist getBlacklist(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Blacklist where username=:username");
        query.setParameter("username", username);

        List result = query.list();
        if (!result.isEmpty()) {
            return (Blacklist) result.get(0);
        }

        return null;
    }

    @Override
    public Blacklist getBlacklist(User user) {
        return getBlacklist(user.getUsername());
    }

    @Override
    public Set<String> getBlackListSet(String username) {
        return getBlacklist(username).getBlacklist();
    }

    @Override
    public Set<String> getBlackListSet(User user) {
        return getBlackListSet(user.getUsername());
    }

    @Override
    public boolean isEmpty(User user) {
        return isEmpty(user.getUsername());
    }

    @Override
    public boolean isEmpty(String username) {
        Blacklist blacklist = getBlacklist(username);
        return blacklist == null || blacklist.getBlacklist().isEmpty();
    }

    @Override
    public void save(Blacklist blacklist) {
        Session session = sessionFactory.getCurrentSession();
        session.save(blacklist);
    }

    @Override
    public void update(Blacklist blacklist) {
        Session session = sessionFactory.getCurrentSession();
        session.update(blacklist);
    }
}
