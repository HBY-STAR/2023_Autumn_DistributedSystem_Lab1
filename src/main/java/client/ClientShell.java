package client;

import api.Client;
import impl.ClientImpl;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientShell {
    private static final Client client = new ClientImpl();
    public static void main(String[] args) {
        System.out.println("Hello! Input command to operate");
        System.out.print(">>>");
        Scanner scanner = new Scanner(System.in);
        do {
            //命令及参数
            String cmd;
            List<String> cmd_args = new ArrayList<>();

            //获取命名和参数并执行对应的操作
            Scanner cmd_scanner = new Scanner(scanner.nextLine());
            if (cmd_scanner.hasNext()) {
                cmd = cmd_scanner.next();
                while (cmd_scanner.hasNext()) {
                    cmd_args.add(cmd_scanner.next());
                }

                //程序结束标志
                if (cmd.equals("exit")) {
                    System.out.println("Bye~");
                    System.exit(0);
                }

                switch (cmd){
                    case "open":{
                        if(cmd_args.size()!=2 || (!cmd_args.get(1).equals("w") && !cmd_args.get(1).equals("r") && !cmd_args.get(1).equals("rw") && !cmd_args.get(1).equals("wr"))){
                            System.out.println("INFO: open格式错误");
                            break;
                        }
                        int mode= 0;
                        switch (cmd_args.get(1)){
                            case "r":{
                                mode = 0b01;
                                break;
                            }
                            case "w":{
                                mode = 0b10;
                                break;
                            }
                            case "rw":
                            case "wr": {
                                mode = 0b11;
                                break;
                            }
                        }
                        int fd = client.open(cmd_args.get(0),mode);
                        if(fd==-1){
                            System.out.println("INFO: Open failed");
                        }else {
                            System.out.println("INFO: fd = "+fd);
                        }
                        break;
                    }
                    case "read":{
                        if(cmd_args.size()!=1){
                            System.out.println("INFO: read格式错误");
                            break;
                        }
                        int fd =-1;
                        try {
                            fd = Integer.parseInt(cmd_args.get(0));
                        }catch (NumberFormatException e){
                            System.out.println("INFO: read格式错误");
                            break;
                        }
                        byte[] read = client.read(fd);
                        if(read==null){
                            System.out.println("INFO: read failed");
                            break;
                        }
                        System.out.println(new String(read));
                        break;
                    }
                    case "append":{
                        if(cmd_args.size()<2){
                            System.out.println("INFO: append格式错误");
                            break;
                        }
                        int fd =-1;
                        try {
                            fd = Integer.parseInt(cmd_args.get(0));
                        }catch (NumberFormatException e){
                            System.out.println("INFO: read格式错误");
                            break;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i=1;i<cmd_args.size();i++){
                            stringBuilder.append(cmd_args.get(i));
                            if(i<cmd_args.size()-1){
                                stringBuilder.append(" ");
                            }
                        }
                        byte[] append  = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
                        client.append(fd,append);
                        break;
                    }
                    case "close":{
                        if(cmd_args.size()!=1){
                            System.out.println("INFO: append格式错误");
                            break;
                        }
                        int fd =-1;
                        try {
                            fd = Integer.parseInt(cmd_args.get(0));
                        }catch (NumberFormatException e){
                            System.out.println("INFO: close格式错误");
                            break;
                        }
                        client.close(fd);
                        break;
                    }
                    case "help":{
                        System.out.println("|--------HELP---------|");
                        System.out.println("| open filename mode  |");
                        System.out.println("| read fd             |");
                        System.out.println("| append fd ...       |");
                        System.out.println("| close fd            |");
                        System.out.println("|--------END----------|");
                        break;
                    }
                    default:{
                        System.out.println("无效命令！");
                        break;

                    }
                }
            } else {
                System.out.println("输入为空!");
            }
            System.out.print(">>>");
        }
        while (scanner.hasNextLine());
    }
}
