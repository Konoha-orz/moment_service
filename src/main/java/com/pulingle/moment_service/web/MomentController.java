package com.pulingle.moment_service.web;

import com.pulingle.moment_service.domain.dto.MomentDTO;
import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.service.MomentService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by @杨健 on 2018/4/3 13:03
 *
 * @Des: 动态Controller
 */
@RestController
@RequestMapping(value = "moment")
public class MomentController {
    @Resource
    private MomentService momentService;

    /**
    * @param: moment
    * @return: RespondBody
    * @Des: 发布动态接口
    */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public RespondBody publish(@RequestBody MomentDTO momentDTO){
        return momentService.publishMoment(momentDTO);
    }

    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 根据userId查询用户所有动态信息
    */
//    @RequestMapping(value = "/queryMomentByUserId",method = RequestMethod.POST)
//    public RespondBody queryMomentByUserId(@RequestBody MomentDTO momentDTO){
//        return momentService.queryMomentByUserId(momentDTO);
//    }

    /**
     * @param: userId
     * @return: RespondBody
     * @Des: 根据userId查询用户所有好友动态信息
     */
    @RequestMapping(value = "/queryFriendMomentByUserId",method = RequestMethod.POST)
    public RespondBody queryFriendMomentByUserId(@RequestBody MomentDTO momentDTO){
        return momentService.queryFriendMomentByUserId(momentDTO);
    }

    /**
     * @param: momentDTO(用户ID,页面要求)
     * @return: RespondBody
     * @Des: 根据用户ID,页面要求，查询推荐用户的动态信息，包含隐私筛选
     */
    @RequestMapping(value = "/queryRecommendMoments",method = RequestMethod.POST)
    public RespondBody queryRecommendMoments(@RequestBody MomentDTO momentDTO){
        return momentService.queryRecommendMoments(momentDTO);
    }

    /**
     * @param: momentDTO (用户ID,页面要求)
     * @return: List<Map> 用户动态信息列表
     * @Des: 根据用户ID，以及是否登录账号，权限，获取个人的动态信息
     */
    @RequestMapping(value = "/queryMomentsByUserId",method = RequestMethod.POST)
    public RespondBody queryPublicMomentsByUserId(@RequestBody MomentDTO momentDTO, HttpServletRequest request){
        return momentService.queryMomentsByUserId(momentDTO,request);
    }
}
