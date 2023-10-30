package api;


/**
* api/DataNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��10��30�� ����һ ����05ʱ31��59�� CST
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
