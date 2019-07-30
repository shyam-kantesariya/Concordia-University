package com.classmanagement.server.center;

import com.classmanagement.configuration.Constants;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.models.Location;
import com.classmanagement.utils.PropertyManager;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import javax.xml.ws.Endpoint;
import java.io.IOException;

/**
 * Entry point to the server side application
 */
public class CenterServerManager {


    /** Create separate server instance for each location
     * Register those to the registry service to expose interface APIs
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Endpoint mtlEndPoint = Endpoint.publish("http://localhost:8080/MTL", new CenterServerImp(Location.MTL));
        Endpoint lvlEndPoint = Endpoint.publish("http://localhost:8080/LVL", new CenterServerImp(Location.LVL));
        Endpoint ddoEndPoint = Endpoint.publish("http://localhost:8080/DDO", new CenterServerImp(Location.DDO));
        System.out.println("Published MTL service: " + mtlEndPoint.isPublished());
        System.out.println("Published LVL service: " + lvlEndPoint.isPublished());
        System.out.println("Published DDO service: " + ddoEndPoint.isPublished());

        System.out.println("#================= Server is starting =================#");
    }
}