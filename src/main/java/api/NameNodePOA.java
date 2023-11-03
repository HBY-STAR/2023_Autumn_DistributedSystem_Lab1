package api;


/**
* api/NameNodePOA.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年11月3日 星期五 下午12时28分30秒 CST
*/

public abstract class NameNodePOA extends org.omg.PortableServer.Servant
 implements api.NameNodeOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("open", new java.lang.Integer (0));
    _methods.put ("close", new java.lang.Integer (1));
    _methods.put ("mk_dir", new java.lang.Integer (2));
    _methods.put ("del_dir", new java.lang.Integer (3));
    _methods.put ("change_dir", new java.lang.Integer (4));
    _methods.put ("rename_dir", new java.lang.Integer (5));
    _methods.put ("mk_file", new java.lang.Integer (6));
    _methods.put ("del_file", new java.lang.Integer (7));
    _methods.put ("change_file", new java.lang.Integer (8));
    _methods.put ("rename_file", new java.lang.Integer (9));
    _methods.put ("file_increase", new java.lang.Integer (10));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  // from Client
       case 0:  // api/NameNode/open
       {
         String file_path = in.read_string ();
         int mode = in.read_long ();
         api.FileMeta $result = null;
         $result = this.open (file_path, mode);
         out = $rh.createReply();
         api.FileMetaHelper.write (out, $result);
         break;
       }

       case 1:  // api/NameNode/close
       {
         String file_path = in.read_string ();
         int writing_cookie = in.read_long ();
         boolean $result = false;
         $result = this.close (file_path, writing_cookie);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  // )?????
       case 2:  // api/NameNode/mk_dir
       {
         String dir_path = in.read_string ();
         boolean $result = false;
         $result = this.mk_dir (dir_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 3:  // api/NameNode/del_dir
       {
         String dir_path = in.read_string ();
         boolean $result = false;
         $result = this.del_dir (dir_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 4:  // api/NameNode/change_dir
       {
         String old_dir_path = in.read_string ();
         String new_dir_path = in.read_string ();
         boolean $result = false;
         $result = this.change_dir (old_dir_path, new_dir_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // api/NameNode/rename_dir
       {
         String old_dir_path = in.read_string ();
         String new_dir_name = in.read_string ();
         boolean $result = false;
         $result = this.rename_dir (old_dir_path, new_dir_name);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  // ??"???
       case 6:  // api/NameNode/mk_file
       {
         String file_path = in.read_string ();
         boolean $result = false;
         $result = this.mk_file (file_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 7:  // api/NameNode/del_file
       {
         String file_path = in.read_string ();
         boolean $result = false;
         $result = this.del_file (file_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 8:  // api/NameNode/change_file
       {
         String old_file_path = in.read_string ();
         String new_file_path = in.read_string ();
         boolean $result = false;
         $result = this.change_file (old_file_path, new_file_path);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 9:  // api/NameNode/rename_file
       {
         String old_file_path = in.read_string ();
         String new_file_name = in.read_string ();
         boolean $result = false;
         $result = this.rename_file (old_file_path, new_file_name);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  // from DataNode
       case 10:  // api/NameNode/file_increase
       {
         String file_path = in.read_string ();
         api.ByteArrayWithLength byteArray = api.ByteArrayWithLengthHelper.read (in);
         int block_data_node = in.read_long ();
         int block_id = in.read_long ();
         boolean have_free = in.read_boolean ();
         boolean $result = false;
         $result = this.file_increase (file_path, byteArray, block_data_node, block_id, have_free);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:api/NameNode:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public NameNode _this() 
  {
    return NameNodeHelper.narrow(
    super._this_object());
  }

  public NameNode _this(org.omg.CORBA.ORB orb) 
  {
    return NameNodeHelper.narrow(
    super._this_object(orb));
  }


} // class NameNodePOA
