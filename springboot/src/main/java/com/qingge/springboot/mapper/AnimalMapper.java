package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.Animal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.YearCount;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-04-02
 */
public interface AnimalMapper extends BaseMapper<Animal> {

    @Select("SELECT\n" +
            "    YEAR(time) AS time1,\n" +
            "    COUNT(*) AS count1\n" +
            "FROM\n" +
            "    animal\n" +
            "GROUP BY\n" +
            "    time1\n" +
            "ORDER BY\n" +
            "    time1;")
    List<YearCount> getAnimalCountByYear();

}
