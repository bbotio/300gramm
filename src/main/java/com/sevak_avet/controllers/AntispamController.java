package com.sevak_avet.controllers;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.AntiSpam;
import com.sevak_avet.scheduler.ApproveTaskSubmitter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.model.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by savetisyan on 05.07.15.
 */
@Controller
@Scope("session")
@RequestMapping("/antispam")
public class AntispamController {
    private static Logger log = Logger.getLogger(AntispamController.class.getName());

    @Autowired
    private AntiSpamDao antiSpamDao;

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpSession session, ModelMap params) throws InstagramException {
        log.info("Antispam page GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        UserInfoData data = instagram.getCurrentUserInfo().getData();
        AntiSpam antiSpam = antiSpamDao.getAntiSpam(data.getUsername());

        if(antiSpam.isAntiSpamEnabled()) {
            params.addAttribute("isAntiSpamEnabled", "checked");
        } else {
            params.addAttribute("isAntiSpamEnabled", "");
        }

        Set<String> badWords = antiSpam.getBadWords();
        params.addAttribute("badWordsList", convertBadWordsList(badWords));
        return "antispam";
    }

    private String convertBadWordsList(Set<String> badWords) {
        StringBuilder sb = new StringBuilder();
        badWords.stream().forEach(x -> sb.append(x).append(","));
        return sb.toString();
    }

    @RequestMapping(params = "saveAntiSpam", method = RequestMethod.POST)
    public String saveBadWords(@RequestParam("badWordsList") String badWordsList,
                               @RequestParam("isAntiSpamEnabled") String isAntiSpamEnabled,
                               HttpSession session, ModelMap params) throws InstagramException {

        log.info("badWordsList: " + badWordsList);
        log.info("isAntiSpamEnabled: " + isAntiSpamEnabled);

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        UserInfoData userData = instagram.getCurrentUserInfo().getData();

        AntiSpam antiSpam = antiSpamDao.getAntiSpam(userData.getUsername());
        if(badWordsList.isEmpty()) {
            antiSpam.setAntiSpamEnabled(false);
        }
        if (isAntiSpamEnabled.isEmpty()) {
            antiSpam.setAntiSpamEnabled(false);
        } else {
            if(badWordsList.isEmpty()) {
                antiSpam.setAntiSpamEnabled(false);
                antiSpamDao.update(antiSpam);
                showErrorMessage(params);
                return "antispam";
            }
            antiSpam.setAntiSpamEnabled(true);
        }
        antiSpamDao.update(antiSpam);

        String[] split = Arrays.stream(badWordsList.split(","))
                .map(String::toLowerCase)
                .flatMap(x -> Arrays.stream(x.split(" +")))
                .filter(x -> x.matches("[A-Za-z]+") || x.matches("[А-Яа-яЁё]+"))
                .toArray(String[]::new);

        antiSpam.setBadWords(new HashSet<>(Arrays.asList(split)));
        antiSpamDao.update(antiSpam);

        log.info("Antispam list: " + Arrays.asList(split));
        return "redirect:antispam";
    }

    private void showErrorMessage(ModelMap params) {
        params.addAttribute("visibility", "visible");
        params.addAttribute("errorMessage", "Bad words list must be not empty or anti-spam will be turned off");
    }
}

