package ipc.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sparta on 5/20/2017.
 */

public class Worker extends Thread {
    Socket clientSocket;
    DataOutputStream dout = null;
    DataInputStream din = null;

    Worker(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            dout = new DataOutputStream(clientSocket.getOutputStream());
            din = new DataInputStream(clientSocket.getInputStream());
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            System.out.println("In run method");
            String in = din.readUTF();
            System.out.println("Data from Client is: " + in);
            dout.writeUTF("Hi from Server Thread");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                clientSocket.close();
                din.close();
                dout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}