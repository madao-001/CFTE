package com.example.administrator.cfte.Widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.cfte.R;

public class MyDialog extends Dialog{
    private Activity context;
    private EditText name;
    private TextView cancel,confirm;
    //确定文本和取消文本的显示的内容
    private String yesStr, noStr;
    // 点击事件
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public MyDialog(Activity context) {
        super(context);
        this.context = context;

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        //初始化界面控件
        initView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        name = (EditText) findViewById(R.id.et_name);
        cancel = (TextView) findViewById(R.id.tv_cancel);
        confirm = (TextView) findViewById(R.id.tv_confirm);
        //如果设置按钮文字
        if (yesStr != null) {
            confirm.setText(yesStr);
        }
        if (noStr != null) {
            cancel.setText(noStr);
        }
        //设置确定按钮被点击后，向外界提供监听
        confirm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (yesOnclickListener != null) {
                     yesOnclickListener.onYesOnclick();
                 }
             }
         });
        //设置取消按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (noOnclickListener != null) {
                     noOnclickListener.onNoClick();
                 }
             }
         });
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param yesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener yesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = yesOnclickListener;
    }

    public interface onNoOnclickListener {
         public void onNoClick();
    }

    public interface onYesOnclickListener {
         public void onYesOnclick();
     }
    public String getName(){
        return name.getText().toString();
    }
}
