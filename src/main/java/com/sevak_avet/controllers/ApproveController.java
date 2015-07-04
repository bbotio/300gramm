package com.sevak_avet.controllers;

import com.sevak_avet.TaskSubmitter;
import com.sevak_avet.dao.AutoApproveDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.AutoApprove;
import com.sevak_avet.domain.User;
import org.apache.log4j.Logger;
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
import java.util.List;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:29
 */
@Controller
@Scope("session")
@RequestMapping("/requests")
public class ApproveController {
    private static Logger log = Logger.getLogger(ApproveController.class.getName());

    @Autowired
    private UserDao userDao;

    @Autowired
    private AutoApproveDao autoApproveDao;

    @Autowired
    private TaskSubmitter taskSubmitter;

    @RequestMapping(method = RequestMethod.GET)
    public String request(HttpSession session, ModelMap params) throws InstagramException {
        log.info("--------- REQUESTS GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        UserInfoData userData = instagram.getCurrentUserInfo().getData();
        User user = userDao.getUserInfo(userData.getUsername());
        if(user.isAutoApproveEnabled()) {
            Integer period = autoApproveDao.getUserPeriod(user);
            params.addAttribute("approvePeriod", period);
        }

        List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();
        if (userRequestedBy.isEmpty()) {
            params.addAttribute("haveNoUsersRequestedBy", "You have no users to accept!");
        } else {
            params.addAttribute("users", userRequestedBy);
        }

        params.addAttribute("visibility", "hidden");
        params.addAttribute("errorMessage", "ERROR!!");

        return "requests";
    }

    @RequestMapping(params = "btnApprove", method = RequestMethod.POST)
    public String approveRequest(@RequestParam("userId") String userId, HttpSession session) throws InstagramException {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        instagram.setUserRelationship(userId, Relationship.APPROVE);
        return "redirect:requests";
    }

    @RequestMapping(params = "btnApproveAll", method = RequestMethod.POST)
    public String approveAllRequests(HttpSession session) throws InstagramException {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();
        for (UserFeedData userFeedData : userRequestedBy) {
            instagram.setUserRelationship(userFeedData.getId(), Relationship.APPROVE);
        }
        return "redirect:requests";
    }

    @RequestMapping(params = "saveNewPeriod", method = RequestMethod.POST)
    public String saveNewPeriod(@RequestParam("approvePeriod") String time, HttpSession session, ModelMap params) throws InstagramException {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        UserInfoData userData = instagram.getCurrentUserInfo().getData();

        Integer localTime;
        try {
            localTime = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            params.addAttribute("approvePeriod", time);
            showErrorMessage(params, instagram);
            return "requests";
        }

        if(localTime >= 12) {
            User user = userDao.getUserInfo(userData.getUsername());
            AutoApprove autoApprove = autoApproveDao.getAutoApprove(user);
            autoApprove.setPeriod(localTime);
            autoApproveDao.update(autoApprove);

            log.info("OLD TASK CANCELED FOR USER " + user.getUsername());

            taskSubmitter.cancel(user);
            taskSubmitter.submitTask(user, autoApprove.getPeriod());
        } else {
            params.addAttribute("approvePeriod", time);
            showErrorMessage(params, instagram);
            return "requests";
        }

        return "redirect:requests";
    }

    private void showErrorMessage(ModelMap params, Instagram instagram) throws InstagramException {
        params.addAttribute("visibility", "visible");
        params.addAttribute("errorMessage", "Enter approve period in hours. Must be >= 12.");

        List<UserFeedData> userRequestedBy = instagram.getUserRequestedBy().getUserList();
        if (userRequestedBy.isEmpty()) {
            params.addAttribute("haveNoUsersRequestedBy", "You have no users to accept!");
        } else {
            params.addAttribute("users", userRequestedBy);
        }
    }
}