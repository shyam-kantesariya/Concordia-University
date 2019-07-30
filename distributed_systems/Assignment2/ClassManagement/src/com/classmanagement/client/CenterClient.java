package com.classmanagement.client;

import com.classmanagement.exceptions.ManagerInvalidException;
import com.classmanagement.models.Status;

import java.util.HashSet;
import java.util.Iterator;

import static com.classmanagement.models.Location.MTL;

/**
 * This class is for demonstration purpose.
 *
 * It will create multiple threads and send requests to the CORBA servers
 * and perform adding/editing of records
 */
public class CenterClient implements Runnable {

    private String managerID;

    public CenterClient(String managerId) {
        this.managerID = managerId;
    }

    public static void main(String args[]) throws Exception {
        HashSet<String> managers = new HashSet<>();
        managers.add("MTL00001");
        managers.add("MTL00002");
        managers.add("MTL00003");

        for (Iterator<String> i = managers.iterator(); i.hasNext();) {
            String managerId = i.next();
            CenterClient centerClient = new CenterClient(managerId);
            Thread thread = new Thread(centerClient);
            thread.start();
            System.out.println("Thread Started");
        }

        Thread.sleep(5000);
        CenterClientManager centerClientManager = new CenterClientManager(args,"MTL00001");
        System.out.println("#~~~~~~~ FINAL RESULT ~~~~~~~~#");
        System.out.println(centerClientManager.getRecordCounts("MTL00001"));
        System.out.println("#~~~~~~~ FINAL RESULT END ~~~~~~~~#");
    }

    public void run() {
        try {
            String[] args = new String[1];
            CenterClientManager centerClientManager = new CenterClientManager(args, this.managerID);

            centerClientManager.createSRecord("Prasanth", "AJ", ",java", Status.Active, "abc", managerID);
            centerClientManager.editRecord("SR00001", "status", "InActive", this.managerID);

            centerClientManager.createTRecord("Shyam", "Kan", "Montreal", "12345", "english", MTL,this.managerID);
            centerClientManager.editRecord("TR00001", "address", "Toronto", managerID);

            centerClientManager.transferRecord(this.managerID, "SR00001", "DDO");

        } catch (ManagerInvalidException e) {
            System.out.println("ERROR!!!!!!   Manager Not Found;" + managerID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("********************");
            System.out.println();
        }
    }
}