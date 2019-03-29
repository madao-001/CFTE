package com.example.administrator.cfte.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cfte.R;

public class NumberBtn extends LinearLayout {
    private TextView numberBtn;

    public NumberBtn(Context context) {
        this(context, null);
    }

    public NumberBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.call_number, this, true);
        numberBtn=(TextView)findViewById(R.id.number);
    }

    public void setNumber(String number){
        numberBtn.setText(number);
    }

    public void setTextSize(float size){
        numberBtn.setTextSize(size);
    }
}
