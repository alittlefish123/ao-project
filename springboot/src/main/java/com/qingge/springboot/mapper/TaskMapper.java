package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    @Select("select * from task where id=(\n" +
            "select id from task_name\n" +
            "where name like concat('%',#{myKey},'%'))")
    List<Task> selectTaskByName(@Param("myKey")String name);
}
