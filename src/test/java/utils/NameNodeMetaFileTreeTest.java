package utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameNodeMetaFileTreeTest {
    NameNodeMetaFileTree tree = new NameNodeMetaFileTree();
    @Before
    public void set(){
        tree = new NameNodeMetaFileTree("NameNodeFile/fs_meta_file.json");
        NameNodeMetaFileTree.root =null;
        try {
            Path path = Paths.get("NameNodeFile/fs_meta_file.json");
            if(Files.exists(path))
                Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tree.loadTree();
    }

    @Test
    public void AddTest() {
        buildTree(tree);
        boolean store_success = tree.storeTree();

        String check = "{" +
                "\"data\":null,\"path\":[\"~\"],\"is_file\":false," +
                "\"children\":" +
                    "[" +
                    "{\"data\":null,\"path\":[\"dir1\"],\"is_file\":false," +
                    "\"children\":" +
                        "[" +
                        "{\"data\":null,\"path\":[\"dir1\",\"file11\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}," +
                        "{\"data\":null,\"path\":[\"dir1\",\"file12\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}," +
                        "{\"data\":null,\"path\":[\"dir1\",\"dir13\"],\"is_file\":false," +
                        "\"children\":" +
                            "[" +
                            "{\"data\":null,\"path\":[\"dir1\",\"dir13\",\"file131\"],\"is_file\":true," +
                            "\"children\":[],\"_file\":true}" +
                            "],\"_file\":false}" +
                        "],\"_file\":false}," +
                    "{\"data\":null,\"path\":[\"file2\"],\"is_file\":true," +
                    "\"children\":[],\"_file\":true}," +
                    "{\"data\":null,\"path\":[\"dir3\"],\"is_file\":false," +
                    "\"children\":" +
                        "[" +
                        "{\"data\":null,\"path\":[\"dir3\",\"file31\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}" +
                        "],\"_file\":false}" +
                    "],\"_file\":false" +
                "}";

        String real;
        try {
            Path path1 = Paths.get("NameNodeFile/fs_meta_file.json");
            real = new String(Files.readAllBytes(path1));
            Files.delete(path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(check,real);
        assertTrue(store_success);
    }

    @Test
    public void DeleteTest(){
        buildTree(tree);
        List<String> path;

        path =  new ArrayList<>();
        path.add("dir3");
        path.add("file31");
        tree.deleteNode(path);

        path =  new ArrayList<>();
        path.add("dir1");
        tree.deleteNode(path);

        boolean store_success = tree.storeTree();

        String check = "{" +
                "\"data\":null,\"path\":[\"~\"],\"is_file\":false," +
                "\"children\":" +
                    "[" +
                    "{\"data\":null,\"path\":[\"file2\"],\"is_file\":true," +
                    "\"children\":[],\"_file\":true}," +
                    "{\"data\":null,\"path\":[\"dir3\"],\"is_file\":false," +
                        "\"children\":" +
                        "[" +
                        "],\"_file\":false}" +
                    "],\"_file\":false" +
                "}";


        String real;
        try {
            Path path1 = Paths.get("NameNodeFile/fs_meta_file.json");
            real = new String(Files.readAllBytes(path1));
            Files.delete(path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(check,real);
        assertTrue(store_success);
    }

    @Test
    public void RenameTest(){
        NameNodeMetaFileNode node = new NameNodeMetaFileNode();
        buildTree(tree);
        List<String> path;

        path = new ArrayList<>();
        path.add("dir1");
        node = tree.findNode(NameNodeMetaFileTree.root,path);
        tree.renameNode(node,"new_dir1",path.size()-1);

        path = new ArrayList<>();
        path.add("dir3");
        path.add("file31");
        node = tree.findNode(NameNodeMetaFileTree.root,path);
        tree.renameNode(node,"new_file31",path.size()-1);

        boolean store_success = tree.storeTree();

        String check = "{" +
                "\"data\":null,\"path\":[\"~\"],\"is_file\":false," +
                "\"children\":" +
                    "[" +
                    "{\"data\":null,\"path\":[\"new_dir1\"],\"is_file\":false," +
                    "\"children\":" +
                        "[" +
                        "{\"data\":null,\"path\":[\"new_dir1\",\"file11\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}," +
                        "{\"data\":null,\"path\":[\"new_dir1\",\"file12\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}," +
                        "{\"data\":null,\"path\":[\"new_dir1\",\"dir13\"],\"is_file\":false," +
                        "\"children\":" +
                            "[" +
                            "{\"data\":null,\"path\":[\"new_dir1\",\"dir13\",\"file131\"],\"is_file\":true," +
                            "\"children\":[],\"_file\":true}" +
                            "],\"_file\":false}" +
                        "],\"_file\":false}," +
                        "{\"data\":null,\"path\":[\"file2\"],\"is_file\":true," +
                        "\"children\":[],\"_file\":true}," +
                        "{\"data\":null,\"path\":[\"dir3\"],\"is_file\":false," +
                        "\"children\":" +
                            "[" +
                            "{\"data\":null,\"path\":[\"dir3\",\"new_file31\"],\"is_file\":true," +
                            "\"children\":[],\"_file\":true}" +
                            "],\"_file\":false}" +
                    "],\"_file\":false" +
                "}";

        String real;
        try {
            Path path1 = Paths.get("NameNodeFile/fs_meta_file.json");
            real = new String(Files.readAllBytes(path1));
            Files.delete(path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(check,real);
        assertTrue(store_success);
    }


    private static void buildTree(NameNodeMetaFileTree tree) {
        List<String> path=new ArrayList<>();
        path.add("dir1");
        NameNodeMetaFileNode node = new NameNodeMetaFileNode(null,path,false,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("file2");
        node = new NameNodeMetaFileNode(null,path,true,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir3");
        node = new NameNodeMetaFileNode(null,path,false,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir1");
        path.add("file11");
        node = new NameNodeMetaFileNode(null,path,true,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir1");
        path.add("file12");
        node = new NameNodeMetaFileNode(null,path,true,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir1");
        path.add("dir13");
        node = new NameNodeMetaFileNode(null,path,false,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir3");
        path.add("file31");
        node = new NameNodeMetaFileNode(null,path,true,new ArrayList<>());
        tree.addNode(node);

        path =  new ArrayList<>();
        path.add("dir1");
        path.add("dir13");
        path.add("file131");
        node = new NameNodeMetaFileNode(null,path,true,new ArrayList<>());
        tree.addNode(node);
    }
}