package com.jinlong.im.model.bean;

/**
 * 选择联系人的bean类
 * Created by Administrator on 2016/12/3.
 */

public class PickContactInfo {
    private UserInfo user;  //联系人
    private boolean isChecked; //是否被选中

    public PickContactInfo(UserInfo user, boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }
    public PickContactInfo() {    }

    public UserInfo getUser() {
        return user;
    }
    public void setUser(UserInfo user) {
        this.user = user;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }
}
