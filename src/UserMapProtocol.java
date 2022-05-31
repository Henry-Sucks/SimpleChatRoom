public interface UserMapProtocol {
    // 定义协议字符串的长度
    int PROTOCOL_LEN = 2;

    //下面是一些协议字符串，服务器端和客户端交换的信息都应该在前后添加这种特殊字符串
    String MSG_ROUND="ηθ";
    String LOGIN_ROUND="∏∑";
    String CURNAME_ROUND="θη";
    String LOGIN_SUCCESS="1";
    String NAME_REP="-1";
    String SELECT_ROUND="★【";
    String SPLIT_SIGN="卐";
}
