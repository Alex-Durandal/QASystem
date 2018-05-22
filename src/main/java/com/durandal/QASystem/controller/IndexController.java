package com.durandal.QASystem.controller;

import com.durandal.QASystem.model.Question;
import com.durandal.QASystem.model.User;
import com.durandal.QASystem.model.ViewObject;
import com.durandal.QASystem.service.QuestionService;
import com.durandal.QASystem.service.UserService;
import com.durandal.QASystem.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.*;


/**
 * Created by alex on 2018/4/28.
 */

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    WendaService wendaService;
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/index", "/"})
    @ResponseBody
    public String index(HttpSession httpSession) {
        logger.info("visit index");
        return wendaService.getMessage(2) + "Hello user 2" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", required = false) String key) {
        return String.format("Profile Page of %d, t:%d k:%s", userId, type, key);
    }

    @RequestMapping(path = {"/template"}, method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "vvvv1");
        List<String> colors = Arrays.asList(new String[]{"Red", "Green", "Blue"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("LEE"));
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "hello";
    }

    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request, @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cookie value:" + sessionId);
        Enumeration<String> headerNmaes = request.getHeaderNames();
        while (headerNmaes.hasMoreElements()) {
            String name = headerNmaes.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("cookie:" + cookie.getName() + " value:" + cookie.getValue()+"<br>");
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("userdefinedId","hello");
        response.addCookie(new Cookie("username","xly"));

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }
    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw  new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
