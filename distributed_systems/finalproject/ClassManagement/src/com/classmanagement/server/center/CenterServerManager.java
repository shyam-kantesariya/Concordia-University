package com.classmanagement.server.center;

import com.classmanagement.configuration.Constants;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.server.center.frontend.CenterServerImp;
import com.classmanagement.utils.PropertyManager;
import com.classmanagement.utils.corba.helper.CenterServerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import java.io.IOException;

/**
 * Entry point to the server side application
 */
public class CenterServerManager {

    static {
         /*Start CORBA services on localhost
        Alternatively you can go and run mentioned command on command prompt as well
        REF: http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Runtime.html#getRuntime()
            https://stackoverflow.com/questions/7112259/how-to-execute-windows-commands-using-java-change-network-settings
            http://www.java-samples.com/showtutorial.php?tutorialid=8
         */
        System.out.println("Starting CORBA on cmd");
        try {
            Runtime.getRuntime().exec("orbd -ORBInitialPort 1050 -ORBInitialHost localhost");
            //Following command doesn't give any error during execution but it seems on activating service in real
            // cmd start orbd -ORBInitialPort 1050 -ORBInitialHost localhost
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Started CORBA on cmd");
    }

    /** Create separate server instance for each location
     * Register those to the registry service to expose interface APIs
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CenterServerImp frontEnd = new CenterServerImp();

        ORB orb = ORB.init(args, PropertyManager.getProperties());
        POA rootPoa = (POA) orb.resolve_initial_references("RootPOA");
        rootPoa.the_POAManager().activate();

        org.omg.CORBA.Object frontEndCorbaRef = rootPoa.servant_to_reference(frontEnd);
        CenterServer feRef = CenterServerHelper.narrow(frontEndCorbaRef);

        org.omg.CORBA.Object nameServiceCorbaRef = orb.resolve_initial_references("NameService");
        NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceCorbaRef);

        NameComponent fePath[] = nameService.to_name(Constants.FRONT_END_CORBA_OBJ_NAME);
        nameService.rebind(fePath, feRef );

        System.out.println("#================= Server is starting =================#");

        orb.run();
    }
}