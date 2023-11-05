package api;

/**
* api/ByteArrayWithLengthHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年11月3日 星期五 下午12时28分30秒 CST
*/

public final class ByteArrayWithLengthHolder implements org.omg.CORBA.portable.Streamable
{
  public api.ByteArrayWithLength value = null;

  public ByteArrayWithLengthHolder ()
  {
  }

  public ByteArrayWithLengthHolder (api.ByteArrayWithLength initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.ByteArrayWithLengthHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.ByteArrayWithLengthHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.ByteArrayWithLengthHelper.type ();
  }

}
