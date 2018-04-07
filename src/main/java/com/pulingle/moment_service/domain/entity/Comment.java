package com.pulingle.moment_service.domain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by @杨健 on 2018/4/7 14:46
 *
 * @Des: 评论实体类
 */

public class Comment implements Serializable{
    /**
     * 评论Id
     */
    private long commentId;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 动态Id
     */
    private long momentId;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 内容
     */
    private String content;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMomentId() {
        return momentId;
    }

    public void setMomentId(long momentId) {
        this.momentId = momentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", momentId=" + momentId +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                '}';
    }
}
