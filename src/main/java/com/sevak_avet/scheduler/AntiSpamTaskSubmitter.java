package com.sevak_avet.scheduler;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.User;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jinstagram.Instagram;
import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramBadRequestException;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
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

    @Autowired
    private MorphologyHelper morphologyHelper;

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
                Set<String> badWords = antiSpamDao.getBadWords(user);

                MediaFeed feed = instagram.getRecentMediaFeed("self", 100, null, null, null, null);
                for (MediaFeedData mediaFeedData : feed.getData()) {
                    mediaFeedData.getComments().getComments().stream()
                            .filter(comment -> !comment.getCommentFrom().getUsername().equals(user.getUsername()))
                            .filter(comment -> {
                                try {
                                    return morphologyHelper.checkBadWord(comment.getText(), badWords);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            })
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
            tasks.get(user.getUsername()).cancel(false);
        }
    }

    public void init() {
        List<User> allUsersInfo = userDao.getAllUsersInfo();
        allUsersInfo.stream().filter(antiSpamDao::isAntiSpamEnabled).forEach(this::submitTask);
    }
}
