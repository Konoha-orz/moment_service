package com.pulingle.moment_service.domain.dto;

import java.io.Serializable;

/**
 * Created by @杨健 on 2018/4/7 18:56
 *
 * @Des: 用户基础信息传说对象
 */

public class BasicUserInfoDTO implements Serializable {
    private String userId;
    private String nickname;
    private String profilePictureUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
