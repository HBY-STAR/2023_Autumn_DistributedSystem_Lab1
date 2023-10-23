package api;

/**
* api/DataNodeHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年10月23日星期一 中国标准时间 下午2:22:02
*/

public final class DataNodeHolder implements org.omg.CORBA.portable.Streamable
{
  public api.DataNode value = null;

  public DataNodeHolder ()
  {
  }

  public DataNodeHolder (api.DataNode initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.DataNodeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.DataNodeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.DataNodeHelper.type ();
  }

}
