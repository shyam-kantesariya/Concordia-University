package experiment;

/**
 * Created by Sparta on 5/29/2017.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.apache.commons.lang.SerializationUtils;

public class WriteObjectToFile {
    public static void main(String[] args){
        /*Person ob1 = new Person(1,"shyam", "Patel");
        Person ob2 = new Person(2,"shyamkumar", "Kantesariya");
        FileOutputStream fout = null;
        byte[] obj1 = SerializationUtils.serialize(ob1);
        byte[] obj2 = SerializationUtils.serialize(ob2);
        System.out.println("Size of obj1 is: " + obj1.length);
        System.out.println("Size of obj2 is: " + obj2.length);
        */
        try {
            DatagramSocket sc = new DatagramSocket(123);
            System.out.println("Port is free to use");
            System.out.println("Address is: " +  sc.getInetAddress());
            System.out.println("Address is: " +  sc.getLocalAddress());
            System.out.println("Address is: " +  sc.getLocalSocketAddress());
            System.out.println("Address is: " +  sc.getRemoteSocketAddress());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Port is NOT free to use");
        }
        Location lc = Location.MTL;
        System.out.println(lc.toString());
    }
}
