package com.durandal.QASystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by alex on 2018/4/28.
 */

@Controller
public class IndexController {

    @RequestMapping(path = {"/index","/"})
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @RequestMapping(path = {"/profile/{userId}"})
    public String profile(@PathVariable("userId") int userId){
        return String.format("Hello user %d",userId);
    }

    @RequestMapping(path = {"/template"})
    public String template(){
        return "hello";
    }

}
