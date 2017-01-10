package com.jinlong.im.model;

import android.content.Context;

import com.jinlong.im.model.bean.UserInfo;
import com.jinlong.im.model.dao.UserAccountDao;
import com.jinlong.im.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据模型层全局类
 * Created by Administrator on 2016/10/26.
 */

public class Model {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();

    private static Model model = new Model();  //创建对象
    private UserAccountDao userAccountDao;
    private DBManager dbManager;

    private Model(){   //私有化构造
    }
    public static Model getInstance(){  //获取单例对象
        return model;
    }
    //初始化的方法
    public void init(Context contect){
        mContext = contect;
        //创建用户账号数据库的操作类对象
        userAccountDao = new UserAccountDao(mContext);
        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }
    //获取全局线程池对象
    public ExecutorService getGlobalThreadPool(){
        return  executors;
    }
    //用户登录成功后的处理方法
    public void loginSuccess(UserInfo account) {
        if(account == null){ //校验
            return;
        }
        if(dbManager != null){
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getName());
    }
    public DBManager getDbManager(){
        return dbManager;
    }
    //获取用户账号数据库的操作类对象
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }
}
