package com.classmanagement.server.replica;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.Location;
import com.classmanagement.models.ReplicaErrorInfo;
import com.classmanagement.models.ServerErrorInfo;
import com.classmanagement.models.Status;
import com.classmanagement.server.center.servant.Implementation2.SMS_implementation;

import java.util.HashMap;
import java.util.Locale;

import static com.classmanagement.configuration.Constants.REPLICA_ONE;
import static com.classmanagement.configuration.Constants.REPLICA_THREE;
import static com.classmanagement.configuration.Constants.REPLICA_TWO;
import static com.classmanagement.models.Location.MTL;

// Class to implemenet Replica Manager
public class ReplicaManager {

    private Replica replicaFirst;
    private Replica replicaSecond;
    private Replica replicaThird;
    private ReplicaManagerUdpReceiver replicaManagerUdpReceiver;
    private Thread udpThread;

    public static void main(String[] args) throws Exception {
        ReplicaManager replicaManager = new ReplicaManager();
    }

    public ReplicaManager(){
        this.Create();
        this.replicaManagerUdpReceiver = new ReplicaManagerUdpReceiver(Constants.REPLICA_MANAGER_HOST_NAME,
                Constants.REPLICA_MANAGER_PORT, this);
        this.udpThread = new Thread(this.replicaManagerUdpReceiver);
        this.udpThread.start();
    }
    private HashMap<String,ReplicaErrorInfo> errors = new HashMap<String, ReplicaErrorInfo>();

    //Create three replicas
    public void Create() {
        try {
            replicaFirst = new Replica(REPLICA_ONE);
            replicaSecond = new Replica(REPLICA_TWO);
            replicaThird = new Replica(REPLICA_THREE);
            errors.put(REPLICA_ONE, new ReplicaErrorInfo());
            errors.put(REPLICA_TWO, new ReplicaErrorInfo());
            errors.put(REPLICA_THREE, new ReplicaErrorInfo());
        }
        catch (Exception e) {
            //TODO: HANDLE IT
        }
    }

    //Process any compaints received from FE
    public void complain(String replicaId, String serverName, String requestId) {
        serverName = serverName.substring(0,3);
        System.out.println("Complaint recieved");
        ReplicaErrorInfo replicaErrorInfo = errors.get(replicaId);
        if(replicaErrorInfo != null) {
            ServerErrorInfo serverErrorInfo = getServerErrorInfo(replicaErrorInfo, serverName);
            if(serverErrorInfo != null) {
                if (isNextRequestId(serverErrorInfo.getRequestId(), requestId)) {
                    if (serverErrorInfo.getCount() == 2) {
                        restartReplica(replicaId, serverName);
                        resetServerErrorInfo(replicaErrorInfo, serverName);
                    } else {
                        serverErrorInfo.increment();
                        serverErrorInfo.setRequestId(requestId);
                    }
                }
                else {
                    serverErrorInfo.setCount(1);
                    serverErrorInfo.setRequestId(requestId);
                }
            }
        }
        else {
            errors.put(replicaId, new ReplicaErrorInfo());
        }
    }

    private ServerErrorInfo getServerErrorInfo(ReplicaErrorInfo replicaErrorInfo, String serverName) {
        if(serverName.equals("MTL")) {
            return replicaErrorInfo.MTLserverErrorInfo;
        }
        if(serverName.equals("LVL")) {
            return replicaErrorInfo.LVLserverErrorInfo;
        }
        if(serverName.equals("DDO")) {
            return replicaErrorInfo.DDOserverErrorInfo;
        }
        return null;
    }

    private void resetServerErrorInfo(ReplicaErrorInfo replicaErrorInfo, String serverName) {
        if(serverName.equals("MTL")) {
            replicaErrorInfo.MTLserverErrorInfo = new ServerErrorInfo();
        }
        if(serverName.equals("LVL")) {
            replicaErrorInfo.LVLserverErrorInfo  = new ServerErrorInfo();
        }
        if(serverName.equals("DDO")) {
            replicaErrorInfo.DDOserverErrorInfo  = new ServerErrorInfo();
        }
    }

    private boolean isNextRequestId(String currentRequestId, String newRequestId) {
        if(!currentRequestId.equals("") && !newRequestId.equals("")) {
            int currentRequestIdNum = Integer.parseInt(currentRequestId.substring(3));
            int newRequestIdNum = Integer.parseInt(newRequestId.substring(3));
            if((newRequestIdNum - currentRequestIdNum) == 1) {
                return true;
            }
        }
        return  false;
    }

    //Restart the replica
    public void restartReplica(String replicaId, String serverName){
        try {
            System.out.println("Restarting " + serverName + " Replica for: " + replicaId );
            if (replicaId.equals(REPLICA_ONE)) {
                System.out.println("Shutting down Replica one");
                replicaFirst.shutDown(serverName,replicaId);
            }
            if (replicaId.equals(REPLICA_TWO)) {
                System.out.println("Shutting down Replica two");
                replicaSecond.shutDown(serverName, replicaId);
            }
            if (replicaId.equals(REPLICA_THREE)) {
                System.out.println("Shutting down Replica three");
                replicaThird.shutDown(serverName, replicaId);
            }
        }
        catch (Exception e) {
            //TODO: HANDLE IT
        }
    }
}
