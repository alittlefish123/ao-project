package com.qingge.springboot.controller.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer state;
    private String date;
    private String uid;
    private String phone;
    private String action;
    private Boolean isDelete;
}
