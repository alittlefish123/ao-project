package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("task")
public class Task {

    @TableId(type= IdType.AUTO)
    private Integer id;
    private Integer name;
    private String description;
    private Integer state;
    private String date;
    private Integer uid;
    private String phone;
    private String action;
    private Boolean isDelete;
}
