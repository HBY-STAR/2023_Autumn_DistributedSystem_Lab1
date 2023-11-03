package api;

/**
* api/FileMetaHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��3�� ������ ����12ʱ28��30�� CST
*/

public final class FileMetaHolder implements org.omg.CORBA.portable.Streamable
{
  public api.FileMeta value = null;

  public FileMetaHolder ()
  {
  }

  public FileMetaHolder (api.FileMeta initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.FileMetaHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.FileMetaHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.FileMetaHelper.type ();
  }

}
