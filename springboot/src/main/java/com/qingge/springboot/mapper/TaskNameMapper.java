package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.TaskName;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskNameMapper extends BaseMapper<TaskName> {
}
