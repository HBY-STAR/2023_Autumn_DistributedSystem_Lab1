package api;


/**
* api/DataNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年11月2日 星期四 下午03时33分15秒 CST
*/

public interface DataNodeOperations 
{

  // from Client
  byte[] read (int block_id);
  boolean append (int block_id, byte[] bytes, String file_path);

  // from NameNode
  int check_free_size ();
  int alloc ();
  boolean free (int block_id);
} // interface DataNodeOperations
