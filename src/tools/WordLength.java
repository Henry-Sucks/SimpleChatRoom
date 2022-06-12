/*
判断String长度来确定文本框长度

 */
package tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return length * 7.5 + 7 - hasEmojy(str) * 5;
    }

    public static boolean IsChinese(char c)
    {
        //通过字节码进行判断
        return c >= 0x4E00 && c <= 0x29FA5;
    }

    public static int hasEmojy(String str){
        int count = 0;
        String regex = "%\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            int start = matcher.start();
            String temp = str.substring(start + 1, start + 3);
            int num = Integer.parseInt(temp);
            if (num < 37){
                count++;
            }
        }
        return count;
    }
}
