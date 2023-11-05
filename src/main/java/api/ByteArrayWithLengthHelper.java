package api;


/**
* api/ByteArrayWithLengthHelper.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��3�� ������ ����12ʱ28��30�� CST
*/

abstract public class ByteArrayWithLengthHelper
{
  private static String  _id = "IDL:api/ByteArrayWithLength:1.0";

  public static void insert (org.omg.CORBA.Any a, api.ByteArrayWithLength that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static api.ByteArrayWithLength extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_octet);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_array_tc ((int)(4 * 1024), _tcOf_members0 );
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_alias_tc (api.byteArrayHelper.id (), "byteArray", _tcOf_members0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "bytes",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[1] = new org.omg.CORBA.StructMember (
            "byteNum",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (api.ByteArrayWithLengthHelper.id (), "ByteArrayWithLength", _members0);
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

  public static api.ByteArrayWithLength read (org.omg.CORBA.portable.InputStream istream)
  {
    api.ByteArrayWithLength value = new api.ByteArrayWithLength ();
    value.bytes = api.byteArrayHelper.read (istream);
    value.byteNum = istream.read_long ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, api.ByteArrayWithLength value)
  {
    api.byteArrayHelper.write (ostream, value.bytes);
    ostream.write_long (value.byteNum);
  }

}
