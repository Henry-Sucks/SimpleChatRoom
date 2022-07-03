package user;

import java.util.Collections;
import java.util.LinkedList;

public class User implements Comparable<Object>{
    public int compareTo(Object b) {
        User user = (User)b;
        if(user.if_login == this.if_login)	return this.user_id - user.user_id;
        else if(user.if_login) return 1;
        else return -1;
    }

    private static int User_count = 0;
    private int user_id;
    private String name;
    private String password;

    private String email;
    private String sign;

    private String icon;

    private int sex;

    private boolean if_login = false; // 默认在线状态为false
    private LinkedList<User> friend_list = new LinkedList<User>();

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        user_id = User_count;
        User_count++;
    }

    public int getId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String new_s) {
        sign = new_s;
    }
    /*
     * 根据用户id返回用户
     */
//    public User getUser(int id) {
//        for(User i : User_list) {
//            if(i.user_id == id) {
//                return i;
//            }
//        }
//        return null;
//    }

    public void setId(int newId) {
        user_id = newId;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /*
     * 设置一个新用户
     */
    public static void addUser(String name, String password, String email) {
        new User(name, password, email);
    }

    /*
     * 添加好友
     */
    public void addFriend(User friend) {
        friend_list.add(friend);
    }

    public boolean getIf_login() {
        return if_login;
    }

    private void setIf_login(boolean login) {
        if_login = login;
    }

    /*
     * 登录用户并重排所有含该用户的friend列表
     */
//    public void User_login(User user) {
//        user.if_login = true;
//        for(User i : User_list) {
//            if(i.friend_list.contains(user) && i.if_login) {
//                i.sort_friend();
//            }
//        }
//    }

    /*
     * 登出用户并重排所有含该用户的friend列表
     */
//    public void User_logout(User user) {
//        user.if_login = false;
//        for(User i : User_list) {
//            if(i.friend_list.contains(user) && i.if_login) {
//                i.sort_friend();
//            }
//        }
//    }
    public void sort_friend() {
        Collections.sort(friend_list);
    }

    // 测试，不用管
//    public static void main(String[] args) {
//        User a = new User("a", "ap");
//        User b = new User("b", "bp");
//        User c = new User("c", "cp");
//        a.setIf_login(true);
//        a.addFriend(b);
//        a.addFriend(c);
//        a.sort_friend();
//        System.out.println("b c both logout:");
//        for(User i : a.friend_list) {
//            System.out.println(i.name);
//        }
//        c.setIf_login(true);
//        a.sort_friend();
//        System.out.println("c is login:");
//        for(User i : a.friend_list) {
//            System.out.println(i.name);
//        }
//        b.setIf_login(true);
//        c.setIf_login(true);
//        a.sort_friend();
//        System.out.println("b  c both login:");
//        for(User i : a.friend_list) {
//            System.out.println(i.name);
//        }
//    }
}
