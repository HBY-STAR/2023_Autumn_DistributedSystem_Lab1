package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NameNodeMetaFileTree {
    public static NameNodeMetaFileNode root = null;
    private String meta_file_path = null;

    public NameNodeMetaFileTree(String meta_file_path){
        this.meta_file_path = meta_file_path;
    }

    public boolean loadTree() {
        try {
            // 创建ObjectMapper对象
            ObjectMapper mapper = new ObjectMapper();
            // 从JSON文件读取对象
            File check = new File(meta_file_path);
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
            root = mapper.readValue(new File(meta_file_path), NameNodeMetaFileNode.class);
            return true;
        } catch (IOException e) {
            System.out.println("Can recognize this file");
            return false;
        }
    }

    public boolean storeTree() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(meta_file_path), root);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNode(NameNodeMetaFileNode node) {
        if (root == null) {
            List<String> root_path = new ArrayList<>();
            root_path.add("~");
            root = new NameNodeMetaFileNode(null, root_path, false, new ArrayList<>());
        }
        NameNodeMetaFileNode findDir;
        if (node.path.size() == 1) {
            findDir = root;
        } else {
            findDir = findNode(root, node.path.subList(0, node.path.size() - 1));
        }
        if (findDir == null) {
            System.out.println("No such file or directory");
            return false;
        } else if (findDir.is_file) {
            System.out.println("No a directory");
            return false;
        } else {
            boolean already_exist=false;
            for(NameNodeMetaFileNode child : findDir.children){
                if(child.getPath().equals(node.getPath())){
                    already_exist=true;
                    break;
                }
            }
            if(already_exist){
                System.out.println("File or Directory already exists");
                return false;
            }else {
                findDir.children.add(node);
                return true;
            }

        }
    }

    public boolean deleteNode(List<String> path) {
        if (path.size() == 0) {
            System.out.println("Empty path");
            return false;
        }
        NameNodeMetaFileNode findNode = findNode(root, path);
        NameNodeMetaFileNode findDir;
        if (path.size() == 1) {
            findDir = root;
        } else {
            findDir = findNode(root,path.subList(0,path.size() - 1));
        }
        if (findDir == null || findNode == null) {
            System.out.println("No such file or directory");
            return false;
        } else {
            findDir.children.remove(findNode);
            return true;
        }
    }

    public NameNodeMetaFileNode findNode(NameNodeMetaFileNode start_node, List<String> path) {
        if (path.size() == 0) {
            return start_node;
        }
        String cur_path = path.get(0);
        boolean cur_exist = false;
        NameNodeMetaFileNode cur_exist_node = new NameNodeMetaFileNode();
        for (NameNodeMetaFileNode child : start_node.children) {
            if (child.path.size() == 0) {
                continue;
            }
            if (cur_path.equals(child.path.get(child.path.size() - 1))) {
                cur_exist = true;
                cur_exist_node = child;
                break;
            }
        }
        if (cur_exist) {
            if(path.size()<2){
                return cur_exist_node;
            }
            return findNode(cur_exist_node, path.subList(1,path.size() - 1));
        } else {
            return null;
        }
    }

    public void test_and_set_tree(){
        if(root==null){
            loadTree();
        }
    }
}
