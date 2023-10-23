package api;


/**
* api/DataNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月23日星期一 中国标准时间 下午2:22:02
*/

public interface DataNodeOperations 
{
  byte[] read (int block_id);
  void append (int block_id, byte[] bytes);
  int randomBlockId ();
} // interface DataNodeOperations
