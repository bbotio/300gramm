package com.sevak_avet.controllers;

import com.sevak_avet.dao.AntiSpamDao;
import com.sevak_avet.dao.AutoApproveDao;
import com.sevak_avet.dao.BlacklistDao;
import com.sevak_avet.dao.UserDao;
import com.sevak_avet.domain.AntiSpam;
import com.sevak_avet.domain.AutoApprove;
import com.sevak_avet.domain.Blacklist;
import com.sevak_avet.domain.User;
import com.sevak_avet.scheduler.AntiSpamTaskSubmitter;
import com.sevak_avet.scheduler.ApproveTaskSubmitter;
import com.sevak_avet.scheduler.BlacklistTaskSubmitter;
import org.apache.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 10:30
 */
@Controller
@Scope("session")
@RequestMapping("/login")
public class LoginController {
    private static Logger log = Logger.getLogger(LoginController.class.getName());

    @Value("${client_id}")
    private String CLIENT_ID;

    @Value("${secret}")
    private String SECRET;

    @Value("${url}")
    private String URL;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AutoApproveDao autoApproveDao;

    @Autowired
    private AntiSpamDao antiSpamDao;

    @Autowired
    private BlacklistDao blacklistDao;

    @Autowired
    private ApproveTaskSubmitter approveTaskSubmitter;

    @Autowired
    private AntiSpamTaskSubmitter antiSpamTaskSubmitter;

    @Autowired
    private BlacklistTaskSubmitter blacklistTaskSubmitter;

    @RequestMapping(method = RequestMethod.GET)
    public String login(HttpSession session, ModelMap params) {
        log.info("---------- LOGIN GET");

        Instagram instagram = (Instagram) session.getAttribute("instagram");
        if (instagram != null) {
            return "redirect:/profile";
        }

        InstagramService service = new InstagramAuthService()
                .apiKey(CLIENT_ID)
                .apiSecret(SECRET)
                .callback(URL)
                .scope("relationships comments")
                .build();

        session.setAttribute("instagram_service", service);
        String authorizationUrl = service.getAuthorizationUrl(null);
        params.addAttribute("authorizationUrl", authorizationUrl);
        return "/login";
    }

    @RequestMapping(value = "/handleToken")
    public String handleToken(HttpSession session, HttpServletRequest request) throws InstagramException {
        String code = request.getParameter("code");
        if (code == null) {
            return "redirect:/login";
        }

        InstagramService service = (InstagramService) session.getAttribute("instagram_service");

        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(null, verifier);
        Instagram instagram = new Instagram(accessToken);
        session.setAttribute("instagram", instagram);

        UserInfoData userData = instagram.getCurrentUserInfo().getData();
        User user = userDao.getUserInfo(userData.getUsername());

        if (user == null) {
            user = new User();
            user.setUsername(userData.getUsername());
            user.setToken(accessToken);
            userDao.save(user);

            AutoApprove autoApprove = new AutoApprove();
            autoApprove.setUserName(userData.getUsername());
            autoApprove.setAutoApproveEnabled(true);
            autoApprove.setPeriod(12);
            autoApproveDao.save(autoApprove);

            HashSet<String> badWords = new HashSet<>();
            badWords.add("the");
            badWords.add("mother");
            badWords.add("of");
            badWords.add("dragons");

            AntiSpam antiSpam = new AntiSpam();
            antiSpam.setUsername(userData.getUsername());
            antiSpam.setAntiSpamEnabled(true);
            antiSpam.setBadWords(badWords);
            antiSpamDao.save(antiSpam);

            Blacklist blacklist = new Blacklist();
            blacklist.setUsername(userData.getUsername());
            blacklist.setBlacklist(new HashSet<>());
            blacklistDao.update(blacklist);

            approveTaskSubmitter.submitTask(user, 12);
            antiSpamTaskSubmitter.submitTask(user);
            blacklistTaskSubmitter.submitTask(user);
        } else if(user.getToken() == null) {
            log.info("Token updated for user " + user.getUsername());
            user.setToken(accessToken);
            userDao.update(user);

            AutoApprove autoApprove = autoApproveDao.getAutoApprove(user);
            if(autoApprove.isAutoApproveEnabled()) {
                approveTaskSubmitter.submitTask(user, autoApprove.getPeriod());
            }

            if(antiSpamDao.isAntiSpamEnabled(user)) {
                antiSpamTaskSubmitter.submitTask(user);
            }

            if(!blacklistDao.isEmpty(user)) {
                blacklistTaskSubmitter.submitTask(user);
            }
        }

        return "redirect:/profile";
    }
}
