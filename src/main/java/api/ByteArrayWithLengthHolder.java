package api;

/**
* api/ByteArrayWithLengthHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��11��3�� ������ ����12ʱ28��30�� CST
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
