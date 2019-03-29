package com.example.administrator.cfte.UI;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.example.administrator.cfte.R;
import com.example.administrator.cfte.Service.AutoSendMsgService;
import com.example.administrator.cfte.Service.FileService;
import com.example.administrator.cfte.Utils.OpenAccessibilitySettingHelper;
import com.example.administrator.cfte.Utils.WeChatTextWrapper;
import com.example.administrator.cfte.Widget.ImageBt;
import com.example.administrator.cfte.Widget.MyDialog;


import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.administrator.cfte.Service.AutoSendMsgService.JUMP_STATUS;
import static com.example.administrator.cfte.Service.AutoSendMsgService.JUMP_SUCCESS;
import static com.example.administrator.cfte.Service.AutoSendMsgService.ServiceType;
import static com.example.administrator.cfte.Service.AutoSendMsgService.ServiceType_video;
import static com.example.administrator.cfte.Service.AutoSendMsgService.ServiceType_word;
import static com.example.administrator.cfte.Service.AutoSendMsgService.hasJump;
import static com.example.administrator.cfte.Utils.WechatUtils.NAME;
public class F3 extends Fragment {

    private ImageBt ib1;
    private ImageBt ib2;
    private ImageBt ib3;
    private ImageBt ib4;
    private ImageBt ib5;
    private ImageBt ib6;
    //微信联系人
    private String contact_person,contact_person1,contact_person2;
    private FileService fileService;

    private AccessibilityManager accessibilityManager;
    private String name;
    private int type;
    private MyDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f3, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileService=new FileService(getContext());
        ib1 = (ImageBt) getView().findViewById(R.id.bt_wifi);
        ib1.setTextViewText("开关无线网");
        ib1.setImageResource(R.mipmap.wifi);
        ib1.setTextSize(25);
        //ib1.setTextViewPadding(70);

        ib1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                WifiManager wifiManger=(WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManger.isWifiEnabled()) {
                    wifiManger.setWifiEnabled(false);
                    Toast.makeText(getContext(),"已关闭无线网",Toast.LENGTH_LONG).show();
                } else {
                    wifiManger.setWifiEnabled(true);
                    Toast.makeText(getContext(),"已打开无线网",Toast.LENGTH_LONG).show();
                }

            }
        });

        ib2 = (ImageBt) getView().findViewById(R.id.bt_liuliang);
        ib2.setTextViewText("开关流量");
        ib2.setImageResource(R.mipmap.liuliang);
        ib2.setTextSize(25);
        //ib2.setTextViewPadding(70);

        ib2.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                startActivity(intent);
                Toast.makeText(getContext(),"请手动关闭移动数据",Toast.LENGTH_LONG).show();
            }
        });

        ib3 = (ImageBt) getView().findViewById(R.id.bt_faweixin_erzi);
        try {
            contact_person1=fileService.readFromPhone("contact_person1.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contact_person1!=null) {
            ib3.setTextViewText("给"+contact_person1+"发微信");
        }else {
            ib3.setTextViewText("给xxx发微信");
        }
        ib3.setImageResource(R.mipmap.faweixin);
        ib3.setTextSize(25);
        ib3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                contact_person1=null;
                try {
                    contact_person1=fileService.readFromPhone("contact_person1.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contact_person1!=null) {
                    contact_person = contact_person1;
                    checkAndStartService();
                    type=ServiceType_word;
                } else {
                    dialog = new MyDialog(getActivity());
                    dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                     @Override
                     public void onYesOnclick() {
                         contact_person1=dialog.getName();
                         if(contact_person1.isEmpty()){
                             Toast.makeText(getContext(),"请输入微信快捷联系人名称！",Toast.LENGTH_LONG).show();
                         }else {
                             try {
                                 ib3.setTextViewText("给"+contact_person1+"发微信");
                                 ib5.setTextViewText("和"+contact_person1+"视频");
                                 fileService.saveToPhone("contact_person1.txt",contact_person1);
                                 Toast.makeText(getContext(),"微信快捷联系人设置成功！",Toast.LENGTH_LONG).show();
                                 dialog.dismiss();
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     }
                 });
                    dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                     @Override
                     public void onNoClick() {
                         dialog.dismiss();
                     }
                 });
                    dialog.show();
                }
            }
        });

        ib4 = (ImageBt) getView().findViewById(R.id.bt_faweixin_nver);
        try {
            contact_person2=fileService.readFromPhone("contact_person2.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contact_person2!=null) {
            ib4.setTextViewText("给"+contact_person2+"发微信");
        }else {
            ib4.setTextViewText("给xxx发微信");
        }
        ib4.setImageResource(R.mipmap.faweixin);
        ib4.setTextSize(25);

        ib4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                contact_person2=null;
                try {
                    contact_person2=fileService.readFromPhone("contact_person2.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contact_person2!=null) {
                    contact_person = contact_person2;
                    checkAndStartService();
                    type=ServiceType_word;
                } else {
                    dialog = new MyDialog(getActivity());
                    dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                        @Override
                        public void onYesOnclick() {
                            contact_person2=dialog.getName();
                            if(contact_person2.isEmpty()){
                                Toast.makeText(getContext(),"请输入微信快捷联系人名称！",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    ib4.setTextViewText("给"+contact_person2+"发微信");
                                    ib6.setTextViewText("和"+contact_person2+"视频");
                                    fileService.saveToPhone("contact_person2.txt",contact_person2);
                                    Toast.makeText(getContext(),"微信快捷联系人设置成功！",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        ib5 = (ImageBt) getView().findViewById(R.id.bt_video_erzi);
        try {
            contact_person1=fileService.readFromPhone("contact_person1.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contact_person1!=null) {
            ib5.setTextViewText("和"+contact_person1+"视频");
        }else {
            ib5.setTextViewText("和xxx视频");
        }
        ib5.setImageResource(R.mipmap.video);
        ib5.setTextSize(25);

        ib5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                contact_person1=null;
                try {
                    contact_person1=fileService.readFromPhone("contact_person1.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contact_person1!=null) {
                    contact_person = contact_person1;
                    checkAndStartService();
                    type=ServiceType_video;
                } else {
                    dialog = new MyDialog(getActivity());
                    dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                        @Override
                        public void onYesOnclick() {
                            contact_person1=dialog.getName();
                            if(contact_person1.isEmpty()){
                                Toast.makeText(getContext(),"请输入微信快捷联系人名称！",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    ib3.setTextViewText("给"+contact_person1+"发微信");
                                    ib5.setTextViewText("和"+contact_person1+"视频");
                                    fileService.saveToPhone("contact_person1.txt",contact_person1);
                                    Toast.makeText(getContext(),"微信快捷联系人设置成功！",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        ib6 = (ImageBt) getView().findViewById(R.id.bt_video_nver);
        try {
            contact_person2=fileService.readFromPhone("contact_person2.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contact_person2!=null) {
            ib6.setTextViewText("和"+contact_person2+"视频");
        }else {
            ib6.setTextViewText("和xxx视频");
        }
        ib6.setImageResource(R.mipmap.video);
        ib6.setTextSize(25);

        ib6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                contact_person2=null;
                try {
                    contact_person2=fileService.readFromPhone("contact_person2.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (contact_person2!=null) {
                    contact_person = contact_person2;
                    type=ServiceType_video;
                    checkAndStartService();
                } else {
                    dialog = new MyDialog(getActivity());
                    dialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                        @Override
                        public void onYesOnclick() {
                            contact_person2=dialog.getName();
                            if(contact_person2.isEmpty()){
                                Toast.makeText(getContext(),"请输入微信快捷联系人名称！",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    ib4.setTextViewText("给"+contact_person2+"发微信");
                                    ib6.setTextViewText("和"+contact_person2+"视频");
                                    fileService.saveToPhone("contact_person2.txt",contact_person2);
                                    Toast.makeText(getContext(),"微信快捷联系人设置成功！",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    dialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    public ImageBt getIb1(){
        return ib1;
    }
    public ImageBt getIb2(){
        return ib2;
    }
    public ImageBt getIb3(){
        return ib3;
    }
    public ImageBt getIb4(){
        return ib4;
    }
    public ImageBt getIb5(){
        return ib5;
    }
    public ImageBt getIb6(){
        return ib6;
    }

    private int goWecaht() {
        try {
            setValue(name,type);
            hasJump = false;
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(WeChatTextWrapper.WECAHT_PACKAGENAME, WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI);
            startActivity(intent);

            while (true) {
                if (hasJump) {
                    return JUMP_STATUS;
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        OpenAccessibilitySettingHelper.jumpToSettingPage(getActivity());// 跳转到开启页面
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return JUMP_STATUS;
        }
    }

    private void checkAndStartService() {
        accessibilityManager = (AccessibilityManager) getActivity().getSystemService(ACCESSIBILITY_SERVICE);

        name = contact_person;

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "联系人不能为空", Toast.LENGTH_SHORT).show();
        }else{
            if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                    AutoSendMsgService.class.getName())) {// 判断服务是否开启
                Toast.makeText(getActivity(), "服务未启动，请手动启动"+getString(R.string.app_name)+"服务", Toast.LENGTH_SHORT).show();
                OpenAccessibilitySettingHelper.jumpToSettingPage(getActivity());// 跳转到开启页面
            } else {
                Toast.makeText(getActivity(), getString(R.string.app_name)+"服务已开启", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        statusHandler.sendEmptyMessage(goWecaht());
                    }
                }).start();
            }
        }
    }

    Handler statusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSendStatusText(msg.what);
        }
    };

    private void setSendStatusText(int status) {
        if (status == JUMP_SUCCESS) {
            Toast.makeText(getActivity(), "微信跳转成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "微信跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void setValue(String name,int type) {
        NAME = name;
        ServiceType = type;
        hasJump = false;
    }

}
