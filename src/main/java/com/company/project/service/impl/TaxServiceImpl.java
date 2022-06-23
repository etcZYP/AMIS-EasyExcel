package com.company.project.service.impl;

import com.company.project.entity.Tax;
import com.company.project.mapper.TaxMapper;
import com.company.project.service.TaxService;
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
public class TaxServiceImpl extends ServiceImpl<TaxMapper, Tax> implements TaxService {

}
