package api;


/**
* api/NameNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年11月2日 星期四 下午03时33分15秒 CST
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
