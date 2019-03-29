package com.example.administrator.cfte.Control;

import android.content.SharedPreferences;

import com.example.administrator.cfte.MyApplicaton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_WORLD_WRITEABLE;
import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

public class FileControl {
    public String read() {
        try {
            SharedPreferences read = MyApplicaton.getContext().getSharedPreferences("contact", MODE_WORLD_READABLE);
            String result = read.getString("ICE", "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String msg){
        // 步骤1：获取输入值
        if(msg == null) return;
        try {
            SharedPreferences.Editor editor = MyApplicaton.getContext().getSharedPreferences("contact", MODE_WORLD_WRITEABLE).edit();
            editor.putString("ICE", msg);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //判断文件是否存在
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }
        File f=new File(strFile);

        return true;
    }
}
