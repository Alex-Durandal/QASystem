package com.durandal.QASystem.controller;

import com.durandal.QASystem.model.User;
import com.durandal.QASystem.service.UserService;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/register/"},method = {RequestMethod.POST})
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("next") String next,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("token")) {
                Cookie cookie = new Cookie("token", map.get("token").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                if (!StringUtils.isEmptyOrWhitespaceOnly(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常"+e.getMessage());
            model.addAttribute("msg", "服务器错误");
            model.addAttribute("next", next);
            return "login";
        }
    }
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="next", required = false) String next,
                        @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("token")) {
                Cookie cookie = new Cookie("token", map.get("token").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (!StringUtils.isEmptyOrWhitespaceOnly(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                model.addAttribute("next", next);
                return "login";
            }
        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("token") String token) {
        userService.logout(token);
        return "redirect:/";
    }
}
