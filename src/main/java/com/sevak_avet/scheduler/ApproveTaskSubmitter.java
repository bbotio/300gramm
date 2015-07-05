package com.sevak_avet.scheduler;

import com.sevak_avet.dao.AutoApproveDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.User;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.model.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:36
 */

@Component
public class ApproveTaskSubmitter {
    private static Logger log = Logger.getLogger(ApproveTaskSubmitter.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private AutoApproveDao autoApproveDao;

    private static final Map<String, ScheduledFuture<?>> tasks;
    private static final ScheduledExecutorService scheduler;

    static {
        tasks = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void submitTask(User user, Integer time) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                Instagram instagram = new Instagram(user.getToken());
                List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();

                log.info("Approving for user " + instagram.getCurrentUserInfo() + " every " + time + " hours");

                for (UserFeedData userFeedData : userRequestedBy) {
                    instagram.setUserRelationship(userFeedData.getId(), Relationship.APPROVE);
                    log.info("Approved: " + userFeedData);
                }
            } catch (InstagramException e) {
                e.printStackTrace();
            }
        }, 0, time, TimeUnit.HOURS);

        tasks.put(user.getUsername(), future);
    }

    public void cancel(User user) {
        if (tasks.containsKey(user.getUsername())) {
            tasks.get(user.getUsername()).cancel(false);
        }
    }

    public void init() {
        List<User> allUsersInfo = userDao.getAllUsersInfo();
        allUsersInfo.stream().filter(autoApproveDao::isAutoApproveEnabled).forEach(user -> {
            Integer period = autoApproveDao.getUserPeriod(user);
            submitTask(user, period);
        });
    }
}
