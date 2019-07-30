package server;

import HelloApp.*;
import org.omg.CORBA.ORB;

public class HelloImpl extends HelloPOA{
    private ORB orb;
    public HelloImpl(ORB orb){
        this.orb = orb;
    }
    public void setOrb(ORB orb){
        this.orb = orb;
    }
    public String sayHello(){
        System.out.println("Received a client Request");
        return "Hello World";
    }
}