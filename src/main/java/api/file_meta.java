package api;

import lombok.Data;

@Data
public final class file_meta implements org.omg.CORBA.portable.IDLEntity
{
  public boolean is_new = false;
  public int file_size = (int)0;
  public int block_num = (int)0;
  public int block_data_node[] = null;
  public int block_id[] = null;
  public String creat_time = null;
  public String modify_time = null;
  public String access_time = null;

  public file_meta ()
  {
  } // ctor

  public file_meta (boolean _is_new, int _file_size, int _block_num, int[] _block_data_node, int[] _block_id, String _creat_time, String _modify_time, String _access_time)
  {
    is_new = _is_new;
    file_size = _file_size;
    block_num = _block_num;
    block_data_node = _block_data_node;
    block_id = _block_id;
    creat_time = _creat_time;
    modify_time = _modify_time;
    access_time = _access_time;
  } // ctor

} // class file_meta
