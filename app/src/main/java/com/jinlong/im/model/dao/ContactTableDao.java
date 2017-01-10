package com.jinlong.im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinlong.im.model.bean.UserInfo;
import com.jinlong.im.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人表的操作类
 * Created by Administrator on 2016/10/28.
 */

public class ContactTableDao {
    private DBHelper mHelper;
    public ContactTableDao(DBHelper helper){
        mHelper = helper;
    }
    // 获取所有联系人
    public List<UserInfo> getContacts() {
        SQLiteDatabase db = mHelper.getReadableDatabase(); // 获取数据库链接
        // 执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfo> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            users.add(userInfo);
        }
        cursor.close();  // 关闭资源
        return users;   // 返回数据
    }

    // 通过环信id获取联系人单个信息
    public UserInfo getContactByHx(String hxId) {
        if (hxId == null) {   //判断是否合法
            return null;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();  // 获取数据库链接
        // 执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
        }
        cursor.close();  // 关闭资源
        return userInfo;// 返回数据
    }
    // 通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByHx(List<String> hxIds) {
        if (hxIds == null || hxIds.size() <= 0) {
            return null;
        }
        List<UserInfo> contacts = new ArrayList<>();
        for (String hxid : hxIds) {  // 遍历hxIds，来查找
            UserInfo contact = getContactByHx(hxid);
            contacts.add(contact);
        }
        return contacts;  // 返回查询的数据
    }
    // 保存单个联系人
    public void saveContact(UserInfo user, boolean isMyContact) {
        if (user == null) {
            return;
        }
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID, user.getHxid());
        values.put(ContactTable.COL_NAME, user.getName());
        values.put(ContactTable.COL_NICK, user.getNick());
        values.put(ContactTable.COL_PHOTO, user.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);
        db.replace(ContactTable.TAB_NAME, null, values);
    }
    // 保存联系人信息
    public void saveContacts(List<UserInfo> contacts, boolean isMyContact) {
        if (contacts == null || contacts.size() <= 0) {
            return;
        }
        for (UserInfo contact : contacts) {
            saveContact(contact, isMyContact);
        }
    }
    // 删除联系人信息
    public void deleteContactByHxId(String hxId){
        if(hxId == null) {
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME,ContactTable.COL_HXID+"=?",new String[]{hxId});
    }
}
