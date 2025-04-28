package com.qingge.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qingge.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    @TableField(exist = false)
    private String role;
    private Integer roleId;
    private List<Menu> menus;
}
