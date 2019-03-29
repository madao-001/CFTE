package com.example.administrator.cfte.Model;

/**
 * Created by PolarBear on 2019/3/5.
 * 实体类
 * 通讯录的一条记录
 */

public class CallNumber {
    private String name;
    private String number;

    public CallNumber(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

}
