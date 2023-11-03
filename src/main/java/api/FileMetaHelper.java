package api;

abstract public class FileMetaHelper
{
  private static String  _id = "IDL:api/FileMeta:1.0";

  public static void insert (org.omg.CORBA.Any a, api.FileMeta that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static api.FileMeta extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [10];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "file_path",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_boolean);
          _members0[1] = new org.omg.CORBA.StructMember (
            "is_new",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[2] = new org.omg.CORBA.StructMember (
            "writing_cookie",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[3] = new org.omg.CORBA.StructMember (
            "file_size",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[4] = new org.omg.CORBA.StructMember (
            "block_num",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_array_tc (1000, _tcOf_members0 );
          _members0[5] = new org.omg.CORBA.StructMember (
            "block_data_node",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_array_tc (1000, _tcOf_members0 );
          _members0[6] = new org.omg.CORBA.StructMember (
            "block_id",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[7] = new org.omg.CORBA.StructMember (
            "creat_time",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[8] = new org.omg.CORBA.StructMember (
            "modify_time",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[9] = new org.omg.CORBA.StructMember (
            "access_time",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (api.FileMetaHelper.id (), "FileMeta", _members0);
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

  public static api.FileMeta read (org.omg.CORBA.portable.InputStream istream)
  {
    api.FileMeta value = new api.FileMeta ();
    value.file_path = istream.read_string ();
    value.is_new = istream.read_boolean ();
    value.writing_cookie = istream.read_long ();
    value.file_size = istream.read_long ();
    value.block_num = istream.read_long ();
    value.block_data_node = new int[1000];
    for (int _o0 = 0;_o0 < (1000); ++_o0)
    {
      value.block_data_node[_o0] = istream.read_long ();
    }
    value.block_id = new int[1000];
    for (int _o1 = 0;_o1 < (1000); ++_o1)
    {
      value.block_id[_o1] = istream.read_long ();
    }
    value.creat_time = istream.read_string ();
    value.modify_time = istream.read_string ();
    value.access_time = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, api.FileMeta value)
  {
    ostream.write_string (value.file_path);
    ostream.write_boolean (value.is_new);
    ostream.write_long (value.writing_cookie);
    ostream.write_long (value.file_size);
    ostream.write_long (value.block_num);
    if (value.block_data_node.length != (1000))
      throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    for (int _i0 = 0;_i0 < (1000); ++_i0)
    {
      ostream.write_long (value.block_data_node[_i0]);
    }
    if (value.block_id.length != (1000))
      throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    for (int _i1 = 0;_i1 < (1000); ++_i1)
    {
      ostream.write_long (value.block_id[_i1]);
    }
    ostream.write_string (value.creat_time);
    ostream.write_string (value.modify_time);
    ostream.write_string (value.access_time);
  }

}
