
package Client;
import Global.UserProtocol;
import UI.*;
import javafx.application.Application;

public class Client {
    static private boolean online = false;

    // 主函数，新建登录窗口
    public static void main(String[] args){
        Application.launch(ClientLoginView.class);
    }

    public static boolean getOnline(){
        return online;
    }
    public static void setOnline(boolean i){
        online = i;
    }


    static public String getRealMsg(String line) {
        return line.substring(UserProtocol.PROTOCOL_LEN,line.length()- UserProtocol.PROTOCOL_LEN);
    }
}