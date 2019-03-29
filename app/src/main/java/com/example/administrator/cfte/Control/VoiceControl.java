package com.example.administrator.cfte.Control;


import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Locale;

public class VoiceControl{
    private String order;
    private String result;
    //发声
    private TextToSpeech tts;

    public void setOrder(String order){
        this.order=Pinyin.toPinyin(order,"");
        ActionJudgment(this.order);
    }

    public void Execute(int type){
        switch (type){
            case 0:
                String appname = order.substring(5);
                open(appname);
                break;
            case 1:
                String name = order.substring(11);
                call(name);
                break;
        }
    }

    public void ActionJudgment(String order){
        if(order.startsWith("DAKAI")){
            Execute(0);
        }else if(order.startsWith("DADIANHUAJI")){
            Execute(1);
        }
    }

    public void open(String appname){
        if(appname.equals("XIANGJI")){
            result = "open_ib3";
        }else if(appname.equals("XIANGCE")){
            result = "open_ib4";
        } else if(appname.equals("SHEZHI")){
            result = "open_ib5";
        }else if(appname.equals("DUANXIN")){
            result = "open_ib2";
        }else if(appname.equals("WEIXIN")){
            result = "open_WEIXIN";
        }else if(appname.equals("XIMALAYA")){
            result = "open_XIMALAYA";
        }else if(appname.equals("TAOBAO")){
            result = "open_TAOBAO";
        }else if(appname.equals("DOUYIN")){
            result = "open_DOUYIN";
        }else if(appname.equals("JINGDONG")){
            result = "open_JINGDONG";
        }
    }


    public void call(String name){
        result = name;
    }

    public String getResult(){
        return result;
    }

}
