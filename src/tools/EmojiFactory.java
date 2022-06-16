package tools;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EmojiFactory {
    private static Image[] emojys = new Image[36];
    private static ImageView[] emojyViews = new ImageView[36];

    public static Image[] getEmojys() {
        return emojys;
    }

    public static ImageView[] getEmojyViews() {
        return emojyViews;
    }

    public static void init(){
        for (int i = 1; i <37; i++){
            Image image = new Image("source\\Emoji\\" + i +".png");
            emojys[i-1] = image;
            ImageView iv = new ImageView();
            iv.setFitHeight(30);
            iv.setFitWidth(30);
            iv.setImage(image);
            emojyViews[i-1] = iv;
        }
    }
}


