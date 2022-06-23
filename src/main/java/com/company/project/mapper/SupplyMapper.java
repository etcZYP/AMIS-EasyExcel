package com.company.project.mapper;

import com.company.project.entity.Supply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyp
 * @since 2022-05-24
 */
@Mapper
public interface SupplyMapper extends BaseMapper<Supply> {

    void compare();
}
