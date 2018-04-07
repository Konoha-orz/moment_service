package com.pulingle.moment_service.service.impl;

import com.pulingle.moment_service.domain.dto.BasicUserInfoDTO;
import com.pulingle.moment_service.domain.dto.RespondBody;

import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import com.pulingle.moment_service.domain.entity.Comment;
import com.pulingle.moment_service.feign.OutUserInfoFeign;
import com.pulingle.moment_service.mapper.CommentMapper;
import com.pulingle.moment_service.utils.RespondBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by @杨健 on 2018/4/5 16:09
 *
 * @Des:
 */
@Service("commentService")
public class CommentServiceImpl implements com.pulingle.moment_service.service.CommentService {

    //Redis中每个动态的评论ID列表键值名前缀
    private final String REDIS_COMMENT_ID_LIST="CIL";

    @Resource
    private CommentMapper commentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OutUserInfoFeign outUserInfoFeign;

    @Override
    public RespondBody thumbing(String tulStr, long userId, int status) {
        try {
            if (status == 1) {
                stringRedisTemplate.opsForSet().add(tulStr, String.valueOf(userId));
                return RespondBuilder.buildNormalResponse("点赞成功");
            }else if (status==0){
                stringRedisTemplate.opsForSet().remove(tulStr,String.valueOf(userId));
                return RespondBuilder.buildNormalResponse("取消点赞成功");
            }else
                return RespondBuilder.buildErrorResponse("status值错误") ;
        } catch (Exception e) {
            e.printStackTrace();
            return RespondBuilder.buildErrorResponse(e.getMessage());
        }
    }

    @Override
    public RespondBody comment(Comment comment) {
        RespondBody respondBody;
        try{
            Date date=new Date();
            comment.setCreateTime(date);
            commentMapper.insert(comment);
            String redisKey=REDIS_COMMENT_ID_LIST+comment.getMomentId();
            String redisValue=String.valueOf(comment.getCommentId());
            stringRedisTemplate.opsForSet().add(redisKey,redisValue);
            respondBody=RespondBuilder.buildNormalResponse("评论成功，commentId:"+comment.getCommentId());
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryCommentByCIL(String commentList) {
        RespondBody respondBody;
        try{
            if(!stringRedisTemplate.hasKey(commentList))
                return RespondBuilder.buildErrorResponse("该动态没有评论");
            Set<String> redisSet=stringRedisTemplate.opsForSet().members(commentList);
            List<String> commentIdList=new ArrayList<>(redisSet);
            List<Map> resultList=commentMapper.queryCommentByCIL(commentIdList);
            //获取评论用户Id列表
            List<String> userIdList=new ArrayList<>();
            for (Map map:resultList){
                userIdList.add(map.get("userId").toString());
            }
            //调用用户服务中的获取用户基础信息接口
            UserIdListDTO userIdListDTO=new UserIdListDTO();
            userIdListDTO.setIdList(userIdList);
            RespondBody userInfoBody=outUserInfoFeign.getUserBasicInfo(userIdListDTO);
            List<Map> userInfoResultList=(List<Map>) userInfoBody.getData();
            //用户信息封装成Map
            Map userMap=new HashMap();
            for(Map map:userInfoResultList){
                BasicUserInfoDTO user=new BasicUserInfoDTO();
                user.setUserId(map.get("userId").toString());
                if(map.containsKey("nickname"))
                    user.setNickname(map.get("nickname").toString());
                if(map.containsKey("profilePictureUrl"))
                    user.setProfilePictureUrl(map.get("profilePictureUrl").toString());
                userMap.put(map.get("userId").toString(),user);
            }
            //重构返回消息体
            for(Map map:resultList){
                String userId=map.get("userId").toString();
                BasicUserInfoDTO user=(BasicUserInfoDTO) userMap.get(userId);
                if(user.getNickname()!=null)
                    map.put("nickname",user.getNickname());
                if(user.getProfilePictureUrl()!=null)
                    map.put("profilePictureUrl",user.getProfilePictureUrl());
            }
            respondBody=RespondBuilder.buildNormalResponse(resultList);
        }catch (Exception e){
            e.printStackTrace();
            respondBody=RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }
}
