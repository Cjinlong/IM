package com.jinlong.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinlong.im.R;
import com.jinlong.im.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 群详情的适配器
 * Created by Administrator on 2016/12/4.
 */

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;  //是否允许添加或者删除群成员
    private List<UserInfo> mUsers = new ArrayList<>();
    private boolean mIsDeleteModel;  //删除模式
    private OnGroupDetailListener mOnGroupDetailListener;
    public GroupDetailAdapter(Context context , boolean isCanModify , OnGroupDetailListener onGroupDetailListener){
        mContext = context;
        mIsCanModify = isCanModify;
        mOnGroupDetailListener = onGroupDetailListener;
    }

    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }

    //刷新数据
    public void refresh(List<UserInfo> users){
        if(users != null && users.size() >= 0){
            mUsers.clear();
            //添加 + 、-
            initUsers();
            mUsers.addAll(0 , users);
        }
        notifyDataSetChanged();
    }
    private void initUsers() {
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");
        mUsers.add(delete);
        mUsers.add(0 , add);  //让 - 保持在 + 后面
    }

    @Override
    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }
    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext , R.layout.item_groupdetail , null);
            holder.photo = (ImageView) convertView.findViewById(R.id.iv_group_detail_photo);
            holder.delete = (ImageView) convertView.findViewById(R.id.iv_group_detail_delelte);
            holder.name = (TextView) convertView.findViewById(R.id.tv_group_detail_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //获取当前item数据
        final UserInfo userInfo = mUsers.get(position);
        if(mIsCanModify){   //群主或者开发了群权限
            //分类一、处理布局
            if(position == getCount() - 1){        //减号的处理
                if(mIsDeleteModel){  //删除模式
                    convertView.setVisibility(View.INVISIBLE);
                }else{
                    convertView.setVisibility(View.VISIBLE);
                    holder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else if(position == getCount() - 2){ //加号的处理
                if(mIsDeleteModel){  //删除模式
                    convertView.setVisibility(View.INVISIBLE);
                }else{
                    convertView.setVisibility(View.VISIBLE);
                    holder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else{                                //群成员
                convertView.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                if(mIsDeleteModel){
                    holder.delete.setVisibility(View.VISIBLE);
                }else{
                    holder.delete.setVisibility(View.GONE);
                }
            }

            //分类二、处理点击事件
            if(position == getCount() - 1){       //减号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {  //进入删除模式
                        if(!mIsDeleteModel){
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if(position == getCount() - 2){//加号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onAddMembers();
                    }
                });
            }else{
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onDeleteMember(userInfo);
                    }
                });
            }

        }else{  //普通的群成员
            if(position == getCount() - 1 || position == getCount() - 2){
                convertView.setVisibility(View.GONE);
            }else{
                convertView.setVisibility(View.VISIBLE);
                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                holder.delete.setVisibility(View.GONE);
            }

        }
        return convertView;
    }
    private class ViewHolder{
        private ImageView photo;
        private ImageView delete;
        private TextView name;
    }
    public interface OnGroupDetailListener{
        void onAddMembers();   //添加群成员
        void onDeleteMember(UserInfo user); //删除群成员
    }
}
