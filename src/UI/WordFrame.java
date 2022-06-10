/*
设置文字气泡框
 */
package UI;

import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tools.WordLength;

import java.util.ArrayList;

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
        getChildren().add(new Text(strList.get(0)));
        for(int i = 1; i < size; i++){
            getChildren().add(new Text("\n"));
            getChildren().add(new Text(strList.get(i)));
        }
        setPrefWidth(FrameWigth);
        setPrefHeight(FrameHeight);
        setPadding(new Insets(2));
        setLineSpacing(5);

    }


}
