package api;

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
