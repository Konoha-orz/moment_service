package com.pulingle.moment_service.web;

import com.pulingle.moment_service.domain.dto.MomentDTO;
import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.dto.ThumbDTO;
import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import com.pulingle.moment_service.domain.entity.Comment;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.feign.OutUserInfoFeign;
import com.pulingle.moment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by @杨健 on 2018/4/5 16:45
 *
 * @Des: 评论服务接口
 */
@RestController
@RequestMapping(value = "comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
    * @param: thumbDTO(thumbUsersList,userId)
    * @return: RespondBody
    * @Des: 点赞接口
    */
    @RequestMapping(value = "/thumb",method = RequestMethod.POST)
    public RespondBody thumb(@RequestBody ThumbDTO thumbDTO){
        return commentService.thumbing(thumbDTO.getThumbUsersList(),thumbDTO.getUserId(),thumbDTO.getStatus());
    }

    /**
    * @param: comment(userId,momentId,content)
    * @return: RespondBody
    * @Des: 评论接口
    */
    @RequestMapping(value = "/remark",method = RequestMethod.POST)
    public RespondBody comment(@RequestBody Comment comment){
        return commentService.comment(comment);
    }

    /**
    * @param: commentList
    * @return: RespondBody
    * @Des: 根据commentList值查询评论信息
    */
    @RequestMapping(value = "/queryCommentByCIL",method = RequestMethod.POST)
    public RespondBody queryComment(@RequestBody MomentDTO momentDTO){
        return commentService.queryCommentByCIL(momentDTO);
    }


}
