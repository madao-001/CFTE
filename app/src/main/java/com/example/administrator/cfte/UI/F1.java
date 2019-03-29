package com.example.administrator.cfte.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.cfte.Model.CallNumber;
import com.example.administrator.cfte.Adapter.CallNumberAdapter;
import com.example.administrator.cfte.R;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.cfte.Utils.CallUtil;

/**
 * 通讯录屏
 */
public class F1 extends Fragment {


    private Context Context;
    //通讯录列表
    private List<CallNumber> callNumberList=new ArrayList<>();
    private CallUtil callUtil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f1, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Context=context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callUtil=new CallUtil(this.getActivity());
        callNumberList=callUtil.getCallPhoneList();
        CallNumberAdapter callNumberAdapter=new CallNumberAdapter(F1.this.getContext(), R.layout.callbtn,
                callNumberList);
        ListView listView=(ListView) getView().findViewById(R.id.call_list);
        listView.setAdapter(callNumberAdapter);
        //列表监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CallNumber callNumber=callNumberList.get(position);
                Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+callNumber.getNumber()));
                startActivity(intent);
            }
        });
    }

}
