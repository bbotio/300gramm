package com.sevak_avet.controllers;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.UserDao;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
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
import java.util.List;

/**
 * Created by savetisyan on 05.07.15.
 */
@Controller
@Scope("session")
@RequestMapping("/antispam")
public class AntispamController {
    private static Logger log = Logger.getLogger(AntispamController.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private AntiSpamDao antiSpamDao;

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpSession session) {
        log.info("Antispam page GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        return "antispam";
    }

    @RequestMapping(params = "saveAntiSpam", method = RequestMethod.POST)
    public String saveBadWords(@RequestParam("badWordsList") String badWordsList) {
        log.info(badWordsList);
        return "redirect:antispam";
    }
}

