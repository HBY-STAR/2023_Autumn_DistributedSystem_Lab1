package api;


/**
* api/DataNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月30日 星期一 下午05时31分59秒 CST
*/

public interface DataNodeOperations 
{

  // from Client
  byte[] read (int block_id);
  void append (int block_id, byte[] bytes);

  // from NameNode
  int check_free_size ();
  int alloc ();
  boolean free (int block_id);
} // interface DataNodeOperations
