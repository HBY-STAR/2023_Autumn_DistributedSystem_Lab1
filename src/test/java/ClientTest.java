import api.Client;
import impl.ClientImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;

public class ClientTest {
    static Client client;
    @Before
    public void setUp(){
        client = new ClientImpl();
    }

    @Test
    public void testWriteRead(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b11);
        client.append(fd,"hello".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"hello".getBytes(StandardCharsets.UTF_8));
        client.append(fd," world".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"hello world".getBytes(StandardCharsets.UTF_8));
        client.close(fd);
    }

    @Test
    public void testWriteFail(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b01);
        client.append(fd,"Lala-land".getBytes(StandardCharsets.UTF_8));
        assertNull(client.read(fd));
        client.close(fd);
    }

    @Test
    public void testReadFail(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b10);
        assertNull(client.read(fd));
        client.close(fd);
    }

    //写两个block的大小
    //分别设置DataNodeImpl中的MAX_BLOCK_NUM为1和100
    //为100时在node0中生成了两个block
    //为1时分别在node0和node1中生成了两个block
    //两种情况下测试均能成功。
    @Test
    public void testWriteTwoBlock(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b11);
        byte[] block0 = new byte[4096];
        block0[0]='h';
        block0[4095]='h';
        byte[] block1 = new byte[4096];
        block1[0]='w';
        block1[4095]='w';
        client.append(fd,block0);
        client.append(fd,block1);
        byte[] read = client.read(fd);
        assertNotNull(read);
        assertEquals(4096*2, read.length);
        assertEquals('h', read[0]);
        assertEquals('h', read[4095]);
        assertEquals('w', read[4096]);
        assertEquals('w', read[4096*2-1]);
        client.close(fd);
    }
}
