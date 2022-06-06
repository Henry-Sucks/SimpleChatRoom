package SelectChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientToServer {
    BufferedReader in;
    PrintWriter output;

    public ClientToServer(Socket socket) throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendToServer(String msg) {
        output.println(msg);
        output.flush();
    }

    public String getMsg() throws IOException {
        return in.readLine();
    }
}
