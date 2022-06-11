package UI;

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
        setWidth(50 * chlidren);
        setHeight(160);

        for (int i = 0; i < chlidren; i++){
            ImageView emojy = (ImageView) getChildren().get(i);
            System.out.println(i*16);
            emojy.resizeRelocate(i * 16,0,15, 15);
        }


    }
}
