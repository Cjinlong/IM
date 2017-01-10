package com.jinlong.im.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.jinlong.im.model.bean.GroupInfo;
import com.jinlong.im.model.bean.InvationInfo;
import com.jinlong.im.model.bean.UserInfo;
import com.jinlong.im.utils.Constant;
import com.jinlong.im.utils.SpUtils;

/**
 * 全局事件监听类
 * Created by Administrator on 2016/10/28.
 */

public class EventListener{

    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context){
        mContext = context;
        //创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);
        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        //创建一个群信息变化监听
        EMClient.getInstance().groupManager().addGroupChangeListener(eMGroupChangeListener);
    }


    //群信息变化监听
    private final EMGroupChangeListener eMGroupChangeListener = new EMGroupChangeListener() {
        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName , groupId , inviter));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_INVITE);  //新的群邀请
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applicant, String reason) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName , groupId , applicant));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //群申请被接受
        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setGroup(new GroupInfo(groupName , groupId , accepter));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));

        }

        //群申请被拒绝
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName , groupId , decliner));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //邀请别人进群 被同意
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId , groupId , inviter));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //邀请别人进群 被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId , groupId , inviter));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName) {

        }
        //群被解散
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {

        }
        //群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            //1、数据更新  、 设置参数
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(inviteMessage);
            invitationInfo.setGroup(new GroupInfo(groupId , groupId , inviter));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //2、红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            //3、发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
    };

    //联系人变化监听
    private final EMContactListener emContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String hxid) {  //联系人增加后执行   , 数据更新
            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(hxid) , true);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        @Override
        public void onContactDeleted(String hxid) { //联系人删除后执行
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        @Override
        public void onContactInvited(String hxid, String reason) { //接收到联系人新邀请
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
        @Override
        public void onContactAgreed(String hxid) {  //别人同意了你的好友邀请
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
        @Override
        public void onContactRefused(String s) {//别人拒绝了你的好友邀请
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE , true);
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
