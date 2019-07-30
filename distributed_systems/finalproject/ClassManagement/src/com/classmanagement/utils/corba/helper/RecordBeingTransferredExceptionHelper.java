package com.classmanagement.utils.corba.helper;


import com.classmanagement.exceptions.RecordBeingTransferredException;

/**
* CenterServerIdl/CenterServerPackage/RecordBeingTransferredExceptionHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Tuesday, July 11, 2017 1:08:26 PM EDT
*/

abstract public class RecordBeingTransferredExceptionHelper
{
  private static String  _id = "IDL:CenterServerIdl/CenterServer/RecordBeingTransferredException:1.0";

  public static void insert (org.omg.CORBA.Any a, RecordBeingTransferredException that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RecordBeingTransferredException extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [0];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (RecordBeingTransferredExceptionHelper.id (), "RecordBeingTransferredException", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RecordBeingTransferredException read (org.omg.CORBA.portable.InputStream istream)
  {
    RecordBeingTransferredException value = new RecordBeingTransferredException ();
    // read and discard the repository ID
    istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RecordBeingTransferredException value)
  {
    // write the repository ID
    ostream.write_string (id ());
  }

}
