package com.jinlong.im.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jinlong.im.R;
import com.jinlong.im.controller.activity.LoginActivity;
import com.jinlong.im.model.Model;

/**
 * 设置页面
 * Created by Administrator on 2016/10/27.
 */

public class SettingFragment extends Fragment{
    private Button bt_setting_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        initView(view);   //初始化view
        return view;
    }
    private void initView(View view) {
        bt_setting_out = (Button)view.findViewById(R.id.bt_setting_out);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();  //初始化数据
    }
    private void initData() {
        //在button上显示当前用户名称
        bt_setting_out.setText("退出登录（" + EMClient.getInstance().getCurrentUser()+"）");
        //退出登录的逻辑处理 （环信服务器）
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {  //登录环信服务器退出登录
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {        //回到登录页面、更新UI显示
                                Model.getInstance().getDbManager().close();  //关闭数据库
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity() , LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }
                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "退出失败"+ s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onProgress(int i, String s) {                     }
                        });
                    }
                });
            }
        });

    }
}
