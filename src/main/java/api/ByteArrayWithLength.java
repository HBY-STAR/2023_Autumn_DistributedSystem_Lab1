package api;



public final class ByteArrayWithLength implements org.omg.CORBA.portable.IDLEntity
{
  public byte bytes[] = null;
  public int byteNum = (int)0;

  public ByteArrayWithLength ()
  {
  } // ctor

  public ByteArrayWithLength (byte[] _bytes, int _byteNum)
  {
    bytes = _bytes;
    byteNum = _byteNum;
  } // ctor

} // class ByteArrayWithLength
