package com.pulingle.moment_service.service.impl;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.mapper.MomentMapper;
import com.pulingle.moment_service.service.MomentService;
import com.pulingle.moment_service.utils.RespondBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by @杨健 on 2018/4/3 12:58
 *
 * @Des: 动态服务实现类
 */
@Service("momentService")
public class MomentServiceImpl implements MomentService {

    @Resource
    private MomentMapper momentMapper;

    @Override
    public RespondBody publishMoment(Moment moment) {
        RespondBody respondBody;
        try{
            Date date=new Date();
            moment.setCreateTime(date);
            momentMapper.insert(moment);
            respondBody= RespondBuilder.buildNormalResponse(moment.getMomentId());
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
