package com.sevak_avet.controllers;

import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:25
 */
@Controller
@Scope("session")
@RequestMapping("/profile")
public class ProfileController {
    private static Logger log = Logger.getLogger(ProfileController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String profile(HttpSession session, ModelMap params) throws InstagramException {
        log.info("---------- PROFILE GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();
        params.addAttribute("requestedCount", userRequestedBy.isEmpty() ? "" : userRequestedBy.size());

        UserInfoData userData = instagram.getCurrentUserInfo().getData();
        params.addAttribute("picture", userData.getProfile_picture());
        params.addAttribute("username", userData.getUsername());
        params.addAttribute("fullname", userData.getFullName());
        params.addAttribute("follows", userData.getCounts().getFollows());
        params.addAttribute("followedBy", userData.getCounts().getFollwed_by());

        return "profile";
    }
}
