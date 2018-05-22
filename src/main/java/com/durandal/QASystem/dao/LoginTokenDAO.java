package com.durandal.QASystem.dao;

import com.durandal.QASystem.model.LoginToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTokenDAO {
    String TABLE_NAME = " login_token ";
    String INSERT_FIELDS = " user_id, expired, status, token ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{token})"})
    int addLoginToken(LoginToken loginToken);

    @Select({"select ",SELECT_FIELDS," from " ,TABLE_NAME, "where token=#{token}"})
    LoginToken selectLoginToken(String token);

    @Update({"update ", TABLE_NAME, " set status=#{status} where token=#{token}"})
    void updateStatus(@Param("token") String token, @Param("status")int status);

}
