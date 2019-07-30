package ipc.tcp;

import java.io.*;
import java.net.Socket;

/**
 * Created by Sparta on 5/20/2017.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket sc = null; // Need to initialize as we are closing in finally block
        BufferedReader bf = null;
        DataOutputStream dout = null;
        try{
            //Binding socket to 50001 port number on localhost
            sc = new Socket("localhost",50001);

            /* DataOutputStream: Writes any primitive type to an output stream
            It accepts any OutputStream object as constructor parameter to write on.
            getOutputStream method of Socket class will return OutputStream object
            attached to that socket instance
            */
            dout = new DataOutputStream(sc.getOutputStream());

            //Sends data to the other end.
            dout.writeUTF("Hello Server" + "\n");

            /*getInputStream method of Socket class will return InputStream attached to
            that socket instance.
            InputStreamReader is bridge to bytes stream to character stream.
            Now as that InputStreamReader can read each character, we need a buffer to hold all of them
            hence use BufferedReader for that purpose
             */
            bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));

            //Print data received from other end on local console
            System.out.println("Message from Server: " + bf.readLine());

            /*We can use BufferedWriter as well to write data, following code snippet demonstrate that:

            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
            bfw.write("Hello World");
            bfw.newLine();
            bfw.flush();
             */
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            /* Close all IO streams
            Following both statements may throw an IOException, thats why throws statement
            of main method has been included
            */
            bf.close();
            sc.close();
            dout.close();
        }
    }
}