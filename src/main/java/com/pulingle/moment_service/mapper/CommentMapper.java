package com.pulingle.moment_service.mapper;


import com.pulingle.moment_service.domain.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by @杨健 on 2018/4/7 14:45
 *
 * @Des: 评论Mapper
 */
@Component("commentMapper")
public interface CommentMapper {
    /**
    * @param: comment
    * @return: int
    * @Des: 插入一天评论
    */
    int insert(Comment comment);

    /**
     * @param: idList(评论ID列表)
     * @return: long
     * @Des: 根据评论ID列表统计评论数
     */
    long countCommentByCIL(@Param("idList") List<String> idList);

    /**
    * @param: idList(评论Id列表)
    * @return: List<Map>
    * @Des: 根据评论Id列表查询评论信息列表
    */
    List<Map> queryCommentByCIL(@Param("idList") List<String> idList,@Param("offset")int offset,@Param("size")int size);

}
