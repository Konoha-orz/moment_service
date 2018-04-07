package com.pulingle.moment_service.domain.dto;

import java.io.Serializable;

/**
 * Created by @杨健 on 2018/4/5 16:47
 *
 * @Des: 点赞传输实体类
 */

public class ThumbDTO implements Serializable{
    /**
     * Redis中的点赞用户Id列表 TUL
     */
    private String thumbUsersList;

    /**
     * 点赞用户id
     */
    private long userId;

    /**
     * 是否点赞，1点赞，0取消点赞
     */
    private int status;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThumbUsersList() {
        return thumbUsersList;
    }

    public void setThumbUsersList(String thumbUsersList) {
        this.thumbUsersList = thumbUsersList;
    }

    @Override
    public String toString() {
        return "ThumbDTO{" +
                "thumbUsersList='" + thumbUsersList + '\'' +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
