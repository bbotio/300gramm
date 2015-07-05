package com.sevak_avet.controllers;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.AutoApproveDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.AntiSpam;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

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
    public String get(HttpSession session, ModelMap params) {
        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram == null) {
            return "redirect:login";
        }

        return "antispam";
    }
}
