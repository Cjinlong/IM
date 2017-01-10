package com.jinlong.im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jinlong.im.model.dao.ContactTable;
import com.jinlong.im.model.dao.InviteTable;

/**
 * Created by Administrator on 2016/10/28.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.CREATE_TAB);   //创建联系人的表
        db.execSQL(InviteTable.CREATE_TAB);   //创建邀请信息的表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {   }
}
