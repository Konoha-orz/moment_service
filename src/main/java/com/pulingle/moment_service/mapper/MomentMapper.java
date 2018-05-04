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

//    /**
//    * @param: userId 用户Id，offset（第几条数据），size（显示数据数）
//    * @return: List<Map> 用户动态信息列表
//    * @Des: 根据用户ID获取个人所有动态信息
//    */
//    List<Map> queryMomentsByUserId(@Param("userId") long userId,@Param("offset")int offset,@Param("size")int size);

    /**
    * @param: momentId(动态ID),TUl（点赞用户id列表值）
    * @return: int
    * @Des: 更新动态表的点赞用户id列表值（TUl）
    */
    int updateTULandCIL(@Param("momentId") long momentId,@Param("TUL") String TUl,@Param("CIL") String CIL);

    /**
    * @param: userId用户Id
    * @return: int
    * @Des: 查询某用户所有动态记录数
    */
    long countMomentByUserId(@Param("userId")long userId);

    /**
    * @param: List<String>
    * @return: List<Map>
    * @Des: 根据Id列表获取所有好友动态信息,动态隐私程度为好友以上.小于3
    */
    List<Map> queryFriendMoments(@Param("idList") List<String> idList,@Param("offset")int offset,@Param("size")int size);

    /**
     * @param: List<String>
     * @return: List<Map>
     * @Des: 根据Id列表获取所有推荐用户动态信息,动态隐私程度为好友的好友以上.小于2
     */
    List<Map> queryRecommendMoments(@Param("idList") List<String> idList,@Param("offset")int offset,@Param("size")int size);

    /**
     * @param: userId 用户Id，offset（第几条数据），size（显示数据数）
     * @return: List<Map> 用户动态信息列表
     * @Des: 访客根据用户ID获取个人所有公开的动态信息
     */
    List<Map> queryMomentsByUserId(@Param("userId") long userId,@Param("offset")int offset,@Param("size")int size,@Param("privacyLev")int privacyLev);

    /**
     * @param: userId用户Id
     * @return: int
     * @Des: 查询某用户以及隐私程度所有动态记录数
     */
    long countMomentByUserIdForPrivacy(@Param("userId")long userId,@Param("privacyLevel")int privacyLevel);

}
