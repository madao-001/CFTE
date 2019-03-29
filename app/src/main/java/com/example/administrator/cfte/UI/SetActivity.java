package com.example.administrator.cfte.UI;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.cfte.R;
import com.example.administrator.cfte.Service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SetActivity extends AppCompatActivity {

    private EditText user_id;
    private EditText jinji_name;
    private EditText jinji_number;
    private EditText user_location;
    private Button OkBtn;
    private Button locationBtn;
    private String userId;
    private String message;
    private FileService fileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initBtn();
        OkBtnClicked();
        locationBtn();
    }

    class GetThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;//声明连接对象
            String urlStr="http://wxmsg.dingliqc.com/send?msg="+message+"&userIds="+userId;
            InputStream is = null;
            String resultData = "";
            try {
                URL url = new URL(urlStr); //URL对象
                conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                conn.setRequestMethod("GET"); //使用get请求

                if(conn.getResponseCode()==200){//返回200表示连接成功
                    is = conn.getInputStream(); //获取输入流
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine  = "";
                    while((inputLine = bufferReader.readLine()) != null){
                        resultData += inputLine + "\n";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void locationBtn(){
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location=user_location.getText().toString();
                userId=user_id.getText().toString();
                message="您设置的常驻地址是"+location;
                try {
                    if (location.isEmpty()||userId.isEmpty()){
                        AlertDialog.Builder builder  = new AlertDialog.Builder(SetActivity.this);
                        builder.setTitle("错误提示" ) ;
                        builder.setMessage("输入有空，请重新输入" ) ;
                        builder.setPositiveButton("确定" ,  null );
                        builder.show();
                    }else {
                        new GetThread().start();
                        try {
                            fileService.saveToPhone("userid.txt",userId);
                            fileService.saveToPhone("location.txt",location);
                            AlertDialog.Builder builder  = new AlertDialog.Builder(SetActivity.this);
                            builder.setTitle("设置成功" ) ;
                            builder.setMessage("常住地址以及userId设置成功，为"+location ) ;
                            builder.setPositiveButton("确定" ,  null );
                            builder.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initBtn(){
        fileService=new FileService(getApplicationContext());
        user_id=(EditText)findViewById(R.id.user_id);
        jinji_name=(EditText)findViewById(R.id.jinji_name);
        jinji_number=(EditText)findViewById(R.id.jinji_number);
        user_location=(EditText)findViewById(R.id.user_location);
        OkBtn=(Button)findViewById(R.id.OkBtn);
        locationBtn=(Button)findViewById(R.id.locationBtn);
    }

    public void OkBtnClicked(){
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=jinji_name.getText().toString();
                String number=jinji_number.getText().toString();
                if (name.isEmpty()||number.isEmpty()){
                    AlertDialog.Builder builder  = new AlertDialog.Builder(SetActivity.this);
                    builder.setTitle("输入有空" ) ;
                    builder.setMessage("输入有空，请重新输入" ) ;
                    builder.setPositiveButton("确定" ,  null );
                    builder.show();
                    jinji_name.setText("");
                    jinji_number.setText("");
                }else {
                    try {
                        fileService.saveToPhone("call.txt",number);
                        AlertDialog.Builder builder  = new AlertDialog.Builder(SetActivity.this);
                        builder.setTitle("成功") ;
                        builder.setMessage("添加成功"+name+":"+number ) ;
                        builder.setPositiveButton("确定" ,  null );
                        builder.show();
                        jinji_name.setText("");
                        jinji_number.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
