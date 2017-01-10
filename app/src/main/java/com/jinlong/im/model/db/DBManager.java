package com.jinlong.im.model.db;

import android.content.Context;

import com.jinlong.im.model.dao.ContactTable;
import com.jinlong.im.model.dao.ContactTableDao;
import com.jinlong.im.model.dao.InviteTableDao;

/**
 * 联系人和邀请信息表的操作类的管理类
 * Created by Administrator on 2016/10/28.
 */

public class DBManager {

    private final DBHelper dbHelper;
    private final ContactTableDao contactTableDao;
    private final InviteTableDao inviteTableDao;

    public DBManager(Context context , String name) {
        dbHelper = new DBHelper(context , name);  //创建数据库
        //创建数据库中两张表的操作类
        contactTableDao = new ContactTableDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }
    public ContactTableDao getContactTableDao(){  //获取联系人表的操作类
        return contactTableDao;
    }
    public InviteTableDao getInviteTableDao(){  //获取邀请信息表的操作类
        return inviteTableDao;
    }

    public void close() {  //关闭数据库的方法
        dbHelper.close();
    }
}
