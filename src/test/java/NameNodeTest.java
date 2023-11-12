import api.FileMeta;
import impl.NameNodeImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import static org.junit.Assert.*;
public class NameNodeTest {
    private static NameNodeImpl nn;
    private void close(FileMeta... fileInfos){
        for(FileMeta fileInfo: fileInfos){
            if(fileInfo!=null){
                nn.close(fileInfo.file_path,fileInfo.writing_cookie);
            }
        }
    }

    @Before
    public void setUp(){
        nn = new NameNodeImpl();
    }

    @Test
    /* open a non-exist file */
    public void testCreate(){
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b10);
        assertEquals(fileInfo.file_path,filename);
        close(fileInfo);
    }

    @Test
    /* open an existing file */
    public void testOpen(){
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b10);
        FileMeta fileInfo2 = nn.open(filename, 0b01);
        assertNotSame(fileInfo,fileInfo2);
        close(fileInfo, fileInfo2);
    }



    @Test
    /* open an existing and being written file in writing mode */
    public void testOpenWrite(){
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b10);
        FileMeta fileInfo2 = nn.open(filename, 0b11);
        assertEquals(fileInfo.file_path,filename);
        assertEquals(fileInfo2.file_path,"");
        close(fileInfo);
    }

    @Test
    /* open an existing and being written file in reading mode, multiple times */
    public void testOpenRead(){
        String filename = FileSystem.newFilename();
        FileMeta fileInfo = nn.open(filename, 0b01);
        FileMeta fileInfo2 = nn.open(filename, 0b10);
        nn.close(filename,fileInfo2.writing_cookie);

        FileMeta fileInfo3 = nn.open(filename, 0b01);
        FileMeta fileInfo4 = nn.open(filename, 0b01);
        FileMeta fileInfo5 = nn.open(filename, 0b10);
        FileMeta fileInfo6 = nn.open(filename, 0b01);

        assertEquals(fileInfo.file_path,"");
        assertNotEquals(fileInfo2.file_path,"");
        assertNotEquals(fileInfo3.file_path,"");
        assertNotEquals(fileInfo4.file_path,"");
        assertNotEquals(fileInfo5.file_path,"");
        assertEquals(fileInfo6.file_path,"");

        close(fileInfo2,fileInfo3,fileInfo4,fileInfo5);
    }
}
