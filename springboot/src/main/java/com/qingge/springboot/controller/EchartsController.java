package com.qingge.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.DonateYearCount;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.entity.YearCount;
import com.qingge.springboot.mapper.AnimalMapper;
import com.qingge.springboot.mapper.DonateMapper;
import com.qingge.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private AnimalMapper animalMapper;

    @Autowired
    private DonateMapper donateMapper;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members() {
//        List<User> list = userService.list();
//        int q1 = 0; // 第一季度
//        int q2 = 0; // 第二季度
//        int q3 = 0; // 第三季度
//        int q4 = 0; // 第四季度
//        for (User user : list) {
//            Date createTime = user.getCreateTime();
//            Quarter quarter = DateUtil.quarterEnum(createTime);
//            switch (quarter) {
//                case Q1: q1 += 1; break;
//                case Q2: q2 += 1; break;
//                case Q3: q3 += 1; break;
//                case Q4: q4 += 1; break;
//                default: break;
//            }
//        }
//        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));

        List<YearCount> yearCount = userService.getYearCount();
        return Result.success(yearCount);
    }


    // 这是返回每一年救助了多少动物
    @GetMapping("/members2")
    public Result members2() {

        List<YearCount> animalCountByYear = animalMapper.getAnimalCountByYear();
//        System.out.println(animalCountByYear.toString()+"================================");
        return Result.success(animalCountByYear);
    }


    //用来统计不同季度的捐款金额
    @GetMapping("/members3")
    public Result members3() {

        List<DonateYearCount> donateCountByYear = donateMapper.getAnimalCountByYear();
        System.out.println(donateCountByYear.toString()+"================================");
        return Result.success(donateCountByYear);
    }


}
