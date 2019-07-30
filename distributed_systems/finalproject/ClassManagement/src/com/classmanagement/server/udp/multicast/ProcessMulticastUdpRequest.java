package com.classmanagement.server.udp.multicast;

import com.classmanagement.models.Location;
import com.classmanagement.server.center.servant.CenterServant;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Created by Sparta on 7/23/2017.
 */
public class ProcessMulticastUdpRequest implements Runnable{
    CenterServant servant;
    DatagramPacket dataPacket;
    public ProcessMulticastUdpRequest(CenterServant servant, DatagramPacket dataPacket){
        this.servant = servant;
        this.dataPacket = dataPacket;
    }
    public void run(){
        //http://docs.oracle.com/javase/6/docs/api/java/util/Arrays.html#copyOfRange%28byte[],%20int,%20int%29
        Location location = Location.valueOf(new String(Arrays.copyOfRange(dataPacket.getData(), 0, 3)));
        JSONParser parser = new JSONParser();
        if(location == servant.serverLocation){
            byte[] data = Arrays.copyOfRange(dataPacket.getData(),3, dataPacket.getData().length);
            try {
                JSONObject jsondata = (JSONObject) parser.parse(data.toString());
                String requestId = jsondata.get("requestId").toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else{
            System.out.println("Ignore request");
        }
    }
}
