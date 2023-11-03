package test;

import api.ByteArrayWithLength;
import api.DataNode;
import api.FileMeta;
import impl.DataNodeImpl;
import impl.NameNodeImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class DataNodeTest {
    static DataNodeImpl dn;
    private static NameNodeImpl nn;

    @Before
    public void setUp() {
        nn = new NameNodeImpl();
        dn = new DataNodeImpl();
    }

    @Test
    public void testRead() {
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b10);
        int blockId = fileInfo.block_id[0];
        assertNotNull(dn.read(blockId));
    }

    @Test
    public void testAppend() {
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b10);
        int blockId = fileInfo.block_id[0];
        byte[] toWrite = "Hello World".getBytes(StandardCharsets.UTF_8);

        byte[] block = new byte[4096];
        System.arraycopy(toWrite, 0, block, 0, toWrite.length);

        ByteArrayWithLength byteArray = new ByteArrayWithLength(block,toWrite.length);
        dn.append(blockId, byteArray,filename);
        ByteArrayWithLength readByte = dn.read(blockId);
        byte[] read = readByte.bytes;

        int n = toWrite.length;
        int N = readByte.byteNum;
        for (int i = 0; i < n; i++) {
            assertEquals("Block ID: " + blockId + ". Read block bytes and appended bytes differ at the " + i
                    + " byte to the eof.", toWrite[n - 1 - i], read[N - 1 - i]);
        }
    }
}
