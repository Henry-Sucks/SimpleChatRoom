package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

//只是用来测试一些方法的 没有实际意义
public class Test extends Application {

    @Override
    public void start (Stage primaryStage){
        try{
            TextFlow test = new TextFlow();
            Image image = new Image("source\\Background\\登录背景.jpeg");
            BackgroundSize backgroundSize = new BackgroundSize(600, test.getHeight(), true, true, true, false);
            BackgroundImage myBI= new BackgroundImage(new Image("Source/Background/聊天背景.jpeg",450,300,false,true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    backgroundSize);

            test.setBackground(new Background(myBI));
            ImageView iv = new ImageView();
            iv.setFitWidth(50);
            iv.setFitHeight(50);
            iv.setImage(image);
            SelfChatFrame chat = new SelfChatFrame();
            //chat.setPrefWidth(400);
            //chat.setPrefHeight(300);
            Color bkColor1 = new Color(0.3,0.9,0.3,0.3);
           // chat.setBackground(new Background(new BackgroundFill(bkColor1,null, null)));
            chat.getChildren().add(iv);
            ArrayList<String> str = new ArrayList<>();
            for(int i =0; i < 30; i++)
                str.add("hahahahahahahahahahahaha你好呀");
            //str.add("1111111111111111111111111111111111111111");
            WordFrame wd = new WordFrame(str);
            Color bkColor = new Color(0.3,0.3,0.3,0.3);
            //wd.setBackground(new Background(new BackgroundFill(bkColor,null, null)));
            chat.getChildren().add(wd);
            test.getChildren().add(chat);
            Scene scene = new Scene(test, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
