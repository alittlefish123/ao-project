package com.qingge.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.controller.dto.TaskDTO;
import com.qingge.springboot.entity.*;
import com.qingge.springboot.mapper.TaskMapper;

import com.qingge.springboot.mapper.TaskNameMapper;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.ITaskService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ITaskService taskService;
    @Resource
    private TaskNameMapper taskNameMapper;
    @GetMapping("/list")
    public Result list() {

        List arrayList=new ArrayList<TaskDTO>();
        // 第一个任务
        TaskDTO task1 = new TaskDTO();
        task1.setId(1);
        task1.setName("绝育动物");
        task1.setDescription("任务艰巨这只猫很抗拒");
        task1.setState(0);   // 0 是待完成 1 是已完成
        task1.setDate("2025-1-1");
        task1.setUid("张三");
        task1.setPhone("");
        task1.setAction("删除/更换人员");
        arrayList.add(task1);

        // 第二个任务
        TaskDTO task2 = new TaskDTO();
        task2.setId(2);
        task2.setName("动物抓捕");
        task2.setDescription("so eassy");
        task2.setState(1);
        task2.setDate("2025-3-17");
        task2.setUid("李四");
        task2.setPhone("");
        task2.setAction("删除/更换人员");
        arrayList.add(task2);

        // 第三个任务
        TaskDTO task3 = new TaskDTO();
        task3.setId(3);
        task3.setName("动物派送");
        task3.setDescription("今天下雨了明天再去");
        task3.setState(0);
        task3.setDate("2025-3-21");
        task3.setUid("王五");
        task3.setPhone("");
        task3.setAction("删除/更换人员");
        arrayList.add(task3);
        return Result.success(arrayList);
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String state
    ) {

        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
//         查询未删除的记录   这里是逻辑删除
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(state)) {
            int i = Integer.parseInt(state);
            queryWrapper.eq("state",i);
        }
        if (!"".equals(name) && !"1".equals(name)) {

//            queryWrapper.inSql("name",
//                    "SELECT id FROM task_name WHERE name LIKE CONCAT('%', '" + name + "', '%')");
            queryWrapper.eq("uid",name);
            return Result.success(taskMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
        }

        //fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper))
        return Result.success(taskMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @PostMapping("/update")
    public Result update(@RequestBody TaskDTO taskDTO) {
        taskMapper.update(null,new LambdaUpdateWrapper<Task>()
                .eq(Task::getId,taskDTO.getId())
                .set(Task::getState,taskDTO.getState())
        );
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Task task = taskMapper.selectById(id);
        task.setIsDelete(true);
        taskMapper.updateById(task);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Task> files = taskMapper.selectList(queryWrapper);
        for (Task file : files) {
            file.setIsDelete(true);
            taskMapper.updateById(file);
        }
        return Result.success();
    }


    @PostMapping("/user/batch")
    public Result getUsersByBatch(@RequestBody List<String> uids) {

        if (uids == null || uids.isEmpty()) {
            return Result.error("400","用户ID列表不能为空");
        }
        // 使用in查询批量获取用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getId, uids);
        List<User> users = userMapper.selectList(wrapper);
        return Result.success(users);
    }

    @PostMapping("/taskNames")
    public Result getTaskNames(@RequestBody List<String> uids) {

        if (uids == null || uids.isEmpty()) {
            return Result.error("400","任务ID列表不能为空");
        }
        // 使用in查询批量获取用户
        LambdaQueryWrapper<TaskName> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TaskName::getId, uids);
        List<TaskName> taskNames = taskNameMapper.selectList(wrapper);
        return Result.success(taskNames);
    }

    @PostMapping("/user/all")
    public Result getALLUser() {

        // select distinct name from user;
        // 使用in查询批量获取用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getId,User::getNickname,User::getPhone);
        List<User> users = userMapper.selectList(wrapper);
        System.out.println("===============");
        System.out.println(users);
        return Result.success(users);
    }

    @PostMapping("/names")
    public Result getTaskNames() {

        LambdaQueryWrapper<TaskName> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TaskName::getId,TaskName::getName);
        List<TaskName> taskName = taskNameMapper.selectList(wrapper);
        System.out.println("===============");
        System.out.println(taskName);
        return Result.success(taskName);
    }

    @PostMapping("/add")
    public Result addTask(@RequestBody Task task) {
        System.out.println("===============add======");
        System.out.println(task);
        taskService.save(task);
        return Result.success();
    }


}
