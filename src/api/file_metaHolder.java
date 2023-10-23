package api;

/**
* api/file_metaHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月23日星期一 中国标准时间 下午2:22:02
*/

public final class file_metaHolder implements org.omg.CORBA.portable.Streamable
{
  public api.file_meta value = null;

  public file_metaHolder ()
  {
  }

  public file_metaHolder (api.file_meta initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.file_metaHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.file_metaHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.file_metaHelper.type ();
  }

}
