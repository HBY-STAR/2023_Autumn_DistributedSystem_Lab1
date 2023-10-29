package utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NameNodeMetaFileTreeTest {

    @Test
    public void AddTest() {
        NameNodeMetaFileTree tree = new NameNodeMetaFileTree("NameNodeFile/fs_meta_file.json");
        boolean load_success = tree.loadTree();
        List<String> path1=new ArrayList<>();
        path1.add("dir1");
        NameNodeMetaFileNode node1 = new NameNodeMetaFileNode(null,path1,false,new ArrayList<>());
        List<String> path2=new ArrayList<>();
        path2.add("file2");
        NameNodeMetaFileNode node2 = new NameNodeMetaFileNode(null,path2,true,new ArrayList<>());
        List<String> path3=new ArrayList<>();
        path3.add("dir1");
        path3.add("file1_1");
        NameNodeMetaFileNode node3 = new NameNodeMetaFileNode(null,path3,true,new ArrayList<>());

        boolean add_success1 = tree.addNode(node1);
        boolean add_success2 = tree.addNode(node2);
        boolean add_success3 = tree.addNode(node3);

        boolean store_success = tree.storeTree();

        assertTrue(load_success);
        assertTrue(store_success);
    }
}