package com.pulingle.moment_service.domain.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by @杨健 on 2018/4/10 16:51
 *
 * @Des: 查询动态传输对象，包含分页查询
 */

public class MomentDTO implements Serializable {
    /**
     * 用户Id
     */
    private long userId;

    /**
     * 当前页面数
     */
    private int currentPage;

    /**
     * 页面大小
     */
    private int pageSize;


    /**
     * 图片id列表
     */
    private String[] pictureList;

    /**
     * 内容
     */
    private String content;


    /**
     * 隐私程度
     */
    private int privacyLev;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getPictureList() {
        return pictureList;
    }

    public void setPictureList(String[] pictureList) {
        this.pictureList = pictureList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrivacyLev() {
        return privacyLev;
    }

    public void setPrivacyLev(int privacyLev) {
        this.privacyLev = privacyLev;
    }

    @Override
    public String toString() {
        return "MomentDTO{" +
                "userId=" + userId +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", pictureList=" + Arrays.toString(pictureList) +
                ", content='" + content + '\'' +
                ", privacyLev=" + privacyLev +
                '}';
    }
}
