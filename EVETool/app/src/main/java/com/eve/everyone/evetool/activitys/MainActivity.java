package com.eve.everyone.evetool.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.eve.everyone.evetool.R;

import org.xutils.view.annotation.ViewInject;

/**
 * 主界面:功能选择界面
 */
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.main_recyclerView)
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
       //设置主界面的背景图片

//        recyclerView.setBackground();
    }
}
