package com.sevak_avet.dao.impl;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.domain.AntiSpam;
import com.sevak_avet.domain.AutoApprove;
import com.sevak_avet.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by savetisyan on 05.07.15.
 */
public class AntiSpamDaoImpl implements AntiSpamDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AntiSpam getAntiSpam(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from AntiSpam where username=:username");
        query.setParameter("username", username);

        List result = query.list();
        if (!result.isEmpty()) {
            return (AntiSpam) result.get(0);
        }

        return null;
    }

    @Override
    public AntiSpam getAntiSpam(User user) {
        return getAntiSpam(user.getUsername());
    }

    @Override
    public Set<String> getBadWords(User user) {
        AntiSpam antiSpam = getAntiSpam(user.getUsername());
        if(antiSpam != null) {
            return antiSpam.getBadWords();
        }
        return Collections.emptySet();
    }

    @Override
    public Boolean isAntiSpamEnabled(User user) {
        AntiSpam antiSpam = getAntiSpam(user.getUsername());
        if(antiSpam != null) {
            return antiSpam.isAntiSpamEnabled();
        }
        return false;
    }

    @Override
    public void save(AntiSpam antispam) {
        Session session = sessionFactory.getCurrentSession();
        session.save(antispam);
    }

    @Override
    public void update(AntiSpam antispam) {
        Session session = sessionFactory.getCurrentSession();
        session.update(antispam);
    }
}
