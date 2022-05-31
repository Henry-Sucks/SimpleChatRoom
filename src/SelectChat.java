import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SelectChat {
    static class TextChat {
        public synchronized static void toEveryone(String msg, Socket nowSocket, UserMap<String, Socket> users) throws Exception {
            for (Socket socket : users.valueSet()) {
                if (socket != nowSocket) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println(msg);
                    out.flush();
                }
            }
        }

        public synchronized static void toSelectOne(String msg, ArrayList<String> recvList, UserMap<String, Socket> users) throws Exception {
            for (String userName : recvList) {
                Socket socket = users.getValueByKey(userName);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(msg);
                out.flush();
            }
        }

        public synchronized static void toOneself(String msg, Socket socket) throws Exception {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(msg);
            out.flush();
        }
    }
}
