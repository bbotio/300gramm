package org.springframework.gramm.web;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.gramm.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class InstagramTokenHandler extends HttpServlet {

    @RequestMapping("/handleInstagramToken")
    protected String login(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");

        if (code == null)
            return "redirect:/";

        InstagramService service = (InstagramService) request.getSession().getAttribute(Constants.INSTAGRAM_SERVICE);

        Verifier verifier = new Verifier(code);

        Token accessToken = service.getAccessToken(null, verifier);

        Instagram instagram = new Instagram(accessToken);

        HttpSession session = request.getSession();

        session.setAttribute(Constants.INSTAGRAM_OBJECT, instagram);


        // Redirect to User Profile page.
        return "redirect:/";

    }


}
