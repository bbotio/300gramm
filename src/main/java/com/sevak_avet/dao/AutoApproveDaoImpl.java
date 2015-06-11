package com.sevak_avet.dao;

import com.sevak_avet.domain.AutoApprove;
import com.sevak_avet.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:08
 */
@Component
@Transactional
public class AutoApproveDaoImpl implements AutoApproveDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AutoApprove getAutoApprove(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from AutoApprove where username=:username");
        query.setParameter("username", username);

        List result = query.list();
        if (!result.isEmpty()) {
            return (AutoApprove) result.get(0);
        }

        return null;
    }

    @Override
    public AutoApprove getAutoApprove(User user) {
        return getAutoApprove(user.getUsername());
    }

    @Override
    public Integer getUserPeriod(User user) {
        AutoApprove autoApprove = getAutoApprove(user);
        if (autoApprove != null) {
            return autoApprove.getPeriod();
        }
        return null;
    }

    @Override
    public void save(AutoApprove autoApprove) {
        Session session = sessionFactory.getCurrentSession();
        session.save(autoApprove);
    }

    @Override
    public void update(AutoApprove autoApprove) {
        Session session = sessionFactory.getCurrentSession();
        session.update(autoApprove);
    }
}
