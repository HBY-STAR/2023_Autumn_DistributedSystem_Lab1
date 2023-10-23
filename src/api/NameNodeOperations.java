package api;


/**
* api/NameNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月23日星期一 中国标准时间 下午2:22:02
*/

public interface NameNodeOperations 
{

  //TODO: complete the interface design
  api.file_meta open (String file_path, char r_or_w);
  boolean close (String file_path);

  // )?????
  boolean mk_dir (String dir_path);
  boolean del_dir (String dir_path);
  boolean change_dir (String old_dir_path, String new_dir_path);
  boolean rename_dir (String old_dir_name, String new_dir_name);

  // ??"???
  boolean mk_file (String file_path);
  boolean del_file (String file_path);
  boolean change_file (String old_file_path, String new_file_path);
  boolean rename_file (String old_file_name, String new_file_name);
} // interface NameNodeOperations
