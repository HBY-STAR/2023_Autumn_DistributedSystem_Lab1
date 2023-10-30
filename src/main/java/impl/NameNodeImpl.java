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
    private static final String fs_meta_path = "NameNodeFile/NameNodeMeta";
    private final NameNodeMetaFileTree tree = new NameNodeMetaFileTree(fs_meta_path);
    public static final int MAX_DATA_NODE = 5;
    private static DataNode[] dataNodes = new DataNode[MAX_DATA_NODE];
    @Override
    public FileMeta open(String filepath, int mode) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(filepath);
        NameNodeMetaFileNode findDir = getNameNodeMetaFileNodeDir(filepath);
        //no such file
        if(findNode==null){
            //no such directory
            if(findDir==null){
                return null;
            }
            //create new file
            else {
                //with write mode
                if((mode & 0b10)!=0){
                    Random rand = new Random();
                    int writing_cookie = rand.nextInt(10000000) +10000000;
                    int [] block = alloc();
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    FileMeta new_file = new FileMeta(true,writing_cookie,0,1,new int[]{block[0]},new int[]{block[1]},
                            LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt),LocalDateTime.now().format(fmt));
                    NameNodeMetaFileNode new_node = new NameNodeMetaFileNode(new_file,string_to_list(filepath),true,new ArrayList<>());
                    tree.addNode(new_node);
                    return new_file;
                }
                //without write mode
                else {
                    return null;
                }
            }
        }
        //file exists
        else {
            //file
            if(findNode.is_file){
                //is_writing
                if(findNode.data.writing_cookie!=0){
                    return null;
                }
                //isn't_writing
                else {
                    //with write mode
                    if((mode & 0b10)!=0){
                        Random rand = new Random();
                        findNode.data.writing_cookie = rand.nextInt(10000000) +10000000;
                    }
                    return findNode.data;
                }
            }
            //dir
            else {
                return null;
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
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNodeDir(dir_path);
        //no such directory
        if(findNode==null){
            return false;
        }
        //directory exists
        else {
            free(dir_path);
            return tree.deleteNode(string_to_list(dir_path));
        }
    }

    //TODO
    @Override
    public boolean change_dir(String old_dir_path, String new_dir_path) {
        return false;
    }

    @Override
    public boolean rename_dir(String old_dir_path, String new_dir_name) {
        NameNodeMetaFileNode oldNode = getNameNodeMetaFileNode(old_dir_path);
        if(oldNode!=null){
            oldNode.path.set(oldNode.path.size()-1,new_dir_name);
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
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            FileMeta new_file = new FileMeta(true,0,0,1,new int[]{block[0]},new int[]{block[1]},
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
            free(file_path);
            return tree.deleteNode(string_to_list(file_path));
        }
        //no such file
        else {
            return false;
        }
    }

    //TODO
    @Override
    public boolean change_file(String old_file_path, String new_file_path) {
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
    public boolean file_increase(String file_path, int block_data_node ,int block_id, int free_size) {
        NameNodeMetaFileNode findNode = getNameNodeMetaFileNode(file_path);
        if(findNode!=null){
            if(free_size>0){
                findNode.data.block_data_node[findNode.data.block_num] = block_data_node;
                findNode.data.block_id[findNode.data.block_num] = block_id;
            }
            else {
                int[] new_block = alloc();
                findNode.data.block_data_node[findNode.data.block_num] = new_block[0];
                findNode.data.block_id[findNode.data.block_num] = new_block[1];
            }
            findNode.data.block_num++;
            return true;
        }
        return false;
    }

    //TODO
    //寻找空闲最多的dataNode，分配空间
    private int [] alloc(){
        int [] new_block;

        return new int[]{0, 0};
    }

    //TODO
    //释放当前节点及其子节点下的dataNode空间
    private void free(String dir_path){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}