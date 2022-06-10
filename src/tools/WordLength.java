/*
判断String长度来确定文本框长度

 */
package tools;

public class WordLength {
    public static double length(String str){
        char[] chars = str.toCharArray();
        double length = 0;
        for (char c :chars){
            if (IsChinese(c)){
                length += 1.85;
            }else{
                length += 1;
            }
        }
        return length * 7.5 + 5;
    }

    public static boolean IsChinese(char c)
    {
        //通过字节码进行判断
        return c >= 0x4E00 && c <= 0x29FA5;
    }
}
