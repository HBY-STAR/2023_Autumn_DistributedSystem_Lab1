package utils;

import api.FileMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameNodeMetaFileNode {
    public FileMeta data;       //文件元数据
    public List<String> path;   //文件路径
    public boolean is_file;     //区分文件和目录
    public List<NameNodeMetaFileNode> children = null;  //子节点
}
