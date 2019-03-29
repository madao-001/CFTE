package com.example.administrator.cfte.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.administrator.cfte.Model.CallNumber;
import com.example.administrator.cfte.Model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageUtil {

    private Message message;
    private List<Message> messages=new ArrayList<>();
    private Context context;

    // 所有短信
    final String SMS_URI_ALL = "content://sms/";

    public MessageUtil(Context context) {
        this.context=context;
    }

    public List<Message> getMessages(){
        List<Message> messageList=new ArrayList<>();
        Uri uri=Uri.parse(SMS_URI_ALL);
        String[] projection = new String[] { "_id", "address", "person",
                "body", "date", "type", };
        // 获取手机内部短信
        Cursor cur = context.getContentResolver().query(uri, projection, null,
                null, "date desc");
        while (cur.moveToNext()){
            String address=cur.getString(cur.getColumnIndex("address"));
            String body=cur.getString(cur.getColumnIndex("body"));
            long date=cur.getLong(cur.getColumnIndex("date"));
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd");
            Date d = new Date(date);
            String strDate = dateFormat.format(d);
            String name=getName(address).length()<=11?getName(address)+":":getName(address).substring(0,12)+"...";
            Message message=new Message(name,strDate,body);
            messageList.add(message);
        }
        return messageList;
    }

    public String getName(String address){
        CallUtil callUtil=new CallUtil(context);
        List<CallNumber> callNumberList=callUtil.getCallPhoneList();
        for (int i=0;i<callNumberList.size();i++){
            CallNumber callNumber=callNumberList.get(i);
            if (callNumber.getNumber().equals(address)){
                return callNumber.getName();
            }
        }
        return address;
    }
}
