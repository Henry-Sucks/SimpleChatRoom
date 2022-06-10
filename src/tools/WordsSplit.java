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
            if ((WordLength.length(tempStr))> 300){
                strList.add(tempStr);
                begin = i;
            }
        }
        if (begin != length){
            strList.add(input.substring(begin));
        }
        return strList;
    }
}
