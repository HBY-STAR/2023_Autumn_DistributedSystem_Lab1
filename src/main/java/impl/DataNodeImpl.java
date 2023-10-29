package impl;
//TODO: your implementation
import api.DataNodePOA;

public class DataNodeImpl extends DataNodePOA {
    private static final int MAX_BLOCK_NUM = 100;
    private static final int BLOCK_SIZE = 4096;
    @Override
    public byte[] read(int block_id) {
        return new byte[0];
    }

    @Override
    public void append(int block_id, byte[] bytes) {
    }

    @Override
    public int randomBlockId() {
        return 0;
    }


}
