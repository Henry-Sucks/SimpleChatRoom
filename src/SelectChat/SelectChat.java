package SelectChat;

import Global.UserMap;
import Global.UserProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SelectChat {
    public static ArrayList<Socket> getSelectList(String clientMsg, UserMap<String, Socket> users){
        ArrayList<String> recvList = new ArrayList<>();
        int num = Integer.parseInt(clientMsg.split(UserProtocol.SPLIT_SIGN)[0]);
        for(int i = 1; i <= num; i++){
            System.out.println("用户名：" + clientMsg.split(UserProtocol.SPLIT_SIGN)[i]);
            recvList.add(clientMsg.split(UserProtocol.SPLIT_SIGN)[i]);
        }
        ArrayList<Socket> userList = new ArrayList<>();
        for (String userName : recvList) {
            Socket socket = users.getValueByKey(userName);
            userList.add(socket);
        }
        return userList;
    }

    public static ArrayList<String> getNameList(String clientMsg){
        ArrayList<String> recvList = new ArrayList<>();
        int num = Integer.parseInt(clientMsg.split(UserProtocol.SPLIT_SIGN)[0]);
        for(int i = 1; i <= num; i++){
            System.out.println("用户名：" + clientMsg.split(UserProtocol.SPLIT_SIGN)[i]);
            recvList.add(clientMsg.split(UserProtocol.SPLIT_SIGN)[i]);
        }
        return recvList;
    }
}

