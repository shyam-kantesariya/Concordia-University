package CenterServerIdl.CenterServerPackage;

/**
* CenterServerIdl/CenterServerPackage/FieldNotFoundExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Tuesday, July 11, 2017 2:22:36 PM EDT
*/

public final class FieldNotFoundExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public CenterServerIdl.CenterServerPackage.FieldNotFoundException value = null;

  public FieldNotFoundExceptionHolder ()
  {
  }

  public FieldNotFoundExceptionHolder (CenterServerIdl.CenterServerPackage.FieldNotFoundException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CenterServerIdl.CenterServerPackage.FieldNotFoundExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CenterServerIdl.CenterServerPackage.FieldNotFoundExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CenterServerIdl.CenterServerPackage.FieldNotFoundExceptionHelper.type ();
  }

}
