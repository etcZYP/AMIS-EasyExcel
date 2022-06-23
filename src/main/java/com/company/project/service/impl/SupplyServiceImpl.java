package com.company.project.service.impl;

import com.company.project.entity.Supply;
import com.company.project.mapper.SupplyMapper;
import com.company.project.service.SupplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyp
 * @since 2022-05-24
 */
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {

    @Override
    public void compare() {
        this.baseMapper.compare();
    }
}
