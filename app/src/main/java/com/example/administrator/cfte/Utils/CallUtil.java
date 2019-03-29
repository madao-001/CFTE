package com.example.administrator.cfte.Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.administrator.cfte.MyApplicaton;
import com.example.administrator.cfte.Model.CallNumber;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PolarBear on 2019/3/6.
 */

public class CallUtil {
    //联系人属性
    public final static String call_name= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    public final static String call_number=ContactsContract.CommonDataKinds.Phone.NUMBER;
    //通讯录列表
    private List<CallNumber> callNumberList=new ArrayList<>();
    //获取上下文
    private Context context;
    //获取uri
    private Uri callNumberUri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public CallUtil(Context context) {
        this.context = context;
    }

    public List<CallNumber> getCallPhoneList() {
        List<CallNumber> callNumberList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(callNumberUri, new String[]{call_number, call_name}, null, null, null);
        while (cursor.moveToNext()) {
            CallNumber callNumber = new CallNumber(cursor.getString(cursor.getColumnIndex(call_name)), cursor.getString(cursor.getColumnIndex(call_number)));
            callNumberList.add(callNumber);
        }
        return callNumberList;
    }

    public String PinYinSearch(String name){
        callNumberList=getCallPhoneList();
        for(int i=0;i<callNumberList.size();i++){
            String Name = callNumberList.get(i).getName();
            if(Pinyin.toPinyin(Name,"").equals(name)){
                return callNumberList.get(i).getNumber();
            }
        }
        return "Not Found";
    }

    public String search(String name){
        callNumberList=getCallPhoneList();
        for(int i=0;i<callNumberList.size();i++){
            String Name = callNumberList.get(i).getName();
            if(Name!=null){
                if(Name.equals(name)){
                    return callNumberList.get(i).getNumber();
                }
            }
        }
        return "Not Found";
    }

    public void addContact(String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = MyApplicaton.getContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        // 向联系人URI添加联系人名字
        MyApplicaton.getContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        MyApplicaton.getContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
    }
}
