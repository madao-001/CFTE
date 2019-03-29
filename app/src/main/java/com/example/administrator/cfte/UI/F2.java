package com.example.administrator.cfte.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.cfte.R;
import com.example.administrator.cfte.Service.FileService;
import com.example.administrator.cfte.Widget.ImageBt;


public class F2 extends Fragment {

    private ImageBt ib1;
    private ImageBt ib2;
    private ImageBt ib3;
    private ImageBt ib4;
    private ImageBt ib5;
    private ImageBt ib6;
    //紧急联系人
    private String ICEnumber;
    private String classpath = "/data/data/com.example.administrator.cfte/shared_prefs/contact.xml";
    private FileService fileService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f2, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileService=new FileService(getContext());
        ib1 = (ImageBt) getView().findViewById(R.id.bt_call);
        ib1.setTextViewText("打电话");
        ib1.setImageResource(R.mipmap.phone);
        //ib1.setTextViewPadding(70);

        ib1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent intent=new Intent(getActivity(),CallActivity.class);
                startActivity(intent);
//                Intent dialIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"110"));//跳转到拨号界面
//                startActivity(dialIntent);
            }
        });

        ib2 = (ImageBt) getView().findViewById(R.id.bt_note);
        ib2.setTextViewText("看短信");
        ib2.setImageResource(R.mipmap.note);
        //ib2.setTextViewPadding(70);

        ib2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent intent=new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
            }
        });

        ib3 = (ImageBt) getView().findViewById(R.id.bt_photograph);
        ib3.setTextViewText("打开相机");
        ib3.setImageResource(R.mipmap.photograph);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        ib3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent cameraIntent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
                startActivity(cameraIntent);
            }
        });

        ib4 = (ImageBt) getView().findViewById(R.id.bt_photo);
        ib4.setTextViewText("查看相册");
        ib4.setImageResource(R.mipmap.photo);

        ib4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_GALLERY);
                startActivity(intent);
            }
        });

        ib5 = (ImageBt) getView().findViewById(R.id.bt_set);
        ib5.setTextViewText("打开设置");
        ib5.setImageResource(R.mipmap.set);

        ib5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                Intent intent=new Intent(getActivity(),SetActivity.class);
                startActivity(intent);
            }
        });

        ib6 = (ImageBt) getView().findViewById(R.id.bt_ice);
        ib6.setTextViewText("紧急电话");
        ib6.setImageResource(R.mipmap.ice);

        ib6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在这里可以实现点击事件
                String callNumber=null;
                try {
                    callNumber=fileService.readFromPhone("call.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (callNumber!=null) {
                    Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+callNumber));
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示") ;
                    builder.setMessage("请到设置里添加紧急联系人" ) ;
                    builder.setPositiveButton("确定" ,  null );
                    builder.show();
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
}