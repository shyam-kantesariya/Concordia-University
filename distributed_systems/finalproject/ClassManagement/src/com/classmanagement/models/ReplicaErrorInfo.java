package com.classmanagement.models;

/**
 * Created by prasanth on 8/5/2017.
 */
public class ReplicaErrorInfo {

    public ServerErrorInfo MTLserverErrorInfo ;
    public ServerErrorInfo LVLserverErrorInfo ;
    public ServerErrorInfo DDOserverErrorInfo ;

    public ReplicaErrorInfo() {
        MTLserverErrorInfo = new ServerErrorInfo();
        LVLserverErrorInfo = new ServerErrorInfo();
        DDOserverErrorInfo = new ServerErrorInfo();
    }
}
