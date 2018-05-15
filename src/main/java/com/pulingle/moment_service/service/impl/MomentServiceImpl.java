package com.pulingle.moment_service.service.impl;

import com.alibaba.fastjson.JSON;
import com.pulingle.moment_service.domain.dto.MomentDTO;
import com.pulingle.moment_service.domain.dto.RespondBody;
import com.pulingle.moment_service.domain.dto.UserIdListDTO;
import com.pulingle.moment_service.domain.entity.Moment;
import com.pulingle.moment_service.domain.entity.UserBasicInfo;
import com.pulingle.moment_service.domain.entity.User_info;
import com.pulingle.moment_service.feign.OutUserInfoFeign;
import com.pulingle.moment_service.mapper.MomentMapper;
import com.pulingle.moment_service.service.MomentService;
import com.pulingle.moment_service.utils.RespondBuilder;

import com.pulingle.moment_service.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.*;

/**
 * Created by @杨健 on 2018/4/3 12:58
 *
 * @Des: 动态服务实现类
 */
@Service("momentService")
public class MomentServiceImpl implements MomentService {
    //点赞用户Id列表字符前缀
    String TUL_String = "TUL";

    //Redis中每个动态的评论ID列表键值名前缀
    private final String REDIS_COMMENT_ID_LIST = "CIL";

    //UserBasicInfo类的键值名前缀
    private final String USER_BASIC_INFO = "UBI";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OutUserInfoFeign outUserInfoFeign;

    @Resource
    private MomentMapper momentMapper;

    @Override
    public RespondBody publishMoment(MomentDTO momentDTO) {
        RespondBody respondBody;
        try {
            Moment moment = new Moment();
            moment.setUserId(momentDTO.getUserId());
            moment.setContent(momentDTO.getContent());
            moment.setPrivacyLev(momentDTO.getPrivacyLev());
            String pictureList = StringUtils.join(momentDTO.getPictureList(), ",");
            moment.setPictureList(pictureList);
            Date date = new Date();
            moment.setCreateTime(date);
            momentMapper.insert(moment);
            momentMapper.updateTULandCIL(moment.getMomentId(), TUL_String + moment.getMomentId(), REDIS_COMMENT_ID_LIST + moment.getMomentId());

            respondBody = RespondBuilder.buildNormalResponse("momentId:" + moment.getMomentId());
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryFriendMomentByUserId(MomentDTO momentDTO) {
        RespondBody respondBody;
        try {
            if (momentDTO.getPageSize() == 0 || momentDTO.getCurrentPage() == 0)
                return RespondBuilder.buildErrorResponse("pageSize或currentPage不能为0");
            //通过user-service获取好友Id列表
            UserIdListDTO userIdListDTO = new UserIdListDTO();
            userIdListDTO.setUserId(momentDTO.getUserId());
            RespondBody userFriendListBody = outUserInfoFeign.getFriendList(userIdListDTO);
            if (userFriendListBody.getStatus().equals("0"))
                return RespondBuilder.buildNormalResponse("暂无好友");
            List<String> idList = (List<String>) userFriendListBody.getData();
            //计算分页查询的条件
            long recordNum = momentMapper.countFriendMoments(idList);
            double d = (double) recordNum / (double) momentDTO.getPageSize();
            long pageNum = (long) Math.ceil(d);
            int offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
            //根据Id列表查询符合的动态信息
            List<Map> momentList = momentMapper.queryFriendMoments(idList, offset, momentDTO.getPageSize());
            //如果有数据则查询对应用户信息
            if(momentList.size()!=0) {
                //根据查询出来的动态，获取显示的用户Id列表
                Set<String> newIdSet = new HashSet<>();
                for (Map map : momentList) {
                    newIdSet.add(String.valueOf(map.get("userId")));
                }
                //通过user-service获取对应的用户基本信息
                List<String> newIdList = new ArrayList<>(newIdSet);
                UserIdListDTO newUserIdListDTO = new UserIdListDTO();
                newUserIdListDTO.setIdList(newIdList);
                RespondBody newUserInfoBody = outUserInfoFeign.getUserBasicInfoForMoment(newUserIdListDTO);
                if (newUserInfoBody.getStatus().equals("0"))
                    return RespondBuilder.buildErrorResponse("获取用户信息失败");
                //用户信息与动态合并
                Map userMap = (Map) newUserInfoBody.getData();
                for (Map map : momentList) {
                    //图片列表的字符串构建成数组
                    String pictureResult = String.valueOf(map.get("pictureList"));
                    String pictureList[] = pictureResult.split(",");
                    map.replace("pictureList", pictureList);
                    //获取点赞数
                    String TUl = String.valueOf(map.get("thumbUsersList"));
                    if (stringRedisTemplate.hasKey(TUl)) {
                        long thumbNum = stringRedisTemplate.opsForSet().size(TUl);
                        map.put("thumbNum", thumbNum);
                    } else
                        map.put("thumbNum", 0);
                    //获取评论数
                    String CIL = String.valueOf(map.get("commentList"));
                    if (stringRedisTemplate.hasKey(CIL)) {
                        long commentNum = stringRedisTemplate.opsForSet().size(CIL);
                        map.put("commentNum", commentNum);
                    } else
                        map.put("commentNum", 0);
                    //组合用户基本信息
                    int userId = (int) map.get("userId");
                    Map basicMap = (Map) userMap.get(USER_BASIC_INFO + userId);
                    if (basicMap.get("nickname") != null)
                        map.put("nickname", basicMap.get("nickname"));
                    if (basicMap.get("profilePictureUrl") != null)
                        map.put("profilePictureUrl", basicMap.get("profilePictureUrl"));
                    //检验用户是否对该动态已点赞
                    int isThumb;
                    String thumbUserList = String.valueOf(map.get("thumbUsersList"));
                    if (stringRedisTemplate.opsForSet().isMember(thumbUserList, String.valueOf(momentDTO.getUserId()))) {
                        isThumb = 1;
                    } else
                        isThumb = 0;
                    map.put("isThumb", isThumb);
                }
            }
            //重构返回消息载体
            Map data = new HashMap();
            data.put("currentPage", momentDTO.getCurrentPage());
            data.put("pageSize", momentDTO.getPageSize());
            data.put("pageNum", pageNum);
            data.put("resultList", momentList);
            respondBody = RespondBuilder.buildNormalResponse(data);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryRecommendMoments(MomentDTO momentDTO) {
        RespondBody respondBody;
        try {
            if (momentDTO.getPageSize() == 0 || momentDTO.getCurrentPage() == 0)
                return RespondBuilder.buildErrorResponse("pageSize或currentPage不能为0");
            //计算分页查询的条件
            long recordNum = momentMapper.countMomentByUserId(momentDTO.getUserId());
            double d = (double) recordNum / (double) momentDTO.getPageSize();
            long pageNum = (long) Math.ceil(d);
            int offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
            //通过user-service获取好友Id列表
            UserIdListDTO userIdListDTO = new UserIdListDTO();
            userIdListDTO.setUserId(momentDTO.getUserId());
            RespondBody unionFriendBody = outUserInfoFeign.getUnionFriend(userIdListDTO);
            if (unionFriendBody.getStatus().equals("0"))
                return RespondBuilder.buildErrorResponse("获取推荐用户列表失败");
            Map respondMap = (Map) unionFriendBody.getData();
            List<String> idList = (List<String>) respondMap.get("idList");
            //根据Id列表查询符合的动态信息
            List<Map> momentList = momentMapper.queryRecommendMoments(idList, offset, momentDTO.getPageSize());
            //用户信息与动态合并
            for (Map map : momentList) {
                //图片列表的字符串构建成数组
                String pictureResult = String.valueOf(map.get("pictureList"));
                String pictureList[] = pictureResult.split(",");
                map.replace("pictureList", pictureList);
                //获取点赞数
                String TUl = String.valueOf(map.get("thumbUsersList"));
                if (stringRedisTemplate.hasKey(TUl)) {
                    long thumbNum = stringRedisTemplate.opsForSet().size(TUl);
                    map.put("thumbNum", thumbNum);
                } else
                    map.put("thumbNum", 0);
                //获取评论数
                String CIL = String.valueOf(map.get("commentList"));
                if (stringRedisTemplate.hasKey(CIL)) {
                    long commentNum = stringRedisTemplate.opsForSet().size(CIL);
                    map.put("commentNum", commentNum);
                } else
                    map.put("commentNum", 0);
                //组合用户基本信息
                int userId = (int) map.get("userId");
                Map basicMap = (Map) respondMap.get(USER_BASIC_INFO + userId);
                if (basicMap.get("nickname") != null)
                    map.put("nickname", basicMap.get("nickname"));
                if (basicMap.get("profilePictureUrl") != null)
                    map.put("profilePictureUrl", basicMap.get("profilePictureUrl"));
                //检验用户是否对该动态已点赞
                int isThumb;
                String thumbUserList = String.valueOf(map.get("thumbUsersList"));
                if (stringRedisTemplate.opsForSet().isMember(thumbUserList, String.valueOf(momentDTO.getUserId()))) {
                    isThumb = 1;
                } else
                    isThumb = 0;
                map.put("isThumb", isThumb);
            }
            //重构返回消息载体
            Map data = new HashMap();
            data.put("currentPage", momentDTO.getCurrentPage());
            data.put("pageSize", momentDTO.getPageSize());
            data.put("pageNum", pageNum);
            data.put("resultList", momentList);
            respondBody = RespondBuilder.buildNormalResponse(data);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }

    @Override
    public RespondBody queryMomentsByUserId(MomentDTO momentDTO, HttpServletRequest request) {
        RespondBody respondBody=new RespondBody();
        //返回数据
        Map data = new HashMap();
        //用户身份，0不是好友，1是好友，2是自己
        int identity=0;
        //如果有token，则记录该token的userId,没有则为0
        long loginUserId = 0;
        //分页查询的条件
        long recordNum;
        double d;
        long pageNum;
        int offset;
        List<Map> resultList;
        try {
            respondBody.setStatus("1");
            respondBody.setMsg("0000");
            //根据是否登录，是否好友按权限查询某用户动态信息
            //没登录，则为查询公开动态
            if (request.getHeader("token") == null || request.getHeader("token").equals("")) {
                respondBody.setStatus("2");
                respondBody.setMsg("用户未登录");
                //计算分页查询的条件
                recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 1);
                d = (double) recordNum / (double) momentDTO.getPageSize();
                pageNum = (long) Math.ceil(d);
                offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 1);
            } else {
                try {
                    //解析token获取用户信息
                    String token = request.getHeader("token");
                    Claims claims = TokenUtil.parseJWT(token);
                    String subject = claims.getSubject();
                    User_info user_info = JSON.parseObject(subject, User_info.class);
                    loginUserId = user_info.getUser_id();
                    //如果查询的用户ID与登录的用户ID相等，则查询该用户全部动态
                    if (user_info.getUser_id() == momentDTO.getUserId()) {
                        //身份为自己
                        identity = 2;
                        //计算分页查询的条件
                        recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 4);
                        d = (double) recordNum / (double) momentDTO.getPageSize();
                        pageNum = (long) Math.ceil(d);
                        offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                        resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 4);
                    } else {
                        //通过Feign查询是否为好友
                        //封装调用接口参数
                        User_info user_info1 = new User_info();
                        user_info1.setUser_id(momentDTO.getUserId());
                        user_info1.setFriends_list(user_info.getFriends_list());
                        RespondBody result = outUserInfoFeign.isFriend(user_info1);
                        if (result.getStatus().equals("0")) {
                            //计算分页查询的条件
                            recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 1);
                            d = (double) recordNum / (double) momentDTO.getPageSize();
                            pageNum = (long) Math.ceil(d);
                            offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                            resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 1);
                        } else {
                            int flag = (Integer) result.getData();
                            //是朋友
                            if (flag == 1) {
                                //身份为好友
                                identity = 1;
                                //计算分页查询的条件
                                recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 1);
                                d = (double) recordNum / (double) momentDTO.getPageSize();
                                pageNum = (long) Math.ceil(d);
                                offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                                resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 3);
                            }
                            //不是朋友
                            else {
                                //身份不是好友
                                identity = 0;
                                //计算分页查询的条件
                                recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 1);
                                d = (double) recordNum / (double) momentDTO.getPageSize();
                                pageNum = (long) Math.ceil(d);
                                offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                                resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 1);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();

                    respondBody.setStatus("2");
                    respondBody.setMsg("用户未登录");
                    //身份不是好友
                    identity = 0;
                    //计算分页查询的条件
                    recordNum = momentMapper.countMomentByUserIdForPrivacy(momentDTO.getUserId(), 1);
                    d = (double) recordNum / (double) momentDTO.getPageSize();
                    pageNum = (long) Math.ceil(d);
                    offset = (momentDTO.getCurrentPage() - 1) * momentDTO.getPageSize();
                    resultList = momentMapper.queryMomentsByUserId(momentDTO.getUserId(), offset, momentDTO.getPageSize(), 1);
                }
            }
            //返回数据处理
            for (Map map : resultList) {
                //图片列表的字符串构建成数组
                String pictureResult = String.valueOf(map.get("pictureList"));
                String pictureList[] = pictureResult.split(",");
                map.replace("pictureList", pictureList);
                //获取点赞数
                String TUl = String.valueOf(map.get("thumbUsersList"));
                if (stringRedisTemplate.hasKey(TUl)) {
                    long thumbNum = stringRedisTemplate.opsForSet().size(TUl);
                    map.put("thumbNum", thumbNum);
                } else
                    map.put("thumbNum", 0);
                //获取评论数
                String CIL = String.valueOf(map.get("commentList"));
                if (stringRedisTemplate.hasKey(CIL)) {
                    long commentNum = stringRedisTemplate.opsForSet().size(CIL);
                    map.put("commentNum", commentNum);
                } else
                    map.put("commentNum", 0);
                //检验用户是否对该动态已点赞
                //是否登录了,登录了显示点赞状态，否则默认为没点赞
                if (loginUserId != 0) {
                    int isThumb;
                    String thumbUserList = String.valueOf(map.get("thumbUsersList"));
                    if (stringRedisTemplate.opsForSet().isMember(thumbUserList, String.valueOf(loginUserId))) {
                        isThumb = 1;
                    } else
                        isThumb = 0;
                    map.put("isThumb", isThumb);
                } else
                    map.put("isThumb", 0);
            }
            //通过Feign调用接口获取该用户信息
            UserBasicInfo userBasicInfo = new UserBasicInfo();
            userBasicInfo.setUserId(momentDTO.getUserId());
            RespondBody respondBody1 = outUserInfoFeign.getUserInfo(userBasicInfo);
            Map userInfo;
            if (respondBody1.getStatus().equals("1"))
                userInfo = (Map) respondBody1.getData();
            else
                userInfo = null;
            userInfo.put("identity",identity);
            if(identity==2) {
                //返回发布的动态数
                userInfo.put("momentNum", recordNum);
                //通过Feign获取好友数
                UserBasicInfo friendUser=new UserBasicInfo();
                friendUser.setUserId(momentDTO.getUserId());
                RespondBody friendNumRespond=outUserInfoFeign.getFriendAmount(friendUser);
                if(friendNumRespond.getStatus().equals("1"))
                    userInfo.put("friendNum",friendNumRespond.getData());
                else
                    userInfo.put("friendNum",0);
            }
            data.put("currentPage", momentDTO.getCurrentPage());
            data.put("pageSize", momentDTO.getPageSize());
            data.put("pageNum", pageNum);
            data.put("resultList", resultList);
            data.put("userInfo", userInfo);
            respondBody.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            respondBody = RespondBuilder.buildErrorResponse(e.getMessage());
        }
        return respondBody;
    }


}
