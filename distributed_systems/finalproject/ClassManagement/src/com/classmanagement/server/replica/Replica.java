package com.classmanagement.server.replica;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.Location;
import com.classmanagement.server.center.servant.CenterServant;
import com.classmanagement.server.center.servant.CenterServantInterface;
import com.classmanagement.server.center.servant.Implementation2.SMS_implementation;
import com.classmanagement.server.center.servant.Implementation2.utils.Recreate_hashMap;
import com.classmanagement.server.udp.multicast.UdpMulticastReceiver;

import java.io.*;
import java.net.UnknownHostException;

import static com.classmanagement.configuration.Constants.REPLICA_ONE;
import static com.classmanagement.configuration.Constants.REPLICA_TWO;
import org.apache.commons.lang3.*;


//Implementation specific Replica holder class

public class Replica {

    public CenterServantInterface mtlServant ;
    public CenterServantInterface lvlServant ;
    public CenterServantInterface ddoServant ;
    private UdpMulticastReceiver replicaUdpListenerThread;
    public String replicaId;

    public Replica(String replicaId) throws UnknownHostException {
        this.replicaId = replicaId;
        this.create();
        replicaUdpListenerThread = new UdpMulticastReceiver(this);
    }

    //Create three replicas each one for separate server MTL, LVL, DDO based on replica ID
    private void create() {
        try {
            if(this.replicaId.equals(REPLICA_ONE) || this.replicaId.equals(REPLICA_TWO)) {
                //Implementation 1
                mtlServant = new CenterServant(Location.MTL, replicaId);
                lvlServant = new CenterServant(Location.LVL, replicaId);
                ddoServant = new CenterServant(Location.DDO, replicaId);
            }
            else {
                //Implementation 2
                mtlServant = new SMS_implementation(Location.MTL.toString(), replicaId);
                lvlServant = new SMS_implementation(Location.LVL.toString(), replicaId);
                ddoServant = new SMS_implementation(Location.DDO.toString(), replicaId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //In case of any genuine error, shutdown the replica and restart it gracefully
    public void shutDown(String serverName, String replicaId) {
        try {
            if (!replicaId.equals(Constants.REPLICA_THREE)){
                //this.replicaUdpListenerThread.setShouldStop(true);
                if (serverName.equals("MTL")) {
                    this.mtlServant.shutDown();
                    this.mtlServant = new CenterServant(Location.LVL, this.replicaId);
                    System.out.println("Created new server for Montreal at " + this.replicaId);
                } else if (serverName.equals("LVL")) {
                    this.lvlServant.shutDown();
                    this.lvlServant = new CenterServant(Location.LVL, this.replicaId);
                    System.out.println("Created new server for Laval at " + this.replicaId);
                } else if (serverName.equals("DDO")) {
                    this.ddoServant.shutDown();
                    this.ddoServant = new CenterServant(Location.DDO, this.replicaId);
                    System.out.println("Created new server for DDO at " + this.replicaId);
                } else {
                    this.mtlServant.shutDown();
                    this.lvlServant.shutDown();
                    this.ddoServant.shutDown();
                    this.mtlServant = new CenterServant(Location.MTL, this.replicaId);
                    this.lvlServant = new CenterServant(Location.LVL, this.replicaId);
                    this.ddoServant = new CenterServant(Location.DDO, this.replicaId);
                    System.out.println("Created new server for MTL at " + this.replicaId);
                    System.out.println("Created new server for LVL at " + this.replicaId);
                    System.out.println("Created new server for DDO at " + this.replicaId);
                }
            } else {
                this.replicaUdpListenerThread.setShouldStop(true);
                if (serverName.equals("MTL")) {
                    this.mtlServant.shutDown();
                    String filePath = com.classmanagement.server.center.servant.Implementation2.configuration.Constants.PROJECT_DIR +
                            "\\" + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_DIR + "\\"
                            + "MTL" + "\\" + replicaId + "_"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    Recreate_hashMap creator =  new Recreate_hashMap(copyFilePath, Location.MTL, replicaId);
                    this.mtlServant = creator.execute();
                    System.out.println("Created new server for Montreal at " + this.replicaId);
                } else if (serverName.equals("LVL")) {
                    this.lvlServant.shutDown();
                    String filePath = com.classmanagement.server.center.servant.Implementation2.configuration.Constants.PROJECT_DIR +
                            "\\" + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_DIR + "\\"
                            + "LVL" + "\\" + replicaId + "_"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    this.lvlServant = new SMS_implementation(Location.LVL.toString(), replicaId);;
                    System.out.println("Created new server for Laval at " + this.replicaId);
                } else if (serverName.equals("DDO")) {
                    this.ddoServant.shutDown();
                    String filePath = com.classmanagement.server.center.servant.Implementation2.configuration.Constants.PROJECT_DIR +
                            "\\" + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.BACKUP_DIR_NAME + "\\"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_DIR +
                            "\\" + "DDO" + "\\" + replicaId + "_"
                            + com.classmanagement.server.center.servant.Implementation2.configuration.Constants.JOURNAL_FILE_NAME;
                    String copyFilePath = filePath + "_temp";
                    copyFile(filePath, copyFilePath);
                    Thread.sleep(2000);
                    this.ddoServant = new SMS_implementation(Location.DDO.toString(), replicaId);
                    System.out.println("Created new server for DDO at " + this.replicaId);
                } else {
                    this.mtlServant.shutDown();
                    this.lvlServant.shutDown();
                    this.ddoServant.shutDown();
                    this.mtlServant = new SMS_implementation(Location.MTL.toString(), replicaId);
                    this.lvlServant = new SMS_implementation(Location.LVL.toString(), replicaId);
                    this.ddoServant = new SMS_implementation(Location.DDO.toString(), replicaId);
                    System.out.println("Created new server for MTL at " + this.replicaId);
                    System.out.println("Created new server for LVL at " + this.replicaId);
                    System.out.println("Created new server for DDO at " + this.replicaId);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Copy existing journal file to new temporary file
    //https://www.mkyong.com/java/how-to-copy-file-in-java/
    private void copyFile(String source, String dest){
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            File sourceFile = new File(source);
            File destFile = new File(dest);

            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inStream != null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
