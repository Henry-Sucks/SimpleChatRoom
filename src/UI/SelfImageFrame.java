package UI;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

public class SelfImageFrame extends TextFlow {
    @Override
    protected void layoutChildren(){
        if (getChildren().size() == 0){
            return;
        }
        double frameWidth = getWidth();
        // 对ImageView进行摆放
        ImageView imageView = (ImageView)getChildren().get(0);
        imageView.setPreserveRatio(true);
        imageView.setX(0);
        imageView.setY(0);
        setWidth(600);
        imageView.resizeRelocate( 600 - 50,0,50,50);

        ImageView imageFrame = (ImageView) getChildren().get(1);
        double hight = imageFrame.getFitHeight();
        double width = imageFrame.getFitWidth();
        imageFrame.resizeRelocate(600- 60 - width,25,imageFrame.getFitWidth(), imageFrame.getFitHeight());

        Pane name = (Pane) getChildren().get(2); //获取用户名
        double nameWideth = name.getWidth();
        double nameHight = name.getHeight();
        name.resizeRelocate(600 - 90 - nameWideth,15,nameWideth, nameHight);
    }

}



