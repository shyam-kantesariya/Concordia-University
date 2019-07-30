package server;

import HelloApp.Hello;
import HelloApp.HelloHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * Created by Sparta on 6/13/2017.
 */
public class HelloServerWithIndividualExceptionDetails {
    public static void main(String[] args){
        ORB orb = ORB.init(args, null);
        POA poaRoot = null;
        try {
            poaRoot = (POA) orb.resolve_initial_references("RootPOA");
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        try {
            poaRoot.the_POAManager().activate();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        }
        HelloImpl helloImpl = new HelloImpl(orb);
        org.omg.CORBA.Object ref = null;
        try {
            ref = poaRoot.servant_to_reference(helloImpl);
        } catch (ServantNotActive servantNotActive) {
            servantNotActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            wrongPolicy.printStackTrace();
        }
        Hello href = HelloHelper.narrow(ref);
        org.omg.CORBA.Object nameService = null;
        try {
            nameService = orb.resolve_initial_references("NameService");
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        NamingContextExt ncRef = NamingContextExtHelper.narrow(nameService);
        String name = "Hello";
        try {
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path,href);
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }
        System.out.println("Server is up and running.....");
        orb.run();
    }
}
