package api;


public final class FileMeta implements org.omg.CORBA.portable.IDLEntity
{
  public String file_path = "";
  public boolean is_new = false;
  public int writing_cookie = (int)0;
  public int file_size = (int)0;
  public int block_num = (int)0;
  public int block_data_node[] = new int[1000];
  public int block_id[] = new int[1000];
  public String creat_time = "";
  public String modify_time = "";
  public String access_time = "";

  public FileMeta ()
  {
  } // ctor

  public FileMeta (String _file_path, boolean _is_new, int _writing_cookie, int _file_size, int _block_num, int[] _block_data_node, int[] _block_id, String _creat_time, String _modify_time, String _access_time)
  {
    file_path = _file_path;
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
