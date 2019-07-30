package com.classmanagement.client;

import com.classmanagement.exceptions.ManagerInvalidException;
import com.classmanagement.models.Status;

import java.util.HashSet;
import java.util.Iterator;

import static com.classmanagement.models.Location.MTL;

/**
 * This class is for demonstration purpose.
 *
 * It will create multiple threads and send requests to the RMI servers
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
        managers.add("MTL00004");
        managers.add("MTL00005");
        managers.add("MTL00006");
        managers.add("MTL00007");
        managers.add("LVL00001");
        managers.add("DDO00001");
        managers.add("JKL00001");

        for (Iterator<String> i = managers.iterator(); i.hasNext();) {
            String managerId = i.next();
            CenterClient centerClient = new CenterClient(managerId);
            //centerClient.run();
            Thread thread = new Thread(centerClient);
            thread.start();
            System.out.println("Thread Started");
        }

        Thread.sleep(5000);
        CenterClientManager centerClientManager = new CenterClientManager("MTL0010");
        System.out.println("#~~~~~~~ FINAL RESULT ~~~~~~~~#");
        System.out.println(centerClientManager.getRecordCounts());
        System.out.println("#~~~~~~~ FINAL RESULT END ~~~~~~~~#");
    }

    public void run() {
        try {
            CenterClientManager centerClientManager = new CenterClientManager(this.managerID);

            centerClientManager.createSRecord("Prasanth", "AJ", new HashSet<>(), Status.Active, "abc");
            centerClientManager.editRecord("ST00001", "status", "InActive");
            centerClientManager.editRecord("ST00001", "statusDate", "new Date");
            centerClientManager.editRecord("ST00001", "coursesRegistered", "English, french");
            centerClientManager.show("ST00001");

            centerClientManager.createTRecord("Shyam", "Kan", "Montreal", "12345", "english", MTL);
            centerClientManager.editRecord("TR00001", "address", "Toronto");
            centerClientManager.editRecord("TR00001", "phone", "54321");
            centerClientManager.editRecord("TR00001", "location", "DDO");
            centerClientManager.show("TR00001");

            centerClientManager.editRecord("TR00050", "address", "Toronto");
            centerClientManager.editRecord("TR00001", "addresss", "Toronto");
            centerClientManager.editRecord("TR00001", "location", "TOR");

            System.out.println(centerClientManager.getRecordCounts());
        } catch (ManagerInvalidException e) {
            System.out.println("ERROR!!!!!!   Manager Not Found;" + managerID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("********************");
            System.out.println("********************");
            System.out.println();
        }
    }
}
