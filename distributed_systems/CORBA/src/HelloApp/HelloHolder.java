package HelloApp;

/**
* HelloApp/HelloHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from HelloApp.idl
* Tuesday, June 13, 2017 12:38:56 PM EDT
*/

public final class HelloHolder implements org.omg.CORBA.portable.Streamable
{
  public HelloApp.Hello value = null;

  public HelloHolder ()
  {
  }

  public HelloHolder (HelloApp.Hello initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    //System.out.println(this.getClass().toString() + ": _read");
    value = HelloApp.HelloHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    //System.out.println(this.getClass().toString() + ": _write");
    HelloApp.HelloHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    //System.out.println(this.getClass().toString() + ": _type()");
    return HelloApp.HelloHelper.type ();
  }
}