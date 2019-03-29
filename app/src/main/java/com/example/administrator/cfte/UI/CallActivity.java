package com.example.administrator.cfte.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.cfte.R;
import com.example.administrator.cfte.Widget.NumberBtn;

public class CallActivity extends AppCompatActivity {

    private NumberBtn number0;
    private NumberBtn number1;
    private NumberBtn number2;
    private NumberBtn number3;
    private NumberBtn number4;
    private NumberBtn number5;
    private NumberBtn number6;
    private NumberBtn number7;
    private NumberBtn number8;
    private NumberBtn number9;
    private NumberBtn numberjing;
    private NumberBtn numberxing;
    private NumberBtn jingcha;
    private NumberBtn yiyuan;
    private NumberBtn delete;
    private NumberBtn boda;
    private NumberBtn title;
    private EditText editText;
    private StringBuffer stringBuffer=new StringBuffer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        initBtn();
        numberInit();
    }

    /**
     *
     */
    public void numberInit(){
        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(0);
                editText.setText(stringBuffer.toString());
            }
        });
        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(1);
                editText.setText(stringBuffer.toString());
            }
        });
        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(2);
                editText.setText(stringBuffer.toString());
            }
        });
        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(3);
                editText.setText(stringBuffer.toString());
            }
        });
        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(4);
                editText.setText(stringBuffer.toString());
            }
        });
        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(5);
                editText.setText(stringBuffer.toString());
            }
        });
        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(6);
                editText.setText(stringBuffer.toString());
            }
        });
        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(7);
                editText.setText(stringBuffer.toString());
            }
        });
        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(8);
                editText.setText(stringBuffer.toString());
            }
        });
        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append(9);
                editText.setText(stringBuffer.toString());
            }
        });
        numberxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append("*");
                editText.setText(stringBuffer.toString());
            }
        });
        numberjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer.append("#");
                editText.setText(stringBuffer.toString());
            }
        });
        jingcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJingcha=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"110"));
                startActivity(intentJingcha);
            }
        });
        yiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentYiyuan=new Intent(Intent.ACTION_CALL,Uri.parse("tel:120"));
                startActivity(intentYiyuan);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringBuffer.length()!=0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    editText.setText(stringBuffer.toString());
                }
            }
        });
        boda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBoda=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+stringBuffer.toString()));
                startActivity(intentBoda);
            }
        });
    }

    /**
     * 初始化界面
     */
    public void initBtn(){
        number0=(NumberBtn)findViewById(R.id.bt_0);
        number1=(NumberBtn)findViewById(R.id.bt_1);
        number2=(NumberBtn)findViewById(R.id.bt_2);
        number3=(NumberBtn)findViewById(R.id.bt_3);
        number4=(NumberBtn)findViewById(R.id.bt_4);
        number5=(NumberBtn)findViewById(R.id.bt_5);
        number6=(NumberBtn)findViewById(R.id.bt_6);
        number7=(NumberBtn)findViewById(R.id.bt_7);
        number8=(NumberBtn)findViewById(R.id.bt_8);
        number9=(NumberBtn)findViewById(R.id.bt_9);
        numberjing=(NumberBtn)findViewById(R.id.bt_jing);
        numberxing=(NumberBtn)findViewById(R.id.bt_xing);
        jingcha=(NumberBtn)findViewById(R.id.bt_jingcha);
        yiyuan=(NumberBtn)findViewById(R.id.bt_yiyuan);
        delete=(NumberBtn)findViewById(R.id.bt_delete);
        boda=(NumberBtn)findViewById(R.id.bt_boda);
        title=(NumberBtn)findViewById(R.id.bt_text);
        editText=(EditText)findViewById(R.id.number_edit);
        editText.setFocusable(false);
        number0.setNumber("0");
        number1.setNumber("1");
        number2.setNumber("2");
        number3.setNumber("3");
        number4.setNumber("4");
        number5.setNumber("5");
        number6.setNumber("6");
        number7.setNumber("7");
        number8.setNumber("8");
        number9.setNumber("9");
        numberjing.setNumber("#");
        numberxing.setNumber("*");
        jingcha.setNumber("警察");
        jingcha.setTextSize(25);
        yiyuan.setNumber("医院");
        yiyuan.setTextSize(25);
        delete.setNumber("删除");
        delete.setTextSize(25);
        boda.setNumber("拨打");
        boda.setTextSize(25);
        title.setNumber("输入电话号码之后点击拨打");
    }
}
