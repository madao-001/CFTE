package com.example.administrator.cfte.Model;

public class JingjiCallNumberSingleton {

    private static volatile JingjiCallNumberSingleton callNumberSingleton;
    private CallNumber callNumber;

    private JingjiCallNumberSingleton() {}

    public static JingjiCallNumberSingleton getInstance() {
        if (callNumberSingleton == null) {
            synchronized (JingjiCallNumberSingleton.class) {
                if (callNumberSingleton == null) {
                    callNumberSingleton = new JingjiCallNumberSingleton();
                }
            }
        }
        return callNumberSingleton;
    }

    public CallNumber getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(CallNumber callNumber) {
        this.callNumber = callNumber;
    }
}
