package server;

import api.DataNode;
import api.DataNodeHelper;
import impl.DataNodeImpl;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class DataNodeLauncher {

    public static void main(String[] args) {
        try{
            if(args.length==0){
                System.out.println("请传入要启动的节点id");
                return;
            }
            int dataNodeId;
            try {
                dataNodeId=Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("节点ID应为0-9的数字");
                throw new RuntimeException(e);
            }
            if(dataNodeId<0||dataNodeId>9){
                System.out.println("节点ID应为0-9的数字");
                return;
            }
            Properties properties = new Properties();
            properties.put("org.omg.CORBA.ORBInitialHost","127.0.0.1");
            properties.put("org.omg.CORBA.ORBInitialPort","15000");

            ORB orb = ORB.init(args,properties);

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            DataNodeImpl dataNodeServant = new DataNodeImpl();
            dataNodeServant.setDateNodeId(dataNodeId);

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(dataNodeServant);
            DataNode href = DataNodeHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            NameComponent[] path = ncRef.to_name("DataNode"+dataNodeId);
            ncRef.rebind(path,href);
            System.out.println("DataNode"+dataNodeId+ " is ready and waiting......");
            orb.run();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
