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
import com.example.administrator.cfte.Model.Message;
import com.example.administrator.cfte.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private int MessageId;

    public MessageAdapter(@NonNull Context context, int ViewId, @NonNull List<Message> objects) {
        super(context, ViewId, objects);
        MessageId = ViewId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       Message message=getItem(position);
        View view;
        //用于增加响应速度
        if (convertView == null){
            view= LayoutInflater.from(getContext()).inflate(MessageId,parent,false);
        }else {
            view = convertView;
        }
        TextView message_name=(TextView)view.findViewById(R.id.message_name);
        TextView message_time=(TextView)view.findViewById(R.id.message_time);
        TextView message_all=(TextView)view.findViewById(R.id.message_all);
        message_name.setText(message.getMessage_name());
        message_time.setText(message.getMessage_time());
        message_all.setText(message.getMessage_all());
        return view;
    }
}
