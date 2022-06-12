package tools;

import java.util.ArrayList;

/*
处理用户输入文本 当过长时进行分行
 */
public class WordsSplit {

    public static ArrayList<String> splitWords(String input){
        int length = input.length();
        int begin = 0;//记录切割起始位置
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 1; i < length + 1; i++){
            String tempStr =  input.substring(begin,i);
            System.out.println(WordLength.length(tempStr));
            if ((WordLength.length(tempStr))> 300){
                int temp = begin;
                begin = i;
                if (input.length() > (i + 1)){
                    System.out.println(input.substring(i-2,i+1));
                    if (input.substring(i-1,i+2).matches("%\\d\\d")){
                        tempStr = input.substring(temp, i - 2);
                        begin = i - 2;
                    }
                }
                strList.add(tempStr);
            }
        }
        if (begin != length){
            strList.add(input.substring(begin));
        }
        return strList;
    }
}
