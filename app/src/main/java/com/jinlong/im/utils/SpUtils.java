package com.jinlong.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jinlong.im.IMApplication;

/**
 * Created by Administrator on 2016/10/28.
 */

public class SpUtils {
    public static final String IS_NEW_INVITE = "is_new_intite";  //新的邀请标记
    public static SpUtils instance = new SpUtils();
    private static SharedPreferences mSp;

    public SpUtils() {    }
    public static SpUtils getInstance(){  //单例
        if(mSp == null){
            mSp = IMApplication.getGlobalApplication().getSharedPreferences("im" , Context.MODE_PRIVATE);
        }
        return instance;
    }

    public void save(String key , Object value){  //保存
        if(value instanceof String){
            mSp.edit().putString(key , (String) value).commit();
        }else if(value instanceof Boolean){
            mSp.edit().putBoolean(key , (Boolean) value).commit();
        }else if(value instanceof Integer){
            mSp.edit().putInt(key , (Integer) value).commit();
        }
    }

    public String getString(String key , String defValue){     //获取String数据
        return mSp.getString(key , defValue);
    }
    public boolean getBoolean(String key , boolean defValue){//获取boolean数据
        return mSp.getBoolean(key , defValue);
    }
    public int getInt(String key , int defValue){             //获取int数据
        return mSp.getInt(key , defValue);
    }
}
