package SelectChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerToClient {
    BufferedReader in;

    public ServerToClient(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getMsg() throws IOException {
        return in.readLine();
    }

    public void sendMsg(String msg, ArrayList<Socket> list) throws IOException {
        for (Socket socket : list) {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(msg);
            out.flush();
        }
    }
}
