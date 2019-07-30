package com.classmanagement.exceptions;


import com.classmanagement.utils.corba.helper.InvalidLastNameExceptionHelper;

/**
* CenterServerIdl/CenterServerPackage/InvalidLastNameException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Sunday, June 18, 2017 12:56:33 PM EDT
*/

public final class InvalidLastNameException extends org.omg.CORBA.UserException
{

  public InvalidLastNameException ()
  {
    super(InvalidLastNameExceptionHelper.id());
  } // ctor


  public InvalidLastNameException (String $reason)
  {
    super(InvalidLastNameExceptionHelper.id() + "  " + $reason);
  } // ctor

} // class InvalidLastNameException
