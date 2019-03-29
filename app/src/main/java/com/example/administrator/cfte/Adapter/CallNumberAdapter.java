package com.example.administrator.cfte.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.cfte.Model.CallNumber;
import com.example.administrator.cfte.R;

import java.util.List;

/**
 * Created by PolarBear on 2019/3/5.
 * 这是一个通讯录列表的适配器
 * 用于将数据绑定到UI上
 */

public class CallNumberAdapter extends ArrayAdapter<CallNumber> {
    private int callNumberId;

    public CallNumberAdapter(Context context,int ViewId,List<CallNumber> objects){
        super(context,ViewId,objects);
        callNumberId=ViewId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CallNumber callNumber=getItem(position);
        View view;
        //用于增加响应速度
        if (convertView == null){
            view=LayoutInflater.from(getContext()).inflate(callNumberId,parent,false);
        }else {
            view = convertView;
        }
        //用于数据绑定
        TextView call_name=(TextView)view.findViewById(R.id.call_name);
        TextView call_number=(TextView)view.findViewById(R.id.call_number);
        call_name.setText(callNumber.getName());
        call_number.setText(callNumber.getNumber());
        return view;
    }
}
