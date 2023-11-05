package impl;

import api.ByteArrayWithLength;
import api.DataNodePOA;
import api.NameNode;
import api.NameNodeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import utils.NameNodeMetaFileNode;

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
    public static final int MAX_BLOCK_NUM = 1000;
    public static final int BLOCK_SIZE = 4096;
    private int dateNodeId;
    private static NameNode nameNode;
    private static boolean nameNode_get=false;
    private final String data_node_path = "DataNodeFile/DataNode";
    private boolean [] block_used = new boolean[MAX_BLOCK_NUM];
    private byte[] block = new byte[BLOCK_SIZE];

    @Override
    public ByteArrayWithLength read(int block_id) {
        String block_file_path = getBlockFilePath(block_id);
        File blockFile = new File(block_file_path);
        block = new byte[BLOCK_SIZE];
        ByteArrayWithLength res;
        if (blockFile.exists()) {
            try {
                byte[] valid = Files.readAllBytes(Paths.get(block_file_path));
                System.arraycopy(valid,0,block,0,valid.length);
                res = new ByteArrayWithLength(block,valid.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return res;
        }
        return new ByteArrayWithLength(new byte[4096],0);
    }

    @Override
    public boolean append(int block_id, ByteArrayWithLength byteArray, String file_path) {
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
            for(int i=0;i<byteArray.byteNum;i++){
                if(cur_len>BLOCK_SIZE-1){
                    int new_block = alloc();
                    if(new_block == -1){
                        byte[] left_bytes = new byte[BLOCK_SIZE];
                        System.arraycopy(byteArray.bytes, i, left_bytes, 0, byteArray.byteNum - i);
                        writer.close();
                        ByteArrayWithLength leftArray = new ByteArrayWithLength(left_bytes,byteArray.byteNum - i);
                        return nameNode.file_increase(file_path,leftArray,dateNodeId,block_id,false);
                    }else {
                        byte[] fill_block = new byte[BLOCK_SIZE];
                        ByteArrayWithLength fill = new ByteArrayWithLength(fill_block,0);
                        nameNode.file_increase(file_path,fill,dateNodeId,new_block,true);
                        writer.write(byteArray.bytes[i]);
                        writer = new FileWriter(getBlockFilePath(new_block),true);
                        cur_len=1;
                    }
                }else {
                    writer.write(byteArray.bytes[i]);
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
        loadMeta();
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
        saveMeta();
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
        loadMeta();
        block_used[block_id]=false;
        String brock_file_path = getBlockFilePath(block_id);
        File blockFile = new File(brock_file_path);
        if (blockFile.exists()) {
            saveMeta();
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
    private boolean loadMeta(){
        try {
            // 创建ObjectMapper对象
            ObjectMapper mapper = new ObjectMapper();
            // 从JSON文件读取对象
            File check = new File(getMetaPath());
            if(!check.exists()){
                if(check.createNewFile()){
                    System.out.println("Create New File");
                    return true;
                }
                else {
                    System.out.println("Create new file failed");
                    return false;
                }

            }
            block_used = mapper.readValue(new File(getMetaPath()),boolean[].class);
            return true;
        } catch (IOException e) {
            System.out.println("Can't recognize this file");
            return false;
        }
    }
    private boolean saveMeta(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(getMetaPath()), block_used);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
