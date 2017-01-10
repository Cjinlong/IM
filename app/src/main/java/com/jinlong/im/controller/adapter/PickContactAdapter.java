package com.jinlong.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinlong.im.R;
import com.jinlong.im.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择联系人的页面适配器
 * Created by Administrator on 2016/12/3.
 */

public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks = new ArrayList<>();
    private List<String> mExistMembers = new ArrayList<>(); //保存群中已经存在的成员集合
    public PickContactAdapter(Context context , List<PickContactInfo> picks , List<String> existMembers){
        mContext = context;
        if(picks != null && picks.size() >= 0){
            mPicks.clear();
            mPicks.addAll(picks);
        }
        mExistMembers.clear();
        mExistMembers.addAll(existMembers);
    }
    @Override
    public int getCount() {
        return mPicks == null ? 0 : mPicks.size();
    }
    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
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
            convertView = View.inflate(mContext , R.layout.item_pick , null);
            holder.cb_pick = (CheckBox) convertView.findViewById(R.id.cb_pick);
            holder.tv_pick_name = (TextView) convertView.findViewById(R.id.tv_pick_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        PickContactInfo pickContactInfo = mPicks.get(position);
        holder.tv_pick_name.setText(pickContactInfo.getUser().getName());
        holder.cb_pick.setChecked(pickContactInfo.isChecked());
        //判断
        if(mExistMembers.contains(pickContactInfo.getUser().getHxid())){
            holder.cb_pick.setChecked(true);
            pickContactInfo.setIsChecked(true);
        }
        return convertView;
    }

    public List<String> getPickContacts() {
        //获取到已经选择的联系人
        List<String> picks = new ArrayList<>();
        for(PickContactInfo pick : mPicks){
            //判断是否选中
            if(pick.isChecked()){
                picks.add(pick.getUser().getName());
            }
        }
        return picks;
    }

    private class ViewHolder{
        private CheckBox cb_pick;
        private TextView tv_pick_name;
    }
}
