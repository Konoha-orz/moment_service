package com.pulingle.moment_service.web;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.service.MomentService;
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
    public RespondBody publish(@RequestBody Moment moment){
        return momentService.publishMoment(moment);
    }

    /**
    * @param: userId
    * @return: RespondBody
    * @Des: 根据userId查询用户所有动态信息
    */
    @RequestMapping(value = "/queryMomentByUserId",method = RequestMethod.POST)
    public RespondBody queryMomentByUserId(@RequestBody Moment moment){
        return momentService.queryMomentByUserId(moment.getUserId());
    }
}
