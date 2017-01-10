package com.jinlong.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.jinlong.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组列表的适配器
 * Created by Administrator on 2016/12/2.
 */
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mGroups = new ArrayList<>();
    public GroupListAdapter(Context context){
        mContext = context;
    }
    //刷新方法
    public void refresh(List<EMGroup> groups){
        if(groups != null && groups.size() >= 0){  //校验
            mGroups.clear();  //先清空数据、再添加数据，然后刷新页面
            mGroups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mGroups == null ? 0 : mGroups.size();
    }
    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1、创建或获取viewholder
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext , R.layout.item_grouplist , null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_grouplist_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //2、获取当前item数据
        EMGroup emGroup = mGroups.get(position);
        //3、显示数据
        holder.name.setText(emGroup.getGroupName());
        return convertView;   //4、返回数据
    }
    private class ViewHolder{
        TextView name;
    }
}
