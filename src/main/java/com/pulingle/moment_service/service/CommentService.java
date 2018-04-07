package com.pulingle.moment_service.service;

import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.entity.Comment;

/**
 * Created by @杨健 on 2018/4/5 16:05
 *
 * @Des: 评论服务
 */

public interface CommentService {
    /**
    * @param: momentId(动态Id),userId(用户id),status(是否点赞,1点赞,0取消点赞)
    * @return: RespondBody
    * @Des: 动态点赞服务
    */
    RespondBody thumbing(String TUL,long userId,int status);

    /**
    * @param: comment
    * @return: RespondBody
    * @Des: 评论服务
    */
    RespondBody comment(Comment comment);

    /**
    * @param: commentList(Redis中评论ID列表的键值名)
    * @return: RespondBody
    * @Des: 根据commentList查询某个动态的所有评论信息
    */
    RespondBody queryCommentByCIL(String commentList);
}
