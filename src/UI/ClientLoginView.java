//用户端登录界面
package UI;

import Client.ClientReadAndPrint;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;



public class ClientLoginView extends Application{
    private ClientReadAndPrint.LoginHandler loginHandler = null;

    @Override
    public void start(Stage primaryStage){
        GridPane pane = new GridPane();
        BackgroundImage myBI= new BackgroundImage(new Image("file:D:\\俄罗斯方块1234\\登录背景.jpeg",450,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11,12,13,14));
        pane.setHgap(0);
        pane.setVgap(5);
        //环形X
        Group textGroup = new Group();
        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20);
        Font font1 = Font.font("黑体", FontWeight.BOLD, FontPosture.REGULAR, 15);
        String welcome = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        double rotation = -20;
        double radius = 61d;
        for (char c : welcome.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                Label text = new Label(Character.toString(c));
                text.setFont(font);
                text.setTextFill(Color.color(Math.random(), Math.random(), Math.random(), Math.random()));
                Rotate rotationMatrix = new Rotate(rotation, 0, radius);
                text.getTransforms().add(rotationMatrix);
                textGroup.getChildren().add(text);
            }
            rotation += 7;
        }

        pane.getChildren().add(textGroup);
        //设置文字label及其格式
        Label label1 = new Label("     Welcome");
        Label label2 = new Label("用户名:");
        Label label3 = new Label("密码:");
        label1.setFont(font);
        label2.setFont(font1);
        label3.setFont(font1);
        label2.setTextFill(Color.color(0.6,0.6,0.6,0.9));
        label3.setTextFill(Color.color(0.6,0.6,0.6,0.9));
        pane.add(label1,0,0);
        pane.add(label2,0,1);
        final TextField text = new TextField();
        pane.add(text,1,1);
        pane.add(label3,0,2);
        final PasswordField pb = new PasswordField();
        pane.add(pb,1,2);
        Button bt=new Button("登录");
        pane.add(bt,1,3);
        GridPane.setHalignment(bt,HPos.CENTER);

        //处理用户的登录请求
        loginHandler = new ClientReadAndPrint(). new LoginHandler();
        loginHandler.setStage(primaryStage);
        loginHandler.setPasswordField(pb);
        loginHandler.setTextField(text);
        bt.setOnAction(loginHandler);

        Scene scene=new Scene(pane,450,300);
        primaryStage.setTitle("JavaFx Welcome");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String args){
        Application.launch(args);
    }

}