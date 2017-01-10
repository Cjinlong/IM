package com.jinlong.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jinlong.im.R;
import com.jinlong.im.model.bean.InvationInfo;
import com.jinlong.im.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请信息页面的适配器
 * Created by Administrator on 2016/11/6.
 */

public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvationInfo> mInvitationInfos = new ArrayList<>();
    private OnInviteListener mOnInviteListener;
    private InvationInfo invationInfo;

    public InviteAdapter(Context context , OnInviteListener onInviteListener){
        mContext = context;
        mOnInviteListener = onInviteListener;
    }
    //刷新数据的方法
    public void refresh(List<InvationInfo> invationInfos){
        if(invationInfos != null && invationInfos.size() >= 0){
            mInvitationInfos.clear(); //先清空再添加
            mInvitationInfos.addAll(invationInfos);
            notifyDataSetChanged();   //刷新数据
        }
    }
    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }
    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1、获取或创建一个viewHolder
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext , R.layout.item_invite , null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_invite_name);
            holder.reason = (TextView) convertView.findViewById(R.id.tv_invite_reason);
            holder.accept = (Button) convertView.findViewById(R.id.bt_invite_accept);
            holder.reject = (Button) convertView.findViewById(R.id.bt_invite_reject);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //2、获取当前item数据
        invationInfo = mInvitationInfos.get(position);
        //3、显示当前item数据
        UserInfo user = invationInfo.getUser();
        if(user != null){  //联系人
            holder.name.setText(invationInfo.getUser().getName());
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            if(invationInfo.getStatus() == InvationInfo.InvitationStatus.NEW_INVITE){  //新的邀请
                if(invationInfo.getReason() == null){
                    holder.reason.setText("添加好友");
                }else {
                    holder.reason.setText(invationInfo.getReason());
                }
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
            }else if(invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT){ //接受邀请
                if(invationInfo.getReason() == null){
                    holder.reason.setText("接受邀请");
                }else {
                    holder.reason.setText(invationInfo.getReason());
                }
            }else if(invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){//邀请被接受
                if(invationInfo.getReason() == null){
                    holder.reason.setText("邀请被接受");
                }else {
                    holder.reason.setText(invationInfo.getReason());
                }
            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invationInfo);
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invationInfo);
                }
            });
        }else {            //群组
            //显示名称  邀请人
            holder.name.setText(invationInfo.getGroup().getInvatePerson());
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            //显示原因
            switch (invationInfo.getStatus()){
                case GROUP_APPLICATION_ACCEPTED:  //你的群申请已经被接受
                    holder.reason.setText("你的群申请已经被接受");
                    break;
                case GROUP_INVITE_ACCEPTED:       //你的群邀请已经被接受
                    holder.reason.setText("你的群邀请已经被接受");
                    break;
                case GROUP_APPLICATION_DECLINED: //你的群申请已经被拒绝
                    holder.reason.setText("你的群申请已经被拒绝");
                    break;
                case GROUP_INVITE_DECLINED:      //你的群邀请已经被拒绝
                    holder.reason.setText("你的群邀请已经被拒绝");
                    break;
                case NEW_GROUP_INVITE:           //收到群邀请
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteAccept(invationInfo);
                        }
                    });
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteReject(invationInfo);
                        }
                    });
                    holder.reason.setText("收到群邀请");
                    break;
                case NEW_GROUP_APPLICATION:     //收到群申请
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationAccept(invationInfo);
                        }
                    });
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationReject(invationInfo);
                        }
                    });
                    holder.reason.setText("收到群申请");
                    break;
                case GROUP_ACCEPT_INVITE:       //你接受了群邀请
                    holder.reason.setText("你接受了群邀请");
                    break;
                case GROUP_ACCEPT_APPLICATION:  //你接受了群申请加入
                    holder.reason.setText("你接受了群申请加入");
                    break;
            }
        }
        return convertView;   //4、返回view
    }
    private class ViewHolder{
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }
    public interface OnInviteListener{
        void onAccept(InvationInfo invationInfo);  //联系人接受按钮的点击事件
        void onReject(InvationInfo invationInfo);  //联系人拒绝按钮的点击事件

        void onInviteAccept(InvationInfo invationInfo);      //接受邀请按钮处理
        void onInviteReject(InvationInfo invationInfo);      //拒绝邀请
        void onApplicationAccept(InvationInfo invationInfo); //接受申请按钮处理
        void onApplicationReject(InvationInfo invationInfo); //拒绝申请
    }
}
