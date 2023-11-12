package impl;
import api.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import utils.NameNodeMetaFileNode;
import utils.NameNodeMetaFileTree;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NameNodeImpl extends NameNodePOA {                             
    private static final String fs_meta_path = "NameNodeFile/fs_meta_file.json";
    private static final NameNodeMetaFileTree tree = new NameNodeMetaFileTree(fs_meta_path);
    public static final int MAX_DATA_NODE = 3;
    private static final DataNode[] dataNodes = new DataNode[MAX_DATA_NODE];
    private static boolean dataNodes_get = false;
    @Override
    public FileMeta open(String filepath, int mode) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(filepath);
        NameNodeMetaFileNode findDir = getNameNodeMetaFileNodeDir(filepath);
        //no such file
        if(findNode==null){
            //no such directory
            if(findDir==null){
                return new FileMeta();
            }
            //create new file
            else {
                //with write mode
                if((mode & 0b10)!=0){
                    Random rand = new Random();
                    int writing_cookie = rand.nextInt(10000000) +10000000;
                    int [] block = alloc();
                    int [] block_data_node = new int[1000];
                    int [] block_id = new int[1000];
                    block_data_node[0] = block[0];
                    block_id[0] = block[1];
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    FileMeta new_file = new FileMeta(filepath,true,writing_cookie,0,1,block_data_node,block_id,
                            LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt));
                    NameNodeMetaFileNode new_node = new NameNodeMetaFileNode(new_file,string_to_list(filepath),true,new ArrayList<>());
                    tree.addNode(new_node);
                    tree.storeTree();
                    return new_file;
                }
                //without write mode
                else {
                    return new FileMeta();
                }
            }
        }
        //file exists
        else {
            //file
            if(findNode.is_file){
                //is_writing
                if(findNode.data.writing_cookie!=0){
                    return new FileMeta();
                }
                //isn't_writing
                else {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    findNode.data.access_time=LocalDateTime.now().format(fmt);

                    //with write mode
                    if((mode & 0b10)!=0){
                        Random rand = new Random();
                        findNode.data.writing_cookie = rand.nextInt(10000000) +10000000;
                        findNode.data.modify_time=LocalDateTime.now().format(fmt);
                    }
                    return findNode.data;
                }
            }
            //dir
            else {
                return new FileMeta();
            }

        }
    }

    @Override
    public boolean close(String fileInfo,int writing_cookie) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(fileInfo);
        //file exists
        if(findNode==null){
            return false;
        }
        //no such file
        else {
            //file
            if(findNode.is_file){
                //is_writing
                if(findNode.data.writing_cookie!=0){
                    //with_right_cookie
                    if(writing_cookie==findNode.data.writing_cookie){
                        findNode.data.writing_cookie=0;
                        tree.storeTree();
                        return true;
                    }
                    //without_right_cookie
                    else {
                        return false;
                    }
                }
                //isn't_writing
                else {
                    tree.storeTree();
                    return true;
                }
            }
            //dir
            else{
                return false;
            }
        }
    }

    @Override
    public boolean mk_dir(String dir_path) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNodeDir(dir_path);
        //no such directory
        if(findNode==null){
            return false;
        }
        //directory exists
        else {
            NameNodeMetaFileNode new_node = new NameNodeMetaFileNode(null,string_to_list(dir_path),
                    false,new ArrayList<>());
            return tree.addNode(new_node);
        }
    }

    @Override
    public boolean del_dir(String dir_path) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(dir_path);
        //no such directory
        if(findNode==null){
            return false;
        }
        //directory exists
        else {
            free(findNode);
            return tree.deleteNode(string_to_list(dir_path));
        }
    }

    @Override
    public boolean change_dir(String old_dir_path, String new_dir_path) {
        NameNodeMetaFileNode oldNode = getNameNodeMetaFileNode(old_dir_path);
        NameNodeMetaFileNode newDir =  getNameNodeMetaFileNode(new_dir_path);
        if(oldNode!=null&&newDir!=null){
            int depth = newDir.path.size()-1;
            for(int i=0;i<depth;i++){
                tree.renameNode(oldNode,newDir.path.get(i),i);
            }
            tree.addNode(oldNode);
            tree.deleteNode(string_to_list(old_dir_path));
            return true;
        }
        return false;
    }

    @Override
    public boolean rename_dir(String old_dir_path, String new_dir_name) {
        NameNodeMetaFileNode oldNode = getNameNodeMetaFileNode(old_dir_path);
        if(oldNode!=null){
            int depth = oldNode.path.size()-1;
            tree.renameNode(oldNode,new_dir_name,depth);
            return true;
        }
        return false;
    }

    @Override
    public boolean mk_file(String file_path) {
        NameNodeMetaFileNode findDir = getNameNodeMetaFileNodeDir(file_path);
        //no such directory
        if(findDir==null){
            return false;
        }
        //directory exists
        else {
            int [] block = alloc();
            int [] block_data_node = new int[1000];
            int [] block_id = new int[1000];
            block_data_node[0] = block[0];
            block_id[0] = block[1];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            FileMeta new_file = new FileMeta(file_path,true,0,0,1,block_data_node,block_id,
                    LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt));
            NameNodeMetaFileNode new_node = new NameNodeMetaFileNode(new_file,string_to_list(file_path),true,new ArrayList<>());
            tree.addNode(new_node);
            return true;
        }
    }

    @Override
    public boolean del_file(String file_path) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(file_path);
        //file exists
        if(findNode!=null){
            free(findNode);
            return tree.deleteNode(string_to_list(file_path));
        }
        //no such file
        else {
            return false;
        }
    }

    @Override
    public boolean change_file(String old_file_path, String new_file_path) {
        NameNodeMetaFileNode oldNode = getNameNodeMetaFileNode(old_file_path);
        if(oldNode!=null){
            NameNodeMetaFileNode findNewDir = getNameNodeMetaFileNodeDir(new_file_path);
            if(findNewDir!=null){
                NameNodeMetaFileNode findOldDir = getNameNodeMetaFileNodeDir(old_file_path);
                findOldDir.children.remove(oldNode);
                oldNode.path = string_to_list(new_file_path);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean rename_file(String old_file_path, String new_file_name) {
        NameNodeMetaFileNode oldNode = getNameNodeMetaFileNode(old_file_path);
        if(oldNode!=null){
            oldNode.path.set(oldNode.path.size()-1,new_file_name);
            return true;
        }
        return false;
    }

    @Override
    public boolean file_increase(String file_path,ByteArrayWithLength byteArray,  int block_data_node ,int block_id, boolean have_free) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(file_path);
        if(findNode!=null){
            if(have_free){
                findNode.data.block_data_node[findNode.data.block_num] = block_data_node;
                findNode.data.block_id[findNode.data.block_num] = block_id;
            }
            else {
                int[] new_block = alloc();
                findNode.data.block_data_node[findNode.data.block_num] = new_block[0];
                findNode.data.block_id[findNode.data.block_num] = new_block[1];
                dataNodes[new_block[0]].append(new_block[1],byteArray,file_path);
            }
            findNode.data.block_num++;
            return true;
        }
        return false;
    }

    //寻找空闲最多的dataNode，分配空间
    private int [] alloc(){
        GetDataNode();
        int [] new_block = new int[]{0,0};
        int max_free_node=0;
        int max_free_size=0;
        for(int i=0;i<MAX_DATA_NODE;i++){
            int free_size = dataNodes[i].check_free_size();
            if(free_size>max_free_size){
                max_free_size=free_size;
                max_free_node=i;
            }
        }
        new_block[0] = max_free_node;
        new_block[1] = dataNodes[max_free_node].alloc();
        return new_block;
    }

    //释放当前节点及其子节点下的dataNode空间
    private void free(NameNodeMetaFileNode start_node){
        GetDataNode();
        if(start_node!=null){
            if(start_node.is_file){
                for(int i=0;i<start_node.data.block_num;i++){
                    dataNodes[start_node.data.block_data_node[i]].free(start_node.data.block_id[i]);
                }
            }else {
                for (NameNodeMetaFileNode child : start_node.children){
                    free(child);
                }
            }
        }
    }

    private NameNodeMetaFileNode getNameNodeMetaFileNode(String filepath) {
        tree.test_and_set_tree();
        List<String> list_file_path = string_to_list(filepath);
        return tree.findNode(NameNodeMetaFileTree.root,list_file_path);
    }

    private static List<String> string_to_list(String filepath) {
        Scanner scanner = new Scanner(filepath);
        scanner.useDelimiter("/");
        List<String> list_file_path = new ArrayList<>();
        while (scanner.hasNext()){
            list_file_path.add(scanner.next());
        }
        return list_file_path;
    }

    private NameNodeMetaFileNode getNameNodeMetaFileNodeDir(String filepath) {
        tree.test_and_set_tree();
        List<String> list_file_path = string_to_list(filepath);
        list_file_path.remove(list_file_path.size()-1);
        return tree.findNode(NameNodeMetaFileTree.root,list_file_path);
    }

    private static void GetDataNode(){
        try {
            if(!dataNodes_get){
                Properties properties = new Properties();
                properties.put("org.omg.CORBA.ORBInitialHost","127.0.0.1");
                properties.put("org.omg.CORBA.ORBInitialPort","15000");

                ORB orb = ORB.init((String[]) null,properties);

                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                for(int dataNodeId = 0;dataNodeId<MAX_DATA_NODE;dataNodeId++){
                    dataNodes[dataNodeId] = DataNodeHelper.narrow(ncRef.resolve_str("DataNode"+dataNodeId));
                    System.out.println("DataNode"+dataNodeId+" is obtained");
                }
            }
            dataNodes_get=true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
