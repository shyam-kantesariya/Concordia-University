package server;

import HelloApp.Hello;
import HelloApp.HelloHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.io.IOException;
import java.util.Properties;

//REF: All details of mentioned packages could be found at: https://docs.oracle.com/javase/7/docs/api/

/**
 * Created by Sparta on 6/13/2017.
 */
public class HelloServer {

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
            //Runtime.getRuntime().exec("java -version");
            //Following command doesn't give any error during execution but it seems on activating service in real
            // cmd start orbd -ORBInitialPort 1050 -ORBInitialHost localhost
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Started CORBA on cmd");
    }

    public static void main(String[] args) {

        /* Initialize System properties. Thus we can run the same program without remembering any command line arguments
        You may not configure anything while running the same code in IDE as well
        following is the replacement of commandline argument "-ORBInitialHost localhost -ORBInitialPort 1050"
        REF: http://docs.oracle.com/javase/7/docs/technotes/guides/idl/jidlExample3.html
         */
        Properties props = System.getProperties();
        props.put("org.omg.CORBA.ORBInitialPort", "1050");
        props.put("org.omg.CORBA.ORBInitialHost", "localhost");

        /*Initialize the CORBA ORB. There are multiple variants for init method.
        Following is to create new ORB object for an application.
        Similarly you can create one for Applet by passing Applet object in place of args.
        It returns singleton reference if no argument specified
        REF: https://docs.oracle.com/javase/7/docs/api/org/omg/CORBA/ORB.html
        */
        ORB orb = ORB.init(args, null);
        try {
            /* resolve_initial_reference method returns Service objects for the services provided by ORB.
            Following method returns RootPOA for this ORB object.
            RootPOA is nothing but a default POA reference offered by ORB, which doesn't allow to change any policy.
            You can create multiple child POA and set policies as per your need

            "implementations of CORBA provide interfaces to the functionality of
            the POA and the ORB core through pseudo-objects, given this name because they
            cannot be used like regular CORBA objects; for example, they cannot be passed as
            arguments in RMIs. They do, though, have IDL interfaces and are implemented as
            libraries. The POA pseudo-object includes, for example, one method for activating a
            POAmanager and another method, servant_to_reference, for registering a CORBA
            object; the ORB pseudo-object includes the method init, which must be called to
            initialize the ORB, the method resolve_initial_references, which is used to find services
            such as the Naming service and the root POA, and other methods"

            poaRoot is the pseudo-object as its not type of CORBA object that can be passed as a parameter to RMI

            REF: http://www.albany.edu/dept/csi/csi518/fall03/inprise/vbroker/doc/books/vbj/vbj45/programmers-guide/servers.html
             */
            POA poaRoot = (POA) orb.resolve_initial_references("RootPOA");

            //Activate POA Manager as default its in Holding state
            poaRoot.the_POAManager().activate();

            //Create Servant class Object
            HelloImpl helloImpl = new HelloImpl(orb);

            /*Register that servant object with POA to get an instance of CORBA object and remote object reference
            try running System.out.println(ref.getClass());
            It will return an object of class: com.sun.corba.se.impl.corba.CORBAObjectImpl
            If you print toString, it will show the IOR (Interoperable Object Reference) value

            "An object adapter gives each CORBA object a unique object name, which forms part of
            its remote object reference. The same name is used each time an object is activated. The
            object name may be specified by the application program or generated by the object
            adapter. Each CORBA object is registered with its object adapter, which keeps a remote
            object table that maps the names of CORBA objects to their servants."

            REF: http://www.pvv.ntnu.no/~ljosa/doc/encycmuclopedia/devenv/corba-index.html
                 https://stackoverflow.com/questions/1087083/what-is-an-ior-file-what-does-it-do-and-how-does-it-work
             */
            org.omg.CORBA.Object ref = poaRoot.servant_to_reference(helloImpl);

            /* Cast CORBA object to Java specific object using narrow method of Helper class
            It will return an object of class: HelloApp._HelloStub
            If you print toString result on this object it will show the IOR (Interoperable Object Reference) value same
            as for ref object
             */
            Hello href = HelloHelper.narrow(ref);

            //Resolve Name Service reference for ORB
            org.omg.CORBA.Object nameService = orb.resolve_initial_references("NameService");

            /*Get Naming Context reference using extended Naming Context class
            It allows to bind different names to an Object in the same or different context at the same time
             */
            NamingContextExt ncRef = NamingContextExtHelper.narrow(nameService);

            /* A NameComponent object consists of two fields:
            id -- a String used as an identifier
            kind -- a String that can be used for any descriptive purpose. Its importance is that it can be used to
                    describe an object without affecting syntax. The C programming language, for example, uses the
                    syntactic convention of appending the extension ".c" to a file name to indicate that it is a source
                    code file. In a NameComponent object, the kind field can be used to describe the type of object
                    rather than a file extension or some other syntactic convention. Examples of the value of the kind
                    field include the strings "c_source", "object_code", "executable", "postscript", and "". It is not
                    unusual for the kind field to be the empty string.
              Ref: https://docs.oracle.com/javase/7/docs/api/org/omg/CosNaming/package-summary.html

              Use following code to see the details of ID and Kind
              for(NameComponent x:path){
                System.out.println("id: " + x.id + " .... kind: " + x.kind);
            }
            */
            String name = "Hello";
            NameComponent path[] = ncRef.to_name(name);

            //Bind the reference to the Named string, which is "Hello" in our case
            ncRef.rebind(path, href);
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        } catch (ServantNotActive servantNotActive) {
            servantNotActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            wrongPolicy.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }

        System.out.println("Starting Server.....");
        //Start the ORB module
        orb.run();
    }
}