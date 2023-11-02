package api;


/**
* api/NameNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��2�� ������ ����03ʱ33��15�� CST
*/

public interface NameNodeOperations 
{

  // from Client
  api.FileMeta open (String file_path, int mode);
  boolean close (String file_path, int writing_cookie);

  // )?????
  boolean mk_dir (String dir_path);
  boolean del_dir (String dir_path);
  boolean change_dir (String old_dir_path, String new_dir_path);
  boolean rename_dir (String old_dir_path, String new_dir_name);

  // ??"???
  boolean mk_file (String file_path);
  boolean del_file (String file_path);
  boolean change_file (String old_file_path, String new_file_path);
  boolean rename_file (String old_file_path, String new_file_name);

  // from DataNode
  boolean file_increase (String file_path, byte[] bytes, int block_data_node, int block_id, boolean have_free);
} // interface NameNodeOperations
