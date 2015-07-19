package com.sevak_avet.scheduler;

import com.sevak_avet.dao.BlacklistDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.User;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramBadRequestException;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Avetisyan Sevak
 * Date: 18.07.2015
 * Time: 19:53
 */
@Component
public class BlacklistTaskSubmitter {
    private static Logger log = Logger.getLogger(BlacklistTaskSubmitter.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private BlacklistDao blacklistDao;

    private static final Map<String, ScheduledFuture<?>> tasks;
    private static final ScheduledExecutorService scheduler;

    static {
        tasks = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void submitTask(User user) {
        /*ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                Instagram instagram = new Instagram(user.getToken());
                Set<String> blacklist = blacklistDao.getBlackListSet(user);

                MediaFeed feed = instagram.getRecentMediaFeed("self", 100, null, null, null, null);
                for (MediaFeedData mediaFeedData : feed.getData()) {
                    mediaFeedData.getComments().getComments().stream()
                            .filter(comment -> !comment.getCommentFrom().getUsername().equals(user.getUsername()))
                            .filter(comment -> blacklist.contains(comment.getCommentFrom().getUsername()))
                            .forEach(comment -> {
                                try {
                                    instagram.deleteMediaCommentById(mediaFeedData.getId(), comment.getId());
                                } catch (InstagramException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            } catch (InstagramException e) {
                if (e instanceof InstagramBadRequestException) {
                    log.info("Token is expired for user " + user.getUsername());
                    user.setToken(null);
                    userDao.update(user);
                    cancel(user);
                }
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);

        tasks.put(user.getUsername(), future);*/
    }

    public void cancel(User user) {
        if (tasks.containsKey(user.getUsername())) {
            log.info("Blacklist task is canceled for user " + user.getUsername());
            tasks.get(user.getUsername()).cancel(false);
        }
    }

    public void init() {
        List<User> allUsersInfo = userDao.getAllUsersInfo();
        allUsersInfo.stream()
                .filter(user -> user.getToken() != null)
                .filter(user -> !blacklistDao.isEmpty(user))
                .forEach(this::submitTask);
    }
}
