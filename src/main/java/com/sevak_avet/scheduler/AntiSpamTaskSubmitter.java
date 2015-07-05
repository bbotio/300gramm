package com.sevak_avet.scheduler;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.User;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by savetisyan on 06.07.15.
 */
@Component
public class AntiSpamTaskSubmitter {
    private static Logger log = Logger.getLogger(AntiSpamTaskSubmitter.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private AntiSpamDao antiSpamDao;

    private static final Map<String, ScheduledFuture<?>> tasks;
    private static final ScheduledExecutorService scheduler;

    static {
        tasks = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void submitTask(User user) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            // TODO: write antispam logic

            try {
                Instagram instagram = new Instagram(user.getToken());
                MediaFeed userFeeds = instagram.getUserFeeds();
                List<MediaFeedData> data = userFeeds.getData();
                Set<String> badWords = antiSpamDao.getBadWords(user);

                for (MediaFeedData mediaFeedData : data) {
                    Comments comments = mediaFeedData.getComments();
                    for (CommentData commentData : comments.getComments()) {
                        String text = commentData.getText();

                        if(false/*TODO: check, that text contains bad words*/) {
                            instagram.deleteMediaCommentById(mediaFeedData.getId(), commentData.getId());
                        }
                    }
                }
            } catch (InstagramException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);

        tasks.put(user.getUsername(), future);
    }

    public void cancel(User user) {
        if (tasks.containsKey(user.getUsername())) {
            tasks.get(user.getUsername()).cancel(false);
        }
    }

    public void init() {
        List<User> allUsersInfo = userDao.getAllUsersInfo();
        allUsersInfo.stream().filter(antiSpamDao::isAntiSpamEnabled).forEach(this::submitTask);
    }
}
