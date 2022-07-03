package ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class EmojyPane extends Pane {
    private int chlidren;

    public EmojyPane(int num){
        this.chlidren = num;
    }

    @Override
    protected void layoutChildren(){
        if (getChildren().size() == 0){
            return;
        }
        setWidth(20 * chlidren);
        setHeight(20);
        for (int i = 0; i < chlidren; i++){
            ImageView emojy = (ImageView) getChildren().get(i);
            emojy.resizeRelocate(i * 19,0,18, 18);
        }


    }
}
