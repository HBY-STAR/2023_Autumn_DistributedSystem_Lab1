package api;

/**
* api/FileMetaHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月30日 星期一 下午05时31分59秒 CST
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
