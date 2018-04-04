package com.pulingle.moment_service.mapper;

import com.pulingle.moment_service.domain.entity.Moment;
import org.springframework.stereotype.Component;

/**
 * Created by @杨健 on 2018/4/3 12:21
 *
 * @Des: 动态Mapper
 */
@Component("momentMapper")
public interface MomentMapper {
    int insert(Moment moment);
}
