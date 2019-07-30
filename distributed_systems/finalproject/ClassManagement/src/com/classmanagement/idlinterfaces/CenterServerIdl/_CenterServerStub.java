package CenterServerIdl;


/**
* CenterServerIdl/_CenterServerStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CenterServerIdl.idl
* Tuesday, July 11, 2017 2:22:36 PM EDT
*/

public class _CenterServerStub extends org.omg.CORBA.portable.ObjectImpl implements CenterServerIdl.CenterServer
{

  public String createTRecord (String firstName, String lastName, String address, String phone, String specialization, String location, String managerId) throws CenterServerIdl.CenterServerPackage.InvalidLastNameException
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
                if (_id.equals ("IDL:CenterServerIdl/CenterServer/InvalidLastNameException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.InvalidLastNameExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createTRecord (firstName, lastName, address, phone, specialization, location, managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // createTRecord

  public String createSRecord (String firstName, String lastName, String coursesRegistered, String status, String statusDate, String managerId) throws CenterServerIdl.CenterServerPackage.InvalidLastNameException
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
                if (_id.equals ("IDL:CenterServerIdl/CenterServer/InvalidLastNameException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.InvalidLastNameExceptionHelper.read ($in);
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
                return getRecordCounts (managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // getRecordCounts

  public void editRecord (String recordID, String fieldName, String newValue, String managerId) throws CenterServerIdl.CenterServerPackage.FieldNotFoundException, CenterServerIdl.CenterServerPackage.RecordNotFoundException, CenterServerIdl.CenterServerPackage.InvalidValueException
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editRecord", true);
                $out.write_string (recordID);
                $out.write_string (fieldName);
                $out.write_string (newValue);
                $out.write_string (managerId);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:CenterServerIdl/CenterServer/FieldNotFoundException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.FieldNotFoundExceptionHelper.read ($in);
                else if (_id.equals ("IDL:CenterServerIdl/CenterServer/RecordNotFoundException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.RecordNotFoundExceptionHelper.read ($in);
                else if (_id.equals ("IDL:CenterServerIdl/CenterServer/InvalidValueException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.InvalidValueExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                editRecord (recordID, fieldName, newValue, managerId        );
            } finally {
                _releaseReply ($in);
            }
  } // editRecord

  public String show (String recordId) throws CenterServerIdl.CenterServerPackage.RecordNotFoundException
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
                if (_id.equals ("IDL:CenterServerIdl/CenterServer/RecordNotFoundException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.RecordNotFoundExceptionHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return show (recordId        );
            } finally {
                _releaseReply ($in);
            }
  } // show

  public String transferRecord (String managerId, String recordId, String remoteCenterServerName) throws CenterServerIdl.CenterServerPackage.RecordNotFoundException, CenterServerIdl.CenterServerPackage.RecordBeingTransferred
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
                if (_id.equals ("IDL:CenterServerIdl/CenterServer/RecordNotFoundException:1.0"))
                    throw CenterServerIdl.CenterServerPackage.RecordNotFoundExceptionHelper.read ($in);
                else if (_id.equals ("IDL:CenterServerIdl/CenterServer/RecordBeingTransferred:1.0"))
                    throw CenterServerIdl.CenterServerPackage.RecordBeingTransferredHelper.read ($in);
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
    "IDL:CenterServerIdl/CenterServer:1.0"};

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