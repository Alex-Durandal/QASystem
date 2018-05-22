package com.durandal.QASystem.service;

import com.durandal.QASystem.dao.LoginTokenDAO;
import com.durandal.QASystem.dao.UserDAO;
import com.durandal.QASystem.model.LoginToken;
import com.durandal.QASystem.model.User;
import com.durandal.QASystem.utils.QAUtils;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTokenDAO loginTokenDAO;

    public Map<String,Object> register(String username,String password) {
        Map<String,Object> map = new HashMap<>();
        Random random = new Random();

        //用户名密码检测
        if (StringUtils.isEmptyOrWhitespaceOnly(username)) {
            map.put("msg","用户名密码不能为空");
            return map;
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
            map.put("msg","用户名密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user!=null){
            map.put("msg","用户名已存在");
            return map;
        }

        //增加用户
        String salt = UUID.randomUUID().toString().substring(0,5);
        user = new User(username);
        user.setSalt(salt);
        user.setPassword(QAUtils.MD5(password+salt));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        userDAO.addUser(user);

        //登录
        String token = addLoginToken(user.getId()); //添加token
        map.put("token",token); //返回token用于下发到客户端
        return map;
    }

    public Map<String,Object> login(String username,String password) {
        Map<String,Object> map = new HashMap<>();

        //用户名密码检测
        if (StringUtils.isEmptyOrWhitespaceOnly(username)) {
            map.put("msg","用户名密码不能为空");
            return map;
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(password)) {
            map.put("msg","用户名密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null){
            map.put("msg","用户不存在");
            return map;
        }
        if (!QAUtils.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "用户名密码错误");
            return map;
        }

        //登录成功
        String token = addLoginToken(user.getId()); //添加token
        map.put("token",token); //返回token用于下发到客户端
        return map;
    }

    public void logout(String token) {
        loginTokenDAO.updateStatus(token,1);
    }

    private String addLoginToken(int userId) {
        LoginToken token = new LoginToken();
        token.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+10*3600*24);
        token.setExpired(date);
        token.setToken(UUID.randomUUID().toString().replaceAll("-",""));    // 生成token字符串
        loginTokenDAO.addLoginToken(token); // 将token加入数据库
        return token.getToken();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
