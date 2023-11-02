package impl;

import api.DataNodePOA;
import api.NameNode;
import api.NameNodeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

@Setter
public class DataNodeImpl extends DataNodePOA {
    private static final int MAX_BLOCK_NUM = 1000;
    private static final int BLOCK_SIZE = 4096;
    private int dateNodeId;
    private static NameNode nameNode;
    private static boolean nameNode_get=false;
    private final String data_node_path = "DataNodeFile/DataNode";
    private boolean [] block_used = new boolean[MAX_BLOCK_NUM];
    private byte[] block = new byte[BLOCK_SIZE];

    @Override
    public byte[] read(int block_id) {
        String block_file_path = getBlockFilePath(block_id);
        File blockFile = new File(block_file_path);
        block = new byte[BLOCK_SIZE];
        if (blockFile.exists()) {
            try {
                block = Files.readAllBytes(Paths.get(block_file_path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return block;
        }
        return null;
    }

    @Override
    public boolean append(int block_id, byte[] bytes, String file_path) {
        String block_file_path = getBlockFilePath(block_id);
        File block_file = new File(block_file_path);
        block = new byte[BLOCK_SIZE];
        if(!block_file.exists()){
            return false;
        }
        try {
            block = Files.readAllBytes(Paths.get(block_file_path));
            int cur_len = block.length;

            FileWriter writer = new FileWriter(block_file, true);
            for(int i=0;i<bytes.length;i++){
                if(cur_len>MAX_BLOCK_NUM-1){
                    int new_block = alloc();
                    if(new_block == -1){
                        byte[] left_bytes = new byte[BLOCK_SIZE];
                        System.arraycopy(bytes, i, left_bytes, 0, bytes.length - i);
                        writer.close();
                        return nameNode.file_increase(file_path,left_bytes,dateNodeId,block_id,false);
                    }else {
                        nameNode.file_increase(file_path,null,dateNodeId,new_block,true);
                        writer = new FileWriter(getBlockFilePath(new_block),true);
                        writer.write(bytes[i]);
                        cur_len=1;
                    }
                }else {
                    writer.write(bytes[i]);
                    cur_len++;
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int check_free_size() {
        int count_free=0;
        for(int i=0;i<MAX_BLOCK_NUM;i++){
            if(!block_used[i]){
                count_free++;
            }
        }
        return count_free;
    }

    @Override
    public int alloc() {
        int find_free=-1;
        for(int i=0;i<MAX_BLOCK_NUM;i++){
            if(!block_used[i]){
                find_free=i;
                block_used[i]=true;
                break;
            }
        }
        if(find_free<0){
            return -1;
        }
        String brock_file_path = getBlockFilePath(find_free);
        File blockFile = new File(brock_file_path);
        if (blockFile.exists()) {
            blockFile.delete();
        }
        try {
            blockFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return find_free;
    }

    private String getBlockFilePath(int find_free) {
        return data_node_path+dateNodeId+"/block"+ find_free;
    }

    private String getMetaPath() {
        return data_node_path+dateNodeId+"/NodeMeta"+dateNodeId+".json";
    }

    @Override
    public boolean free(int block_id) {
        block_used[block_id]=false;
        String brock_file_path = getBlockFilePath(block_id);
        File blockFile = new File(brock_file_path);
        if (blockFile.exists()) {
            return blockFile.delete();
        }
        return false;
    }

    private static void GetNameNode(){
        try {
            if(!nameNode_get){
                Properties properties = new Properties();
                properties.put("org.omg.CORBA.ORBInitialHost","127.0.0.1");
                properties.put("org.omg.CORBA.ORBInitialPort","15000");

                ORB orb = ORB.init((String[]) null,properties);

                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                nameNode = NameNodeHelper.narrow(ncRef.resolve_str("NameNode"));
                System.out.println("NameNode is obtained");
            }
            nameNode_get=true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
