package org.springframework.gramm.web;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.gramm.Constants;
import org.springframework.gramm.util.InstagramUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPageController(HttpServletRequest request, HttpServletResponse response, Model model){
        if (InstagramUtils.isLogged(request)){
            return "redirect:/";
        }

        HttpSession session = request.getSession();

        Properties properties = InstagramUtils.getConfigProperties();
        String clientId = properties.getProperty(Constants.CLIENT_ID);
        String clientSecret = properties.getProperty(Constants.CLIENT_SECRET);
        String callbackUrl = properties.getProperty(Constants.REDIRECT_URI);
        InstagramService service = new InstagramAuthService()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .build();

        String authorizationUrl = service.getAuthorizationUrl(null);
        session.setAttribute(Constants.INSTAGRAM_SERVICE, service);

        model.addAttribute("authorizationUrl", authorizationUrl);

        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();

        session.removeAttribute(Constants.INSTAGRAM_OBJECT);
        session.removeAttribute(Constants.INSTAGRAM_SERVICE);

        return "redirect:/";
    }
}
