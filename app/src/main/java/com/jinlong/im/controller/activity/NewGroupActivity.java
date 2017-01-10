package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.jinlong.im.R;
import com.jinlong.im.model.Model;

import static com.jinlong.im.R.id.cb_newgroup_invite;

/**
 * 创建新群
 */
public class NewGroupActivity extends Activity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_desc;
    private CheckBox cb_newgroup_public;
    private CheckBox cb_newgroup_invite;
    private Button bt_newgroup_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        initView();
        initListener();
    }
    private void initListener() {   //创建点击按钮的事件处理
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //跳转到选择联系人页面
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);
                startActivityForResult(intent , 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){   //成功获取到联系人    、创建群
            createGroup(data.getStringArrayExtra("members"));  //群成员
        }
    }

    private void createGroup(final String[] memberses) {
        final String groupName = et_newgroup_name.getText().toString();  //群名称
        final String groupDesc = et_newgroup_desc.getText().toString();  //群描述
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {  //开启子线程，去环信服务器创建群
                //参数： 1.群名称、2.群描述、3.群成员、4.原因、5.参数设置
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                options.maxUsers = 200;  //群最多容纳200人

                EMGroupManager.EMGroupStyle groupStyle = null;
                if(cb_newgroup_public.isChecked()){   //公开群
                    if(cb_newgroup_invite.isChecked()){  //公开群、开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else{  //公开群、不开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else {  //不公开
                    if(cb_newgroup_invite.isChecked()){  //不公开群、开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else{  //不公开群、不开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                options.style = groupStyle; //群类型
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName , groupDesc , memberses ,"申请加入群" , options);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this , "创建群成功" , Toast.LENGTH_SHORT).show();
                            finish();  //关闭当前页面
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this , "创建群失败" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        et_newgroup_name = (EditText) findViewById(R.id.et_newgroup_name);
        et_newgroup_desc = (EditText) findViewById(R.id.et_newgroup_desc);
        cb_newgroup_public = (CheckBox) findViewById(R.id.cb_newgroup_public);
        cb_newgroup_invite = (CheckBox) findViewById(R.id.cb_newgroup_invite);
        bt_newgroup_create = (Button) findViewById(R.id.bt_newgroup_create);
    }
}
