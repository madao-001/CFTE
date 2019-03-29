package com.example.administrator.cfte.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cfte.R;

public class MessageBtn extends LinearLayout {
    private TextView message_name;
    private TextView message_time;
    private TextView message_all;

    public MessageBtn(Context context) {
        this(context, null);
    }

    public MessageBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.message, this, true);
        message_name=(TextView)findViewById(R.id.message_name);
        message_time=(TextView)findViewById(R.id.message_time);
        message_all=(TextView)findViewById(R.id.message_all);
    }

    public void setMessage_name(String name){
        this.message_name.setText(name);
    }

    public void setMessage_time(String time){
        this.message_time.setText(time);
    }

    public void setMessage_all(String all){
        this.message_all.setText(all);
    }
}
