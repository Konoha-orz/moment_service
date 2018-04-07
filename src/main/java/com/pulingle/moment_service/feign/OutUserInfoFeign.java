package com.pulingle.moment_service.feign;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by @杨健 on 2018/4/7 18:07
 *
 * @Des:
 */
@Component("outUserInfoFeign")
@FeignClient(name = "USER-SERVICE")
public interface OutUserInfoFeign {

    @RequestMapping(value = "/outUserInfo/getUserBasicInfo",method = RequestMethod.POST)
    public RespondBody getUserBasicInfo(@RequestBody UserIdListDTO userIdListDTO);


}
