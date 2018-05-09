package com.pulingle.moment_service.service;


import com.pulingle.moment_service.domain.dto.RespondBody;

import java.util.List;

/**
 * Created by @杨健 on 2018/5/5 14:12
 *
 * @Des: Out提供动态服务
 */

public interface OutMomentService {
    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 根据UserId获取用户发布的动态数
    */
    RespondBody getMomentsNum(long userId);

    /**
     * @param: userId,num
     * @return: RespondBody
     * @Des: 获取用户num个好友近1天新发布动态的时间及好友ID
     */
    RespondBody queryFriendNewMomentTime(List<String> idList);
}
