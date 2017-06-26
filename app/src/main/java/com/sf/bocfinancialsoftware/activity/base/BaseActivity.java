package com.sf.bocfinancialsoftware.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/15.
 * 邮箱：769006026@qq.com
 * project: GoodTaste
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置支持屏幕的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    protected <T extends View> T bindView(int viewId){
        return (T) findViewById(viewId);
    }

    protected abstract void initView();         //初始化View
    protected abstract void initData();        //初始化数据
    protected abstract void initListener();    //初始化事件


}
