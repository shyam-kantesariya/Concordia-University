package client;

import HelloApp.Hello;
import HelloApp.HelloHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.util.Properties;

/**
 * Created by Sparta on 6/13/2017.
 */
public class HelloClient {
    static Hello helloImpl;
    public static void main(String[] args){

        /* Initialize System properties.
        following is the replacement of commandline argument "-ORBInitialHost localhost -ORBInitialPort 1050"
         */
        Properties props = System.getProperties();
        props.put("org.omg.CORBA.ORBInitialPort", "1050");
        props.put("org.omg.CORBA.ORBInitialHost", "localhost");

        //Get ORB reference
        ORB orb = ORB.init(args, null);
        try {
            //retrieve Naming Service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            //get the remote object reference proxy by using resolve method of Naming service
            helloImpl = HelloHelper.narrow(ncRef.resolve_str("Hello"));

            //Call sayHello remote method
            System.out.println(helloImpl.sayHello());
        } catch (org.omg.CORBA.SystemException e){
            e.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        }

    }
}
