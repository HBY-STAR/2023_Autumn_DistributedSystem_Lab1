package utils;

import api.file_meta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameNodeMetaFileNode {
    public file_meta data;
    public List<String> path;
    public boolean is_file;
    //    private int nodeId;
    //    private int parentId;
    public List<NameNodeMetaFileNode> children = null;
}
