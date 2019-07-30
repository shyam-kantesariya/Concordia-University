package com.classmanagement.server.center;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.Location;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
        CenterServerImp mtlServer = new CenterServerImp(Location.MTL);
        CenterServerImp lvlServer = new CenterServerImp(Location.LVL);
        CenterServerImp ddoServer = new CenterServerImp(Location.DDO);
        Registry registry = LocateRegistry.createRegistry(Constants.REGISTRY_PORT);
        registry.bind("MontrealServer", mtlServer);
        registry.bind("LavalServer", lvlServer);
        registry.bind("DDOServer", ddoServer);
        System.out.println("#================= Server is up and running =================#");
    }
}