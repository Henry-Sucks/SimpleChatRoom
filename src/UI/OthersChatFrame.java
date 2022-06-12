package UI;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

public class OthersChatFrame extends TextFlow {
    @Override
    protected void layoutChildren(){
        if (getChildren().size() == 0){
            return;
        }
        // 对ImageView进行摆放
        ImageView imageView = (ImageView)getChildren().get(0);
        imageView.setPreserveRatio(true);
        imageView.setX(0);
        imageView.setY(0);
        setWidth(600);
        imageView.resizeRelocate(0,0,50,50);

        WordFrame wordFrame = (WordFrame) getChildren().get(1);
        wordFrame.resizeRelocate(60,25,wordFrame.getFrameWigth(), wordFrame.getFrameHeight());

        Pane name = (Pane) getChildren().get(2); //获取用户名
        name.resizeRelocate(60,15,name.getWidth(),20);
    }

}



