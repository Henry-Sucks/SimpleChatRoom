package UI;

import Client.ClientFileThread;
import Client.ClientReadAndPrint;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ClientChatView extends Application {
    private String userName;
    private TextFlow textFlow;
    private ScrollPane sp;
    private TextField  textIn = new TextField();
    private Button send = new Button("发送(S)");
    private Button fileChoose = new Button("发送文件");
    private ClientReadAndPrint.ChatViewHandler chatHandler;
    private FileChooseHnadler fileHandler;

    public void run(){
        start(new Stage());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void start(Stage primaryStage){
        try{
            // 最下端的消息编辑区
            textIn.setBlendMode(BlendMode.GREEN);
            HBox downBox = new HBox();
            downBox.getChildren().addAll(textIn, send, fileChoose);
            downBox.setPadding(new Insets(10));
            downBox.setSpacing(5);
            HBox.setHgrow(textIn, Priority.ALWAYS);
            BackgroundSize backgroundSize = new BackgroundSize(100,150,true,true,true,true);
            BackgroundImage myBI= new BackgroundImage(new Image("Source/Background/聊天背景.jpeg",450,300,false,true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    backgroundSize);
            downBox.setBackground(new Background(myBI));


            //TextFlow支持显示富文本
            textFlow = new TextFlow();
            textFlow.setPadding(new Insets(5));
            textFlow.setLineSpacing(20.0f);


            //可滚动窗口
            sp = new ScrollPane();
            sp.setPrefSize(600, 400);
            sp.setContent(textFlow);
            sp.setVvalue(1D);
            textFlow.heightProperty().addListener(observable -> sp.setVvalue(1D));//自动滚动


            BorderPane root = new BorderPane();
            root.setPrefSize(1000,600);
            Group group = new Group();
            group.getChildren().addAll(sp);
            root.setTop(group);
            root.setBottom(downBox);
            root.setBackground(new Background(myBI));

            //处理发送按钮
            chatHandler = new ClientReadAndPrint(). new ChatViewHandler();
            chatHandler.setClientView(primaryStage);
            chatHandler.setJTextArea(textFlow);
            chatHandler.setJTextField(textIn);
            chatHandler.setScrollPane(sp);
            send.setOnAction(chatHandler);
            send.setEffect(new SepiaTone());

            //处理文件按钮
            fileHandler = new FileChooseHnadler();
            fileChoose.setOnAction(fileHandler);
            fileChoose.setEffect(new SepiaTone());

            //设置行间距
            textFlow.setLineSpacing(10);
            Scene scene = new Scene(root, 1000, 600);

            // 为发送设置快捷键ctrl+s
            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
                    new Runnable() {
                        @FXML
                        public void run() {
                            send.fire();
                        }
                    }
            );

            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    private class FileChooseHnadler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            FileChooser chooser = new FileChooser();
            chooser.setTitle("打开目录");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("图片类型","*.jpg", "*.png"),
                    new FileChooser.ExtensionFilter("文本类型","*.txt"),
                    new FileChooser.ExtensionFilter("全部类型", "*.*")
            );
            File dir = chooser.showOpenDialog(new Stage());
            if (dir == null){
                return;
            }else{
                String path = dir.getAbsolutePath();
                ClientFileThread.outFileToServer(path);
            }
        }
    }
}

