package com.qingge.springboot.mapper;

import com.qingge.springboot.controller.dto.UserPasswordDTO;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.YearCount;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);


    @Select("SELECT\n" +
            "    YEAR(create_time) AS time1,\n" +
            "    COUNT(*) AS count1\n" +
            "FROM\n" +
            "    sys_user\n" +
            "GROUP BY\n" +
            "    time1\n" +
            "ORDER BY\n" +
            "    time1;")
    List<YearCount> getYearCount();

}
