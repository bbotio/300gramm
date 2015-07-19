package com.sevak_avet.controllers;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.BlacklistDao;
import com.sevak_avet.domain.AntiSpam;
import com.sevak_avet.domain.Blacklist;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sevak_avet.scheduler.MorphologyHelper.isCyrillicWord;
import static com.sevak_avet.scheduler.MorphologyHelper.isLatinWord;

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

    @Autowired
    private BlacklistDao blacklistDao;

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpSession session, ModelMap params) throws InstagramException {
        log.info("Antispam page GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        UserInfoData data = instagram.getCurrentUserInfo().getData();
        AntiSpam antiSpam = antiSpamDao.getAntiSpam(data.getUsername());

        List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();
        params.addAttribute("requestedCount", userRequestedBy.isEmpty() ? "" : userRequestedBy.size());

        if (antiSpam.isAntiSpamEnabled()) {
            params.addAttribute("isAntiSpamEnabled", "checked");
        } else {
            params.addAttribute("isAntiSpamEnabled", "");
        }

        log.info("IS EMPTY: " + blacklistDao.isEmpty(data.getUsername()));
        if (blacklistDao.isEmpty(data.getUsername())) {
            params.addAttribute("haveNoUsersInBlackList", "Your blacklist is empty!");
        } else {
            params.addAttribute("blacklist", blacklistDao.getBlackListSet(data.getUsername()));
        }

        Set<String> badWords = antiSpam.getBadWords();
        params.addAttribute("badWordsList", convertBadWordsList(badWords));
        return "antispam";
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
        if (badWordsList.isEmpty()) {
            antiSpam.setAntiSpamEnabled(false);
        }
        if (isAntiSpamEnabled.isEmpty()) {
            antiSpam.setAntiSpamEnabled(false);
        } else {
            if (badWordsList.isEmpty()) {
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
                .filter(x -> isCyrillicWord(x) || isLatinWord(x))
                .toArray(String[]::new);

        antiSpam.setBadWords(new HashSet<>(Arrays.asList(split)));
        antiSpamDao.update(antiSpam);

        log.info("Antispam list: " + Arrays.asList(split));
        return "redirect:antispam";
    }

    @RequestMapping(params = "addToBlackList", method = RequestMethod.POST)
    public String addToBlackList(@RequestParam("black_list_input") String userName, HttpSession session) throws InstagramException {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        if (isUserExists(userName, instagram)) {
            UserInfoData data = instagram.getCurrentUserInfo().getData();

            Blacklist blacklist = blacklistDao.getBlacklist(data.getUsername());
            blacklist.getBlacklist().add(userName);
            blacklistDao.update(blacklist);
        }
        return "redirect:antispam";
    }

    @RequestMapping(params = "removeFromBlackList", method = RequestMethod.POST)
    public String removeFromBlackList(@RequestParam("blackListUserName") String userName, HttpSession session) throws InstagramException {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        UserInfoData data = instagram.getCurrentUserInfo().getData();
        Blacklist blacklist = blacklistDao.getBlacklist(data.getUsername());
        blacklist.getBlacklist().remove(userName);
        blacklistDao.update(blacklist);

        return "redirect:antispam";
    }

    private String convertBadWordsList(Set<String> badWords) {
        StringBuilder sb = new StringBuilder();
        badWords.stream().forEach(x -> sb.append(x).append(","));
        return sb.toString();
    }

    private void showErrorMessage(ModelMap params) {
        params.addAttribute("visibility", "visible");
        params.addAttribute("errorMessage", "Bad words list must be not empty or anti-spam will be turned off");
    }

    private boolean isUserExists(String username, Instagram instagram) throws InstagramException {
        UserFeed userFeed = instagram.searchUser(username);
        return userFeed.getUserList().stream().anyMatch(user -> user.getUserName().equals(username));
    }
}

