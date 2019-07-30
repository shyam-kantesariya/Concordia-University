package com.classmanagement.exceptions;


import com.classmanagement.utils.corba.helper.InvalidValueExceptionHelper;

/**
* CenterServerIdl/CenterServerPackage/InvalidValueException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Sunday, June 18, 2017 12:56:33 PM EDT
*/

public final class InvalidValueException extends org.omg.CORBA.UserException
{

  public InvalidValueException ()
  {
    super(InvalidValueExceptionHelper.id());
  } // ctor


  public InvalidValueException (String $reason)
  {
    super(InvalidValueExceptionHelper.id() + "  " + $reason);
  } // ctor

} // class InvalidValueException
