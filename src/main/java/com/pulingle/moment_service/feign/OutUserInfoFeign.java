package com.pulingle.moment_service.feign;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import com.pulingle.moment_service.domain.entity.UserBasicInfo;
import com.pulingle.moment_service.domain.entity.User_info;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by @杨健 on 2018/4/7 18:07
 *
 * @Des: user-service中的提供用户基础信息的接口
 */
@Component("outUserInfoFeign")
@FeignClient(name = "USER-SERVICE")
@RequestMapping(value = "outUserInfo")
public interface OutUserInfoFeign {

    @RequestMapping(value = "/getUserBasicInfo",method = RequestMethod.POST)
    public RespondBody getUserBasicInfo(@RequestBody UserIdListDTO userIdListDTO);

    @RequestMapping(value = "/getFriendBasicInfoList",method = RequestMethod.POST)
    public RespondBody getUserFriendList(@RequestBody UserIdListDTO userIdListDTO);

    @RequestMapping(value = "/getFriendInfoForMoment",method = RequestMethod.POST)
    public RespondBody getFriendInfoForMoment(@RequestBody UserIdListDTO userIdListDTO);

    /**
     * @param: userId
     * @return: RespondBody
     * @Des: 获取用户好友Id列表
     */
    @RequestMapping(value = "/getFriendList",method = RequestMethod.POST)
    public RespondBody getFriendList(@RequestBody UserIdListDTO userIdListDTO);

    /**
     * @param: idList(用户Id列表)
     * @return: RespondBody
     * @Des: 根据用户Id列表查询基本信息，用于动态服务
     */
    @RequestMapping(value = "/getUserBasicInfoForMoment",method = RequestMethod.POST)
    public RespondBody getUserBasicInfoForMoment(@RequestBody UserIdListDTO userIdListDTO);

    /**
     * @param: userId用户Id
     * @return: RespondBody
     * @Des: 推荐用户Id列表（好友的好友的集合作并差处理）
     */
    @RequestMapping(value = "/getUnionFriend",method = RequestMethod.POST)
    public RespondBody getUnionFriend(@RequestBody UserIdListDTO userIdListDTO);

    /**
     * @param: userId,friendList
     * @return: RespondBody
     * @Des: 判断是否为好友
     */
    @RequestMapping(value = "/isFriend",method = RequestMethod.POST)
    public RespondBody isFriend(@RequestBody User_info user_info);

    /**
     * @param: userId
     * @return: Map
     * @Des: 根据用户ID查询用户信息
     */
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public RespondBody getUserInfo(@RequestBody UserBasicInfo userBasicInfo);

    /**
     * @param调用接口的用户id
     * @return 返回体
     * 通过用户id查询用户的好友列表长度
     */
    @RequestMapping(value = "/getFriendAmount",method = RequestMethod.POST)
    public RespondBody getFriendAmount(@RequestBody UserBasicInfo user);
}
