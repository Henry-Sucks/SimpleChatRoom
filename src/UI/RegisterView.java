//用户端登录界面
package UI;

import Client.ClientReadAndPrint;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;



public class RegisterView extends Application{
    private ClientReadAndPrint.LoginHandler loginHandler = null;

    @Override
    public void start(Stage primaryStage){
        GridPane pane = new GridPane();

        BackgroundImage myBI= new BackgroundImage(new Image("Source/Background/注册背景.jpeg",450,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11,12,13,14));
        pane.setHgap(30);
        pane.setVgap(20);

        Font font1 = Font.font("黑体", FontWeight.BOLD, FontPosture.REGULAR, 15);
        Label label2 = new Label("用户名:");
        Label label3 = new Label("密码:");
        Label label4 = new Label("确认密码:");
        Label label5 = new Label("邮箱");
        label5.setFont(font1);
        label2.setFont(font1);
        label3.setFont(font1);
        label4.setFont(font1);
        label2.setTextFill(Color.color(0.9,0.9,0.9,0.99));
        label3.setTextFill(Color.color(0.9,0.9,0.9,0.99));
        label4.setTextFill(Color.color(0.9,0.9,0.9,0.99));
        label5.setTextFill(Color.color(0.9,0.9,0.9,0.99));
        pane.add(label2,0,0);
        final TextField text = new TextField();
        text.setPromptText("👩");
        pane.add(text,1,0);
        pane.add(label3,0,1);
        final PasswordField pb = new PasswordField();
        pb.setPromptText("🔒");
        pane.add(pb,1,1);
        pane.add(label4,0,2);
        final PasswordField pb1 = new PasswordField();
        pb1.setPromptText("🔒");
        pane.add(pb1,1,2);
        pane.add(label5,0,3);
        final TextField textEmail = new TextField();
        textEmail.setPromptText("@");
        pane.add(textEmail,1,3);
        Button bt = new Button("注册");
        bt.setTextFill(Paint.valueOf("#88c8fc"));
        bt.setEffect(new DropShadow(50,100,100,Color.valueOf("#008fdd")));
        pane.add(bt,1,4);
        GridPane.setHalignment(bt,HPos.CENTER);

        //处理用户的登录请求
        loginHandler = new ClientReadAndPrint(). new LoginHandler();
        loginHandler.setStage(primaryStage);
        loginHandler.setPasswordField(pb);
        loginHandler.setTextField(text);
        bt.setOnAction(loginHandler);

        Scene scene=new Scene(pane,450,300);
        primaryStage.setTitle("ChatRoom Register");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public void run(){
        start(new Stage());
    }

    public static void main(String args){
        Application.launch(args);
    }

}