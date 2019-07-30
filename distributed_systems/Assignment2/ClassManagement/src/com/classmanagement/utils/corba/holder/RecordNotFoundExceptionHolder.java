package com.classmanagement.utils.corba.holder;

import com.classmanagement.exceptions.RecordNotFoundException;
import com.classmanagement.utils.corba.helper.RecordNotFoundExceptionHelper;

/**
* CenterServerIdl/CenterServerPackage/RecordNotFoundExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Sunday, June 18, 2017 12:56:33 PM EDT
*/

public final class RecordNotFoundExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public RecordNotFoundException value = null;

  public RecordNotFoundExceptionHolder ()
  {
  }

  public RecordNotFoundExceptionHolder (RecordNotFoundException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RecordNotFoundExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RecordNotFoundExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RecordNotFoundExceptionHelper.type ();
  }

}