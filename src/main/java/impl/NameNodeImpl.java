package impl;
//TODO: your implementation
import api.NameNodePOA;
import api.file_meta;
import utils.NameNodeMetaFileNode;
import utils.NameNodeMetaFileTree;

public class NameNodeImpl extends NameNodePOA {
    private static final String fs_meta_path = "NameNodeFile/NameNodeMeta";
    private final NameNodeMetaFileTree tree = new NameNodeMetaFileTree(fs_meta_path);
    @Override
    public file_meta open(String filepath, int mode) {
        return null;
    }
    @Override
    public boolean close(String fileInfo) {
        return false;
    }

    @Override
    public boolean mk_dir(String dir_path) {
        return false;
    }

    @Override
    public boolean del_dir(String dir_path) {
        return false;
    }

    @Override
    public boolean change_dir(String old_dir_path, String new_dir_path) {
        return false;
    }

    @Override
    public boolean rename_dir(String old_dir_name, String new_dir_name) {
        return false;
    }

    @Override
    public boolean mk_file(String file_path) {
        return false;
    }

    @Override
    public boolean del_file(String file_path) {
        return false;
    }

    @Override
    public boolean change_file(String old_file_path, String new_file_path) {
        return false;
    }

    @Override
    public boolean rename_file(String old_file_name, String new_file_name) {
        return false;
    }
}
