package api;


/**
* api/DataNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年11月3日 星期五 下午12时28分30秒 CST
*/

public interface DataNodeOperations 
{

  // from Client
  api.ByteArrayWithLength read (int block_id);
  boolean append (int block_id, api.ByteArrayWithLength byteArray, String file_path);

  // from NameNode
  int check_free_size ();
  int alloc ();
  boolean free (int block_id);
} // interface DataNodeOperations
