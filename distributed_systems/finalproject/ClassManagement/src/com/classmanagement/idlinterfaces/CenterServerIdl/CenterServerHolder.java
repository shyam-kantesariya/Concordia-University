package CenterServerIdl;

/**
* CenterServerIdl/CenterServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Tuesday, July 11, 2017 2:22:36 PM EDT
*/

public final class CenterServerHolder implements org.omg.CORBA.portable.Streamable
{
  public CenterServerIdl.CenterServer value = null;

  public CenterServerHolder ()
  {
  }

  public CenterServerHolder (CenterServerIdl.CenterServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CenterServerIdl.CenterServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CenterServerIdl.CenterServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CenterServerIdl.CenterServerHelper.type ();
  }

}