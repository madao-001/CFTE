package com.example.administrator.cfte.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cfte.R;

/*
列表一条记录的UI
 */
public class CallBtn extends LinearLayout {
    //图片和用户名和电话号
    private ImageView iv;
    private TextView call_name;
    private TextView call_number;

    public CallBtn(Context context) {
        this(context, null);
    }

    public CallBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.callbtn, this, true);
        iv = (ImageView) findViewById(R.id.iv);
        call_name = (TextView) findViewById(R.id.call_name);
        call_number = (TextView) findViewById(R.id.call_number);
    }

    /**
     * 设置显示的文字
     */
    public void setCall_name(String text) {
        call_name.setText(text);
    }
    public void setCall_number(String text) {call_number.setText(text);}

}