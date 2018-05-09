package com.pulingle.moment_service.service;

import com.pulingle.moment_service.domain.dto.MomentDTO;
import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import java.util.List;

/**
 * Created by @杨健 on 2018/4/3 12:33
 *
 * @Des: 动态服务
 */

public interface MomentService {

    /**
    * @param: Moment
    * @return: RespondBody
    * @Des: 动态发布
    */
    RespondBody publishMoment(MomentDTO momentDTO);


    /**
     * @param: momentDTO(用户Id,页面要求)
     * @return: RespondBody
     * @Des: 根据用户id,页面要求,查询所有该用户好友动态信息,包含隐私筛选
     */
    RespondBody queryFriendMomentByUserId(MomentDTO momentDTO);

    /**
    * @param: momentDTO(用户ID,页面要求)
    * @return: RespondBody
    * @Des: 根据用户ID,页面要求，查询推荐用户的动态信息，包含隐私筛选
    */
    RespondBody queryRecommendMoments(MomentDTO momentDTO);

    /**
     * @param: momentDTO (用户ID,页面要求)
     * @return: List<Map> 用户动态信息列表
     * @Des: 根据用户ID，以及是否登录账号，权限，获取个人的动态信息
     */
    RespondBody queryMomentsByUserId(MomentDTO momentDTO, HttpServletRequest request);


}
