package com.classmanagement.client;

import com.classmanagement.exceptions.ManagerInvalidException;
import com.classmanagement.models.Location;
import com.classmanagement.models.Status;
import com.classmanagement.models.Student;
import com.sun.xml.internal.ws.api.message.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * This class gets user various inputs and facilitates a Manager to create/edit/view the
 * records
 */
public class CenterClientUI {
    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                String managerId = getUserInput("Manager ID");
                CenterClientManager centerClientManager = new CenterClientManager(managerId);
                showWelcomeMessage(centerClientManager.serverLocation);
                boolean shouldBreak = false;
                showHelp();
                while (true) {
                    try {
                        String userChoice = getUserInput("your choice");
                        switch (userChoice) {
                            case "1":
                                centerClientManager.createSRecord(getUserInput("first name"), getUserInput("last name"),
                                        getStudentCourses(), Status.valueOf(getUserInput("Status (Active/InActive)")), getUserInput("Date"));
                                break;
                            case "2":
                                centerClientManager.createTRecord(getUserInput("first name"), getUserInput("last name"),
                                        getUserInput("address"), getUserInput("phone"), getUserInput("specialization"),
                                        Location.valueOf(getUserInput("Location code (MTL/LVL/DDO)")));
                                break;
                            case "3":
                                centerClientManager.editRecord(getUserInput("Record Id"), getUserInput("field name"),
                                        getUserInput("new value"));
                                break;
                            case "4":
                                centerClientManager.getRecordCounts();
                                break;
                            case "5":
                                centerClientManager.show(getUserInput("Record Id"));
                                break;
                            case "6":
                                shouldBreak = true;
                                break;
                            default:
                                showHelp();

                        }
                        if (shouldBreak)
                            break;
                    }
                    catch (Exception e) {
                        System.out.println("ERROR!!!" + e.getMessage());
                    }
                }
            }
            catch (ManagerInvalidException e) {
                System.out.println("ERROR!!! The Manager ID is Invalid");
            }

            catch (Exception e) {
                System.out.println("ERROR!!!" + e.getMessage());
            }

        }

    }

    public static HashSet<String> getStudentCourses() throws IOException {
        String val = null;
        HashSet<String> courses = new HashSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter next course or done: ");
            val = br.readLine();
            if (!val.trim().equals("done")) courses.add(val);
            else break;
        }
        return courses;
    }

    public static void showHelp(){
        System.out.println();
        System.out.println("1. Add a Student");
        System.out.println("2. Add a Teacher");
        System.out.println("3. Edit a record");
        System.out.println("4. Get records count");
        System.out.println("5. Display a record");
        System.out.println("6. Log out");
        System.out.println();
    }

    public static void showWelcomeMessage(String serverLocation) {
        System.out.println();
        System.out.println("****************************************");
        System.out.println("Welcome to " + serverLocation);
        System.out.println("****************************************");
        System.out.println();
    }

    public static String getUserInput(String field) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter " + field + ": ");
        return br.readLine();
    }
}