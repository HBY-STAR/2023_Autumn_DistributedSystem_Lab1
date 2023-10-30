package impl;

import api.DataNodePOA;
import api.NameNode;
import api.NameNodeHelper;
import lombok.Setter;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Properties;

@Setter
public class DataNodeImpl extends DataNodePOA {
    private static final int MAX_BLOCK_NUM = 1000;
    private static final int BLOCK_SIZE = 4096;
    private int dateNodeId;
    private static NameNode nameNode;
    private static boolean nameNode_get=false;
    private final String data_node_meta_path = "DataNodeFile/DataNode"+dateNodeId+"/NodeMeta"+dateNodeId+".json";
    private final String data_node_block_dir_path = "DataNodeFile/DataNode"+dateNodeId;
    private boolean [] block_used = new boolean[MAX_BLOCK_NUM];
    private byte[] block = new byte[4096];
    @Override
    public byte[] read(int block_id) {
        return new byte[0];
    }

    @Override
    public void append(int block_id, byte[] bytes) {
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
        return 0;
    }

    @Override
    public boolean free(int block_id) {
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
