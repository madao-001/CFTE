package com.example.administrator.cfte;
/**
 * 作者：欧阳文君
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.cfte.Adapter.MyAdapter;
import com.example.administrator.cfte.Control.VoiceControl;
import com.example.administrator.cfte.GetLocation.IMyLocation;
import com.example.administrator.cfte.GetLocation.MyLocationBean;
import com.example.administrator.cfte.GetLocation.MyLocationManager;
import com.example.administrator.cfte.UI.CallActivity;
import com.example.administrator.cfte.UI.F1;
import com.example.administrator.cfte.UI.F2;
import com.example.administrator.cfte.UI.F3;
import com.example.administrator.cfte.Service.FileService;
import com.example.administrator.cfte.Utils.WeChatTextWrapper;
import com.example.administrator.cfte.Widget.ImageBt;
import com.example.administrator.cfte.Widget.Point;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.administrator.cfte.Utils.CallUtil;

import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, IMyLocation, ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private MyAdapter mAdapter;
    private ArrayList<Fragment> data;
    private TextView time, date, dianliang, location;
    private ImageButton voice;
    private RecognizerDialog mDialog;

    private CallUtil callUtil;
    private String userId;
    private String message;
    private FileService fileService;
    private String location_now;

    private F1 f1;
    private F2 f2;
    private F3 f3;
    private CallActivity callActivity;

    //当Android6.0系统以上时，动态获取权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.BROADCAST_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_PHONE_STATE ,
            Manifest.permission.MODIFY_PHONE_STATE
    };

    //权限的标志
    private static final int PERMISSION_CODES = 1001;
    private boolean permissionGranted = true;
    //反地理编码的Ak
    public static String AK = "LzkwGACITXdeWKZDYNaQhdhpG7oSfiWy";
    //反地理编码请求网址
    public static String BaseUrl = "http://api.map.baidu.com/";
    //发声
    private TextToSpeech tts;
    //发音文本
    private String text = "";

    private LinearLayout ll;
    private boolean run = false;
    private boolean run2 = false;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableAPIDialog();
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tts = new TextToSpeech(this, this);


        if (!permissionGranted) {
            Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
            return;
        }

        //一分钟刷新一次
        run = true;
        run2 = true;
        handler.postDelayed(task, 1000);
        handler2.postDelayed(task2, 60000);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int language = tts.setLanguage(Locale.CHINESE);
            tts.setSpeechRate(1.0f);
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "this language is not support");
            } else {

            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    //定时更新时间
    private final Runnable task = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                try {
                    SetupData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        }
    };
    private final Runnable task2 = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run2) {
                try {
                    SetupLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler2.postDelayed(this, 60000);
            }
        }
    };
    //@Override
    //public void onClick(View v) {
    //    switch (v.getId()) {
    //        case R.id.btn1:
    //            f4 = new F4();
    //            data.add(f4);
    //            mAdapter.setData(data);
    //            break;
    //    }
    //}

    //下面三个是ViewPager 滑动监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动监听回调
     *
     * @param position 第一页0 第二页1 第三页2   显示的是到哪个页面的时候  比如第一页到第二页就是1
     *                 第二页到第一页就是0
     */
    @Override
    public void onPageSelected(int position) {
        ll.removeAllViews();
        //总共的页数
        int page = data.size();
        for (int i = 0; i < page; i++) {
            Point point = new Point(this);
            if (i == position) {
                point.setSelected(true);
            } else {
                point.setSelected(false);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.leftMargin = 0;
            params.topMargin = 0;
            ll.addView(point, params);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //更新数据
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetupData() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 10) {
            time.setText(hour + ":0" + minute);
        } else {
            time.setText(hour + ":" + minute);
        }
        date.setText(year + "年" + month + "月" + day + "日");

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        dianliang.setText(battery + "");
    }

    public void SetupLocation() throws Exception{
        MyLocationManager manager = new MyLocationManager(this);
        manager.getLocationByLonAndLat(manager.beginLocatioon(), "", "");
        String location_home = fileService.readFromPhone("location.txt");
        userId = fileService.readFromPhone("userid.txt");
        if (!userId.isEmpty()&&!location_home.isEmpty()){
            location_now = location.getText().toString();
            if (!(location_now.substring(0,10).equals(location_home.substring(0,10)))) {
                message = "老人已经离开常住位置，位置为" + location_now;
                new GetThread().start();
                Toast.makeText(this,"您已离开常驻位置",Toast.LENGTH_SHORT).show();
                message = "";
            }
        }
    }
    class GetThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;//声明连接对象
            String urlStr="http://wxmsg.dingliqc.com/send?msg="+message+"&userIds="+userId+"&title=位置异动警告";
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

    //活动初始化
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init() throws Exception {
        fileService=new FileService(getApplicationContext());
        vp = (ViewPager) findViewById(R.id.vp);
        time = (TextView) findViewById(R.id.tv_time);
        date = (TextView) findViewById(R.id.tv_date);
        dianliang = (TextView) findViewById(R.id.tv_dianliang);
        location = (TextView) findViewById(R.id.tv_location);
        voice = (ImageButton) findViewById(R.id.ib_voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                        permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    initSpeech(MainActivity.this);
                }
            }
        });

        data = new ArrayList<>();
        f1 = new F1();
        f2 = new F2();
        f3 = new F3();
        data.add(f1);
        data.add(f2);
        data.add(f3);
        mAdapter = new MyAdapter(getSupportFragmentManager(), data);
        vp.setAdapter(mAdapter);
        vp.addOnPageChangeListener(this);
        ll = (LinearLayout) findViewById(R.id.ll);
        vp.setCurrentItem(1);
        SetupData();
        SetupLocation();
    }

    //语言输入
    public void initSpeech(final Context context) {
        mDialog = new RecognizerDialog(context, null);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "madarin");
        mDialog.setListener(new RecognizerDialogListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if (!b) {
                    String result = parseVoice(recognizerResult.getResultString());
                    speakOut(result);
                    VoiceControl vc = new VoiceControl();
                    vc.setOrder(text);
                    final String order = vc.getResult();
                    if (order.indexOf("open") != -1) {
                        Execute("open",order);
                    } else {
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Execute("call",order);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 2000);//3秒后执行TimeTask的run方法
                    }
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        mDialog.show();
    }

    /**
     * 禁止弹窗
     */
    private void disableAPIDialog() {

        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            stringBuffer.append(word);
        }
        return stringBuffer.toString();
    }

    public class Voice {
        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }

    //获取地理位置
    @Override
    public LocationManager getLocationManager() {
        return (LocationManager) (getSystemService(LOCATION_SERVICE));
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void onSuccessGeocoder(Response<MyLocationBean> response) {
        if (response != null) {
            String locations = response.body().getResult().getFormatted_address();
            location.setText(locations);
        }
    }

    @Override
    public void onFileGeocoder(Throwable t) {

    }

    /**
     * 动态的进行权限请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        List<String> p = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                p.add(permission);
            }
        }
        if (p.size() > 0) {
            requestPermissions(p.toArray(new String[p.size()]), PERMISSION_CODES);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODES:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    permissionGranted = false;
                } else {
                    permissionGranted = true;
                }
        }
    }

    public void Execute(String type,String order) {
        if(type.equals("open")){
            order=order.substring(5);
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("ib2", 1);
            map.put("ib3", 2);
            map.put("ib4", 3);
            map.put("ib5", 4);
            map.put("WEIXIN",5);
            map.put("XIMALAYA",6);
            map.put("TAOBAO",8);
            map.put("DOUYIN",9);
            map.put("JINGDONG",10);

            if ((map.get(order)) != null) {
                switch (map.get(order)) {
                    case 1:
                        ImageBt ib2 = f2.getIb2();
                        ib2.performClick();
                        break;
                    case 2:
                        ImageBt ib3 = f2.getIb3();
                        ib3.performClick();
                        break;
                    case 3:
                        ImageBt ib4 = f2.getIb4();
                        ib4.performClick();
                        break;
                    case 4:
                        ImageBt ib5 = f2.getIb5();
                        ib5.performClick();
                        break;
                    case 5:
                        Intent intent = new Intent();
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName(WeChatTextWrapper.WECAHT_PACKAGENAME, WeChatTextWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI);
                        startActivity(intent);
                        break;
                    case 6:
                        try {
                            intent = this.getPackageManager().getLaunchIntentForPackage("com.ximalaya.ting.android");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 8:
                        try {
                            intent = this.getPackageManager().getLaunchIntentForPackage("com.taobao.taobao");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 9:
                        try {
                            intent = this.getPackageManager().getLaunchIntentForPackage("com.ss.android.ugc.aweme");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 10:
                        try {
                            intent = this.getPackageManager().getLaunchIntentForPackage("com.jingdong.app.mall");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }else if(type.equals("call")){
            callUtil = new CallUtil(MainActivity.this);
            String result = callUtil.PinYinSearch(order);
            if (result.equals("Not Found")) {
                Toast.makeText(this, "未找到该联系人", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + result);
                intent.setData(data);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        }
    }

    //发声
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void toSpeak(){
        tts.speak("正在"+text,TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected  void speakOut(String s){
        text=s;
        toSpeak();
    }

    //检测是否安装了应用
    private boolean isAvilible( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}

