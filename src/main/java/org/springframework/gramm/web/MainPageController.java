/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.gramm.web;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.gramm.Constants;
import org.springframework.gramm.util.InstagramUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MainPageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String mainPageController(HttpServletRequest request,
                                     HttpServletResponse response, Model model) throws InstagramException{

        if (!InstagramUtils.isLogged(request)){
            return "redirect:/login";
        }

        // set user object to view
        HttpSession session = request.getSession();
        Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);
        Instagram instagram = (Instagram) objInstagram;

        UserInfoData userInfoData = instagram.getCurrentUserInfo().getData();
        model.addAttribute("user", userInfoData);

        return "main";
    }


}
