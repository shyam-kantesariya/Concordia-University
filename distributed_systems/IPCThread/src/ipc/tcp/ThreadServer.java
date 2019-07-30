package ipc.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sparta on 5/20/2017.
 */

public class ThreadServer {
    public static void main(String[] args){
        ServerSocket sc = null;
        Socket scc = null;
        try{
            /* Bind ServerSocket to 50001 port on localhost.
            By default it accepts maximum 50 connection in queue. Any connection request later to that
            will receive connection refused error, however you can modify this limit as required
            It has one more constructor definition which accept an IP address as well.
            That is useful when you have server machine running with multiple network hosts.
            */
            sc = new ServerSocket(50001);
            while (true){
                try {
                    //Wait for a client connection
                    scc = sc.accept();
                    System.out.println("Local socket address is: " + scc.getLocalSocketAddress());
                    new Worker(scc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sc.close();
                scc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
