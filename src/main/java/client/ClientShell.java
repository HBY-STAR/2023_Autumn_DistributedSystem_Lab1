package client;

import api.DataNode;
import api.DataNodeHelper;
import api.NameNode;
import api.NameNodeHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Properties;

public class ClientShell {
    public static final int MAX_DATA_NODE = 5;
    private static NameNode nameNode;
    private static final DataNode[] dataNodes = new DataNode[MAX_DATA_NODE];

    public static void main(String[] args) {
        GetNameNodeAndDataNode(args);
    }

    private static void GetNameNodeAndDataNode(String[] args) {
        try{
            Properties properties = new Properties();
            properties.put("org.omg.CORBA.ORBInitialHost","127.0.0.1");
            properties.put("org.omg.CORBA.ORBInitialPort","15000");

            ORB orb = ORB.init(args,properties);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            nameNode = NameNodeHelper.narrow(ncRef.resolve_str("NameNode"));
            System.out.println("NameNode is obtained");

            for(int dataNodeId = 0;dataNodeId<MAX_DATA_NODE;dataNodeId++){
                dataNodes[dataNodeId] = DataNodeHelper.narrow(ncRef.resolve_str("DataNode"+dataNodeId));
                System.out.println("DataNode"+dataNodeId+" is obtained");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
