package com.durandal.QASystem.service;

import com.durandal.QASystem.dao.QuestionDAO;
import com.durandal.QASystem.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

}
