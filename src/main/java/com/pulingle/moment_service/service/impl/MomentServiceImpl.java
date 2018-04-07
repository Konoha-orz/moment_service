package com.pulingle.moment_service.service.impl;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.mapper.MomentMapper;
import com.pulingle.moment_service.service.MomentService;
import com.pulingle.moment_service.utils.RespondBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.*;

/**
 * Created by @杨健 on 2018/4/3 12:58
 *
 * @Des: 动态服务实现类
 */
@Service("momentService")
public class MomentServiceImpl implements MomentService {
    //点赞用户Id列表字符前缀
    String TUL_String="TUL";

    //Redis中每个动态的评论ID列表键值名前缀
    private final String REDIS_COMMENT_ID_LIST="CIL";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MomentMapper momentMapper;

    @Override
    public RespondBody publishMoment(Moment moment) {
        RespondBody respondBody;
        try{
            Date date=new Date();
            moment.setCreateTime(date);
            momentMapper.insert(moment);
            momentMapper.updateTULandCIL(moment.getMomentId(),TUL_String+moment.getMomentId(),REDIS_COMMENT_ID_LIST+moment.getMomentId());

            respondBody= RespondBuilder.buildNormalResponse("momentId:"+moment.getMomentId());
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryMomentByUserId(long userId) {
        RespondBody respondBody;
        try{
            List<Map> resultList=momentMapper.queryMomentsByUserId(userId);
            //返回数据处理
            for(Map map:resultList){
                //图片列表的字符串构建成数组
                String pictureResult=String.valueOf(map.get("pictureList"));
                String pictureList []=pictureResult.split(",");
                map.replace("pictureList",pictureList);
                //获取点赞数
                String TUl=String.valueOf(map.get("thumbUsersList"));
                if(stringRedisTemplate.hasKey(TUl)){
                    long thumbNum=stringRedisTemplate.opsForSet().size(TUl);
                    map.put("thumbNum",thumbNum);
                }else
                    map.put("thumbNum",0);
                //获取评论数
                String CIL=String.valueOf(map.get("commentList"));
                if(stringRedisTemplate.hasKey(CIL)){
                    long commentNum=stringRedisTemplate.opsForSet().size(CIL);
                    map.put("commentNum",commentNum);
                }else
                    map.put("commentNum",0);
            }
            respondBody=RespondBuilder.buildNormalResponse(resultList);
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
