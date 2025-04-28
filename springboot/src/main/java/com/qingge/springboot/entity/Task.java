package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("task")
public class Task {

    @TableId(type= IdType.AUTO)
    private Integer id;
    @TableField(exist=false)
    private String taskName;
    private Integer nameId;
    private String description;
    private Integer state;
    private String date;
    private Integer uid;
    @TableField(exist = false)
    private String username;
    private String phone;
    private String action;
    private Boolean isDelete;
    private Integer animalId;
    @TableField(exist = false)
    private String animalName;
}
