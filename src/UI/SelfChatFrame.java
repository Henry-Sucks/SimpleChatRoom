package UI;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

public class SelfChatFrame extends TextFlow {

    @Override
    protected void layoutChildren(){
        if (getChildren().size() == 0){
            return;
        }
        // 对ImageView进行摆放
        ImageView imageView = (ImageView)getChildren().get(0);
        imageView.setPreserveRatio(true);
        WordFrame wordFrame = (WordFrame) getChildren().get(1);
        Pane name = (Pane) getChildren().get(2); //获取用户名

        double wordWidth = wordFrame.getFrameWigth();
        double wordHeight = wordFrame.getFrameHeight();
        double nameWideth = name.getWidth();
        double nameHight = name.getHeight();
        setWidth(600);
        setHeight(wordHeight + 25);

        double width = getWidth();
        imageView.setX(0);
        imageView.setY(0);
        imageView.resizeRelocate( width- 50,0,50,50);

        wordFrame.resizeRelocate(width - 60 - wordWidth,25,wordFrame.getFrameWigth(), wordFrame.getFrameHeight());
        name.resizeRelocate(width - 90 - nameWideth,15,nameWideth, nameHight);

    }
}
