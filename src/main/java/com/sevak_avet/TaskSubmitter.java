package com.sevak_avet;

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

import java.time.LocalTime;
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
public class TaskSubmitter {
    private static Logger log = Logger.getLogger(TaskSubmitter.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private AutoApproveDao autoApproveDao;

    private static Map<String, ScheduledFuture<?>> tasks = new HashMap<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void submitTask(User user, LocalTime time) {
        long period = time.getMinute() + time.getHour() * 60;
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                Instagram instagram = new Instagram(user.getToken());
                List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();

                log.info("APPROVING FOR USER " + instagram.getCurrentUserInfo() + " EVERY " + period + "min (" + time + ")");

                for (UserFeedData userFeedData : userRequestedBy) {
                    instagram.setUserRelationship(userFeedData.getId(), Relationship.APPROVE);
                    log.info("APPROVED: " + userFeedData);
                }
            } catch (InstagramException e) {
                e.printStackTrace();
            }
        }, 0, period, TimeUnit.MINUTES);

        tasks.put(user.getUsername(), future);
    }

    public void cancel(User user) {
        if(tasks.containsKey(user.getUsername())) {
            tasks.get(user.getUsername()).cancel(false);
        }
    }

    public void init() {
        List<User> allUsersInfo = userDao.getAllUsersInfo();
        allUsersInfo.stream().filter(User::isAutoApproveEnabled).forEach(user -> {
            LocalTime period = autoApproveDao.getUserPeriod(user);
            submitTask(user, period);
        });
    }
}
