package com.classmanagement.server.center;

import com.classmanagement.models.Location;

import java.net.InetAddress;
import java.util.Date;

/**
 * Record general information about Center Server
 */

public class CenterServerInfo {
    private String locDescription;
    private Location location;
    private int udpPort;
    private InetAddress ipAddress;
    private Date startDateTime;
    private int frontEndUdpPort;

    public CenterServerInfo(Location loc, int udpPort, InetAddress ipAddress, Date startDateTime){
        location = loc;
        switch(loc){
            case MTL:
                this.locDescription = "Montreal";
                break;
            case LVL:
                this.locDescription = "Laval";
                break;
            case DDO:
                this.locDescription = "Dollard-des-Ormeaux";
                break;
            default:
                //TODO: handle exception
        }
        this.udpPort = udpPort;
        this.ipAddress = ipAddress;
        this.startDateTime = startDateTime;
    }

    /**
     * @return Descriptive name of location
     */
    public String getLocationDescription(){ return locDescription; }

    /**
     * @return Location code of server
     */
    public Location getLocation() { return location; }

    /**
     * @return port number server is accepting UDP request
     */
    public int getUdpPort() { return udpPort; }

    /**
     * @return IP address of server to send UDP request
     */
    public InetAddress getInetAddress() { return ipAddress; }

    /**
     * @return datetime information when server is started
     */
    public Date getStartDateTime() { return startDateTime; }
}