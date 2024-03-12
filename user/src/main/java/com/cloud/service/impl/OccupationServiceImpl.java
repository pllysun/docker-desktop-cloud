package com.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.mapper.OccupationMapper;
import com.cloud.service.OccupationService;
import com.cloud.entity.Occupation;
import org.springframework.stereotype.Service;

@Service
public class OccupationServiceImpl extends ServiceImpl<OccupationMapper,Occupation> implements OccupationService {

}
