package com.elitecrm.rcclient.entity;

/**
 * Created by Loriling on 2017/2/9.
 */

public class Client extends User {
    private String loginId;
    private String targetId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
