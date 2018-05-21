package com.durandal.QASystem.service;

import com.durandal.QASystem.dao.UserDAO;
import com.durandal.QASystem.model.User;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

//    public Map<String,String> register(String username,String password) {
//        Map<String,String> map = new HashMap<>();
//        if (StringUtils.isEmptyOrWhitespaceOnly(username)) {
//            map.put("msg","用户名密码不能为空");
//            return map;
//        }
//        if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
//            map.put("msg","用户名密码不能为空");
//            return map;
//        }
//        User user = userDAO.selectByName(username);
//        if (user!=null){
//            map.put("msg","用户名已存在");
//            return map;
//        }
//        String salt = UUID.randomUUID().toString().substring(0,5);
//        user = new User(username);
//        user.setSalt(salt);
//
//
//    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
