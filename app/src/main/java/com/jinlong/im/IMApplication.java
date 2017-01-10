package com.jinlong.im;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.jinlong.im.model.Model;

/**
 * Created by Administrator on 2016/10/25.
 */

public class IMApplication extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);     //需要经过同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false); //需要经过同意后才能接受群邀请
        EaseUI.getInstance().init(this , options);

        Model.getInstance().init(this);  //初始化数据模型层类
        mContext = this;  //初始化全局上下文对象
    }
    public static Context getGlobalApplication(){  //获取全局上下文对象
        return mContext;
    }
}

