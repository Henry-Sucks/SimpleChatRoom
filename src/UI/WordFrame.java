/*
设置文字气泡框
 */
package UI;

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tools.EmojiFactory;
import tools.WordLength;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrame extends TextFlow {
    private double FrameHeight;
    private double FrameWigth;
    private ArrayList<Text> texts;

    public double getFrameHeight() {
        return FrameHeight;
    }

    public double getFrameWigth() {
        return FrameWigth;
    }

    //构造方法
    public WordFrame(ArrayList<String> strList){
        if(strList.size() == 1){
            String str = strList.get(0);
            FrameWigth = WordLength.length(str);
            Text t = new Text();

        }else{
            FrameWigth = 300;
        }
        int size = strList.size();
        FrameHeight = size * 20;
        if (getChildren() == null){
            while(true)
                System.out.println("yes");
        }
        addIn(strList.get(0));
        for(int i = 1; i < size; i++){
            getChildren().add(new Text("\n"));
            addIn(strList.get(i));
        }
        setPrefWidth(FrameWigth);
        setPrefHeight(FrameHeight);
        setPadding(new Insets(2));
        setLineSpacing(5);

    }

    /*
    区分表情和文本
     */
    public void addIn(String str){
        String regex = "%\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
       if (matcher.find()){
           int start = matcher.start();
           String temp = str.substring(0, start);
           getChildren().add(new Text(temp));
           String index = str.substring(start + 1,start + 3);
           int indexNum = Integer.parseInt(index);
           if (indexNum > 36){
               getChildren().add(new Text("%" + index));
           }else{
               ImageView emojy = new ImageView();
               emojy.setFitWidth(18);
               emojy.setFitHeight(18);
               emojy.setImage(EmojiFactory.getEmojys()[indexNum - 1]);
               ArrayList<ImageView> emojys = new ArrayList<>();
               emojys.add(emojy);
               ImageView emojyTemp;
               int end = matcher.end();
               if (str.length() > end + 3) {
                   while (str.substring(end, end + 3).matches(regex)) {
                       String str1 = str.substring(end + 1, end + 3);
                       int num = Integer.parseInt(str1);
                       if (num < 36) {
                           matcher.find();
                           emojyTemp = new ImageView();
                           emojyTemp.setFitWidth(18);
                           emojyTemp.setFitHeight(18);
                           emojyTemp.setImage(EmojiFactory.getEmojys()[num - 1]);
                           emojys.add(emojyTemp);
                           end += 3;
                       }
                       if (str.length() < end + 3){
                           break;
                       }
                   }
               }
               int size = emojys.size();
               EmojyPane pane = new EmojyPane(size);
               for (int i = 0; i < size; i++){
                   pane.getChildren().add(emojys.get(i));
               }
               getChildren().add(pane);
           }
           addIn(str.substring(matcher.end()));
       }else{
           getChildren().add(new Text(str));
       }

    }

}
