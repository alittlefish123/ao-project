package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.YearCount;
import com.qingge.springboot.entity.Donate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.DonateYearCount;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-04-04
 */
public interface DonateMapper extends BaseMapper<Donate> {

    @Select("SELECT\n" +
            "    YEAR(time) AS time1,\n" +
            "    sum(money) AS countMoney\n" +
            "FROM\n" +
            "    donate\n" +
            "GROUP BY\n" +
            "    time1\n" +
            "ORDER BY\n" +
            "    time1;")
    List<DonateYearCount> getAnimalCountByYear();

}
