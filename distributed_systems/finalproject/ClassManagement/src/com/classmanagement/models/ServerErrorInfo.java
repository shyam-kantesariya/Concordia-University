package com.classmanagement.models;

/**
 * Created by prasanth on 8/5/2017.
 */
public class ServerErrorInfo {
    private int Count;
    private String RequestId;

    public ServerErrorInfo() {
        setCount(0);
        setRequestId("");
    }

    public ServerErrorInfo(int count, String requestId) {
        setCount(count);
        setRequestId(requestId);
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public void increment() {
        setCount(getCount() + 1);
    }
}
