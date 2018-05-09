package com.pulingle.moment_service.web;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import com.pulingle.moment_service.domain.entity.UserBasicInfo;
import com.pulingle.moment_service.service.OutMomentService;
import com.pulingle.moment_service.utils.RespondBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @杨健 on 2018/5/5 14:12
 *
 * @Des: Out动态服务Controller
 */
@RestController
@RequestMapping(value = "outMoment")
public class OutMomentController {
    @Resource
    private OutMomentService outMomentService;

    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 获取用户发布动态数
    */
    @RequestMapping(value = "/getMomentsNum",method = RequestMethod.POST)
    public RespondBody getMomentsNum(@RequestBody UserBasicInfo userBasicInfo){
        return outMomentService.getMomentsNum(userBasicInfo.getUserId());
    }

    /**
     * @param: userId,num
     * @return: RespondBody
     * @Des: 获取用户num个好友近1天新发布动态的时间及好友ID
     */
    @RequestMapping(value = "/queryFriendNewMomentTime",method = RequestMethod.POST)
    public RespondBody queryFriendNewMomentTime(@RequestBody UserIdListDTO userIdListDTO){
        return outMomentService.queryFriendNewMomentTime(userIdListDTO.getIdList());
    }
}
