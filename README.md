# SimpleChatRoom
A chat room based on Java

## 群聊与私聊的功能实现
### 新增文件
GlobalSettings UserMap UserProtocol SelectiveChat
### 注意点：
文字收发和文件收发所建立的Socket，其port是不同的。(见GlobalSettings)
因此每次新建功能Thread时需要增添port
### 主要原理：
1) 客户端首次与服务端连接时，服务端将客户端socket以"用户名-socket"的形式存储在UserMap里
2) 客户端向服务端发送**系统消息**以开启私发模式
    * 系统消息的格式见UserMapProtocol
    * 服务端与客户端间使用ClientToServer和ServerToClient沟通
3) 服务端根据系统消息从map中选择socket进行发送

