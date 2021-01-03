package com.finder.atm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class BaseController {

    @RequestMapping("/login")
    public String login() {
        return "login.html";
    }

    @RequestMapping({ "/", "/home" })
    public String defaultMethod(Model model) {
        return "home.html";
    }
}
