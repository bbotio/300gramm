package com.sevak_avet.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Scope("session")
@RequestMapping(value = "/")
public class MainController {
    @RequestMapping(method = RequestMethod.GET)
    public String startPage() {
        return "redirect:login";
    }
}