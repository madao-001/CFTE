package com.example.administrator.cfte.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;

/**
 * @author HHH
 *
 */
public class FileService {

    public Context context;

    public FileService(Context context) {
        super();
        this.context = context;
    }

    /**
     * 保存文件到手机内存中
     *
     * @param fileName
     * @param fileContent
     * @return
     * @throws Exception
     */
    public void saveToPhone(String fileName, String fileContent) throws Exception {

        OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
    }

    /**
     * 从手机中读取文件
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public String readFromPhone(String fileName) throws Exception {

        StringBuilder sBuilder = new StringBuilder();

        InputStream inputStream = context.openFileInput(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sBuilder.append(line);
        }
        bufferedReader.close();

        return sBuilder.toString();
    }
}