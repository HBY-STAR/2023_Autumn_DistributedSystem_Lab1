package api;


/**
* api/DataNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��3�� ������ ����12ʱ28��30�� CST
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
