package api;


/**
* api/FileMeta.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月30日 星期一 下午05时31分59秒 CST
*/

public final class FileMeta implements org.omg.CORBA.portable.IDLEntity
{
  public boolean is_new = false;
  public int writing_cookie = (int)0;
  public int file_size = (int)0;
  public int block_num = (int)0;
  public int block_data_node[] = null;
  public int block_id[] = null;
  public String creat_time = null;
  public String modify_time = null;
  public String access_time = null;

  public FileMeta ()
  {
  } // ctor

  public FileMeta (boolean _is_new, int _writing_cookie, int _file_size, int _block_num, int[] _block_data_node, int[] _block_id, String _creat_time, String _modify_time, String _access_time)
  {
    is_new = _is_new;
    writing_cookie = _writing_cookie;
    file_size = _file_size;
    block_num = _block_num;
    block_data_node = _block_data_node;
    block_id = _block_id;
    creat_time = _creat_time;
    modify_time = _modify_time;
    access_time = _access_time;
  } // ctor

} // class FileMeta
