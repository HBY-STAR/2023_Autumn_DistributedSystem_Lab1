package api;


/**
* api/DataNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��2�� ������ ����03ʱ33��15�� CST
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
