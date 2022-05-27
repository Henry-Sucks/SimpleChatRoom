public class Client {
    static public int port = 8090;
    static private boolean online = false;
    // 主函数，新建登录窗口
    public static void main(String[] args){
        new LoginView();
    }

    public static boolean getOnline(){
        return online;
    }
    public static void setOnline(boolean i){
        online = i;
    }
}