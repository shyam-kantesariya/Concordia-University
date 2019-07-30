package com.classmanagement.client.corba.stub;


import com.classmanagement.exceptions.*;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.utils.corba.helper.*;

public class _CenterServerStub extends org.omg.CORBA.portable.ObjectImpl implements CenterServer
{

  public String createTRecord (String firstName, String lastName, String address, String phone, String specialization, String location, String managerId) throws InvalidLastNameException
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createTRecord", true);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (address);
                $out.write_string (phone);
                $out.write_string (specialization);
                $out.write_string (location);
                $out.write_string (managerId);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:com/classmanagement/exceptions/InvalidLastNameException:1.0"))
                    throw InvalidLastNameExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createTRecord (firstName, lastName, address, phone, specialization, location, managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // createTRecord

  public String createSRecord (String firstName, String lastName, String coursesRegistered, String status, String statusDate, String managerId) throws InvalidLastNameException
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createSRecord", true);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (coursesRegistered);
                $out.write_string (status);
                $out.write_string (statusDate);
                $out.write_string (managerId);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:com/classmanagement/exceptions/InvalidLastNameException:1.0"))
                    throw InvalidLastNameExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createSRecord (firstName, lastName, coursesRegistered, status, statusDate, managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // createSRecord

  public String getRecordCounts (String managerId)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getRecordCounts", true);
                $out.write_string (managerId);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getRecordCounts (managerId );
            } finally {
                _releaseReply ($in);
            }
  } // getRecordCounts

  public void editRecord (String recordId, String fieldName, String newValue, String managerId) throws FieldNotFoundException, RecordNotFoundException, InvalidValueException
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editRecord", true);
                $out.write_string (recordId);
                $out.write_string (fieldName);
                $out.write_string (newValue);
                $out.write_string (managerId);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:com/classmanagement/exceptions/FieldNotFoundException:1.0"))
                    throw FieldNotFoundExceptionHelper.read ($in);
                else if (_id.equals ("IDL:com/classmanagement/exceptions/RecordNotFoundException:1.0"))
                    throw RecordNotFoundExceptionHelper.read ($in);
                else if (_id.equals ("IDL:com/classmanagement/exceptions/InvalidValueException:1.0"))
                    throw InvalidValueExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                editRecord (recordId, fieldName, newValue, managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // editRecord

  public String show (String recordId) throws RecordNotFoundException
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("show", true);
                $out.write_string (recordId);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:com/classmanagement/exceptions/RecordNotFoundException:1.0"))
                    throw RecordNotFoundExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return show (recordId        );
            } finally {
                _releaseReply ($in);
            }
  } // show

  public String transferRecord (String managerId, String recordId, String remoteCenterServerName) throws
          RecordNotFoundException, RecordBeingTransferred
  {
      org.omg.CORBA.portable.InputStream $in = null;
      try {
          org.omg.CORBA.portable.OutputStream $out = _request ("transferRecord", true);
          $out.write_string (managerId);
          $out.write_string (recordId);
          $out.write_string (remoteCenterServerName);
          $in = _invoke ($out);
          String $result = $in.read_string ();
          return $result;
      } catch (org.omg.CORBA.portable.ApplicationException $ex) {
          $in = $ex.getInputStream ();
          String _id = $ex.getId ();
                if (_id.equals ("IDL:com/classmanagement/exceptions/RecordNotFoundException:1.0"))
                    throw RecordNotFoundExceptionHelper.read ($in);
                else if(_id.equals ("IDL:com/classmanagement/exceptions/RecordBeingTransferred:1.0"))
                    throw RecordBeingTransferredHelper.read($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return transferRecord (managerId, recordId, remoteCenterServerName        );
            } finally {
                _releaseReply ($in);
            }
  } // transferRecord

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:com/classmanagement/interfaces/CenterServer:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _CenterServerStub
