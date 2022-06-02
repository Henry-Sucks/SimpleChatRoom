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

class ServerToClient{
    BufferedReader in;
    public ServerToClient (Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getMsg() throws IOException {
        return in.readLine();
    }

    public void sendMsg(String msg, ArrayList<Socket> list) throws IOException {
        for(Socket socket : list) {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(msg);
            out.flush();
        }
    }
}

class ClientToServer{
    BufferedReader in;
    PrintWriter output;
    public ClientToServer(Socket socket) throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendToServer(String msg){
        output.println(msg);
        output.flush();
    }

    public String getMsg() throws IOException {
        return in.readLine();
    }
}

