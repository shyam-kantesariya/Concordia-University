package com.classmanagement.models;

import com.classmanagement.configuration.ServerActionConstants;
import com.classmanagement.exceptions.ExceptionList;

/**
 * Created by Sparta on 7/24/2017.
 */
public class Result {
    public String result;
    public String senderId;
    public String responseSeverity;

    public Result(String result, String senderId, String responseSeverity){
        this.result = result;
        this.senderId = senderId;
        this.responseSeverity = responseSeverity;
    }

    public UdpResponseType compare(Result res1) {
        if(res1.result.equals(this.result)){
            return UdpResponseType.OK;
        } else {
            return UdpResponseType.ERROR;
        }
    }
}
