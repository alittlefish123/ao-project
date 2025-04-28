package com.qingge.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Notice;
import com.qingge.springboot.entity.Role;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.RoleMapper;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.IRoleService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2022-02-10
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    //获取角色身份
    @RequestMapping("/update")
    public Result getRole(@RequestParam Integer userId) {

        User user = userMapper.selectById(userId);
        Role role = roleMapper.selectById(user.getRoleId());
        user.setRole(role.getFlag());
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll(String roleName) {
        QueryWrapper<Role> queryWrapper=new QueryWrapper<Role>();
        if(roleName!=null && !roleName.equals("")){
            QueryWrapper<Role> roles = queryWrapper.eq("role", roleName);
        }
        return Result.success(roleService.list(queryWrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success( roleService.getRoleMenu(roleId));
    }


    @GetMapping("/apply")
    public Result changeRoleToVolunteer(@RequestParam Integer userId) {
        User user = new User();
        user.setId(userId);
        User user1 = userMapper.selectById(userId);
        if(user1.getRoleId()==2){
            user.setRoleId(4);
        }
        return Result.success( userMapper.updateById(user));
    }


    @GetMapping("/manage")
    public Result applyManage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.orderByDesc(User::getId);

        queryWrapper.eq(User::getRoleId,4);
        if (!name.isEmpty()) {
            queryWrapper.like(User::getUsername, name);  // 如果 name 有值，按用户名模糊查询
        }
        return Result.success(userMapper.selectList(queryWrapper));
    }

    @GetMapping("/handleApply")
    public Result handleApply(@RequestParam Integer userId,@RequestParam String message) {
        User user = new User();
        user.setId(userId);
        if("success".equals(message)){
            user.setRoleId(3);
        }else if("error".equals(message)){
            user.setRoleId(2);
        }
        return Result.success( userMapper.updateById(user));
    }


}

