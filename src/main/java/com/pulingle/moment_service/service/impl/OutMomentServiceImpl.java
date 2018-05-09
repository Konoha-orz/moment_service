package com.pulingle.moment_service.service.impl;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.mapper.MomentMapper;
import com.pulingle.moment_service.service.OutMomentService;
import com.pulingle.moment_service.utils.RespondBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by @杨健 on 2018/5/5 14:14
 *
 * @Des: Out提供动态服务实现类
 */
@Service("outMomentService")
public class OutMomentServiceImpl implements OutMomentService{

    @Resource
    private MomentMapper momentMapper;

    @Override
    public RespondBody getMomentsNum(long userId) {
        RespondBody respondBody;
        long recordNum=0;
        try{
            if(userId!=0) {
                recordNum = momentMapper.countMomentByUserId(userId);
                respondBody=RespondBuilder.buildNormalResponse(recordNum);
            }
            else
                respondBody= RespondBuilder.buildErrorResponse("UserId不能为0");
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryFriendNewMomentTime(List<String> idList) {
        RespondBody respondBody;
        try{
            if(idList.size()>0){
                List<Moment> resultList=momentMapper.queryFriendNewMomentTime(idList);
                if(resultList.size()>0){
                    LinkedHashMap linkedHashMap=new LinkedHashMap();
                    for(Moment moment:resultList){
                            if(moment.getUserId()!=0) {
                                if(!linkedHashMap.containsKey(String.valueOf(moment.getUserId()))) {
                                    //获取时间差信息
                                    Date momentDate= moment.getCreateTime();
                                    Date now=new Date();
                                    long second=(now.getTime()-momentDate.getTime())/1000;
                                    String time="";
                                    if(second>3600){
                                        time=String.valueOf(second/3600)+" 小时前";
                                    }else if(second>60){
                                        time=String.valueOf(second/60)+" 分钟前";
                                    }else {
                                        time=String.valueOf(second)+" 秒前";
                                    }
                                    Map map=new HashMap();
                                    map.put("userId",moment.getUserId());
                                    map.put("time",time);
                                    linkedHashMap.put(String.valueOf(moment.getUserId()), map);
                                }
                            }
                    }
                    respondBody=RespondBuilder.buildNormalResponse(linkedHashMap);
                }else
                    respondBody=RespondBuilder.buildNormalResponse(null);
            }else {
                respondBody=RespondBuilder.buildNormalResponse(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
