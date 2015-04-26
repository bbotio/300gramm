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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.gramm.util.InstagramUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Controller
public class Page1Controller {

    @RequestMapping("/page1")
    public String page1Controller(HttpServletRequest request, HttpServletResponse response,
                                  Model model){

        if (!InstagramUtils.isLogged(request)){
            return "redirect:/login";
        }

        return "page1/page1";
    }
}
