package com.jinlong.im.model.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jinlong.im.model.dao.UserAccountTable;

/**
 * Created by Administrator on 2016/10/26.
 */

public class UserAccountDB extends SQLiteOpenHelper{
    //实现系统数据库构造方法
    public UserAccountDB(Context context) {
        super(context, "account.db", null , 1);
    }
    //数据库创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {       //注册数据库表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }
    //数据库更新时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    }
}
