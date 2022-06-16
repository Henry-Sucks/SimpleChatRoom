package UI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tools.EmojiFactory;


public class EmojiChooseView extends Application {
    private TextField text = null;
    private GridPane pane = null;

    public void setText(TextField text) {
        this.text = text;
    }

    @Override
    public void start(Stage primaryStage){
        GridPane pane = new GridPane();
        this.pane = pane;
        BackgroundImage myBI= new BackgroundImage(new Image("Source/Background/表情背景.jpeg",450,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));
        EmojiFactory.init();
        ImageView[] emojis = EmojiFactory.getEmojyViews();
        //将图片加入到GridPane中
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 6; j++){
                int index = 6 * i + j;
                pane.add(emojis[index], j, i);
                //设置鼠标点击事件
                emojis[index].setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event)
                    {
                        addEmoji(event, index + 1);
                    }

                });
            }
        }

        pane.setHgap(10);
        pane.setVgap(10);
        Scene scene=new Scene(pane,230,230);
        primaryStage.setTitle("Emoji");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void addEmoji(MouseEvent event, int index){
        if (event.getButton() == MouseButton.PRIMARY){
            if (event.getClickCount() == 1){
                if (index < 10){
                    text.appendText("%" + 0 + index);
                }else{
                    text.appendText("%" + index);
                }
            }
        }
        if (event.getButton() == MouseButton.PRIMARY){
            if (event.getClickCount() == 2){
                if (index < 10){
                    text.appendText("%" + 0 + index);
                    text.appendText("%" + 0 + index);
                }else{
                    text.appendText("%" + index);
                    text.appendText("%" + index);
                }
            }
        }
    }

    public void run(){
        start(new Stage());
    }

    public static void main(String args){
        Application.launch(args);
    }
}
