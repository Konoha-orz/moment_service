package com.pulingle.moment_service.mapper;

import com.pulingle.moment_service.domain.entity.Moment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by @杨健 on 2018/4/3 12:21
 *
 * @Des: 动态Mapper
 */
@Component("momentMapper")
public interface MomentMapper {
    /**
    * @param: moment
    * @return: int
    * @Des: 插入动态信息
    */
    int insert(Moment moment);

    /**
    * @param: userId 用户Id
    * @return: List<Map> 用户动态信息列表
    * @Des: 根据用户ID获取个人所有动态信息
    */
    List<Map> queryMomentsByUserId(long userId);

    /**
    * @param: momentId(动态ID),TUl（点赞用户id列表值）
    * @return: int
    * @Des: 更新动态表的点赞用户id列表值（TUl）
    */
    int updateTULandCIL(@Param("momentId") long momentId,@Param("TUL") String TUl,@Param("CIL") String CIL);

}
