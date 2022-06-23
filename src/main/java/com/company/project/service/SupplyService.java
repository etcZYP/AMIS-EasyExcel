package com.company.project.service;

import com.company.project.entity.Supply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyp
 * @since 2022-05-24
 */
public interface SupplyService extends IService<Supply> {

    void compare();
}
