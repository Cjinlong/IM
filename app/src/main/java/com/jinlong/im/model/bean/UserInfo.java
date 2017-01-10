package com.jinlong.im.model.bean;

/**
 * 用户账号信息的bean类
 * Created by Administrator on 2016/10/26.
 */

public class UserInfo {
    private String name;  //用户名称
    private String hxid;  //环信id
    private String nick;  //昵称
    private String photo; //头像

    public UserInfo(){    }
    public UserInfo(String name){
        this.name = name;
        this.hxid = name;
        this.nick = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setHxid(String hxid) {
        this.hxid = hxid;
    }
    public String getHxid() {
        return hxid;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getNick() {
        return nick;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
