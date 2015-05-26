package com.sevak_avet.controllers;

import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:23
 */
@Controller
@Scope("session")
@RequestMapping("/logout")
public class LogoutController {
    private static Logger log = Logger.getLogger(LogoutController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String logout(HttpSession session) {
        log.info("------------ LOGOUT GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");

        if (instagram == null) {
            return "redirect:login";
        }

        session.removeAttribute("instagram");
        session.removeAttribute("instagram_service");
        return "logout";
    }
}
