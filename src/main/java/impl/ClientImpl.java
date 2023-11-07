package impl;
//TODO: your implementation
import api.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static impl.DataNodeImpl.BLOCK_SIZE;
import static impl.DataNodeImpl.MAX_BLOCK_NUM;
import static impl.NameNodeImpl.MAX_DATA_NODE;

public class ClientImpl implements Client{
    private static final int MAXFDNUM = 64;
    private static NameNode nameNode;
    private static boolean nameNode_get=false;
    private static final DataNode[] dataNodes = new DataNode[MAX_DATA_NODE];
    private static boolean dataNodes_get = false;

    private static FileMeta[] cur_open_file_meta = new FileMeta[MAXFDNUM];
    private static boolean[] cur_fd_used = new boolean[MAXFDNUM];
    private static int [] cur_fd_privilege = new int[MAXFDNUM];

    private static final int MAXFILESIZE = MAX_DATA_NODE*MAX_BLOCK_NUM*BLOCK_SIZE;

    @Override
    public int open(String filepath, int mode) {
        GetNameNodeAndDataNode();
        FileMeta fileMeta = nameNode.open(filepath,mode);
        if(Objects.equals(fileMeta.file_path, "")){
            return -1;
        }

        int fd = findOpenedFile(fileMeta);

        if(fd < 0){
            fd = findFreeFd();
            cur_open_file_meta[fd]=fileMeta;
            cur_fd_used[fd]=true;
            cur_fd_privilege[fd] = mode;
        }
        return fd;
    }

    @Override
    public void append(int fd, byte[] bytes) {
        if(fd<0 || fd>=MAXFDNUM || !cur_fd_used[fd] || (cur_fd_privilege[fd]&0b10)==0){
            System.out.println("INFO: append failed");
            return;
        }
        GetNameNodeAndDataNode();
        FileMeta fileMeta = cur_open_file_meta[fd];
        if(fileMeta.writing_cookie==0){
            System.out.println("INFO: append not allowed");
            return;
        }
        if(!Objects.equals(fileMeta.file_path, "")) {
            byte [] block_bytes = new byte[4096];
            System.arraycopy(bytes, 0, block_bytes, 0, bytes.length);
            ByteArrayWithLength byteArray = new ByteArrayWithLength(block_bytes,bytes.length);
            dataNodes[fileMeta.block_data_node[fileMeta.block_num-1]].append(fileMeta.block_id[fileMeta.block_num-1],byteArray,fileMeta.file_path);
            System.out.println("INFO: write done");
        }
    }
    @Override
    public byte[] read(int fd) {
        if(fd<0 || fd>=MAXFDNUM || !cur_fd_used[fd]){
            return null;
        }
        if((cur_fd_privilege[fd]&0b01)==0){
            System.out.println("INFO: read not allowed");
            return null;
        }
        GetNameNodeAndDataNode();
        FileMeta fileMeta = cur_open_file_meta[fd];
        if(fileMeta.file_path.equals("")){
            return null;
        }
        String string = "";
        for(int i=0;i<fileMeta.block_num;i++){
           ByteArrayWithLength byteArray =  dataNodes[fileMeta.block_data_node[i]].read(fileMeta.block_id[i]);
           string = new String(byteArray.bytes,0, byteArray.byteNum,StandardCharsets.UTF_8);
        }
        return string.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close(int fd) {
        GetNameNodeAndDataNode();
        if(fd<0 || fd>=MAXFDNUM || !cur_fd_used[fd]){
            System.out.println("INFO: close failed");
            return;
        }
        FileMeta fileMeta = cur_open_file_meta[fd];
        if(!fileMeta.file_path.equals("")){
            nameNode.close(fileMeta.file_path,fileMeta.writing_cookie);
        }
        cur_open_file_meta[fd] = new FileMeta();
        cur_fd_used[fd] = false;
        System.out.println("INFO: fd "+fd+" closed");
    }
    private static void GetNameNodeAndDataNode() {
        try{
            if(!nameNode_get||!dataNodes_get){
                Properties properties = new Properties();
                properties.put("org.omg.CORBA.ORBInitialHost","127.0.0.1");
                properties.put("org.omg.CORBA.ORBInitialPort","15000");

                ORB orb = ORB.init((String[]) null,properties);

                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                if(!nameNode_get){
                    nameNode = NameNodeHelper.narrow(ncRef.resolve_str("NameNode"));
                    //System.out.println("NameNode is obtained");
                }
                nameNode_get=true;

                if(!dataNodes_get){
                    for(int dataNodeId = 0;dataNodeId<MAX_DATA_NODE;dataNodeId++){
                        dataNodes[dataNodeId] = DataNodeHelper.narrow(ncRef.resolve_str("DataNode"+dataNodeId));
                        //System.out.println("DataNode"+dataNodeId+" is obtained");
                    }
                }
                dataNodes_get=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int findFreeFd(){
        for(int i=0;i<MAXFDNUM;i++){
            if(!cur_fd_used[i]){
                return i;
            }
        }
        return -1;
    }

    private int findOpenedFile(FileMeta find){
        for(int i=0;i<MAXFDNUM;i++){
            if(find == cur_open_file_meta[i]){
                return i;
            }
        }
        return -1;
    }
}
