package com.example.administrator.cfte.UI;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.cfte.Adapter.MessageAdapter;
import com.example.administrator.cfte.Model.Message;
import com.example.administrator.cfte.R;
import com.example.administrator.cfte.Utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private Context context;
    private List<Message> messageList=new ArrayList<>();
    private MessageUtil messageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageUtil=new MessageUtil(this);
        messageList=messageUtil.getMessages();
        MessageAdapter messageAdapter=new MessageAdapter(this,R.layout.message,messageList);
        ListView listView=(ListView)findViewById(R.id.message_list);
        listView.setAdapter(messageAdapter);
    }
}
