package UI;

import Client.ClientFileThread;
import Client.ClientReadAndPrint;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ClientChatView extends Application {
    private String userName;
    private TextFlow textFlow;
    private ScrollPane sp;
    private TextField  textIn = new TextField();
    private Button send = new Button("发送");
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
            HBox downBox = new HBox();
            downBox.getChildren().addAll(textIn, send, fileChoose);
            downBox.setPadding(new Insets(10));
            downBox.setSpacing(5);
            HBox.setHgrow(textIn, Priority.ALWAYS);

            //TextFlow支持显示富文本
            textFlow = new TextFlow();
            ImageView iv = new ImageView();
            Image icon = new Image("file:D:\\俄罗斯方块1234\\登录背景.jpeg");
            iv.setPreserveRatio(true);
            iv.setImage(icon);
            iv.setFitHeight(100);
            iv.setFitWidth(100);
            Label label = new Label("hahah");
            Label label1 = new Label("123");
            Text t1 = new Text("11111\n");
            textFlow.getChildren().add(iv);
            textFlow.getChildren().add(t1);
            textFlow.getChildren().add(label1);
            textFlow.getChildren().add(label);
            textFlow.setLineSpacing(20.0f);
            for (int i = 0; i < 100; i++){
                textFlow.getChildren().add(new Text("haha\n"));
            }

            //可滚动窗口
            sp = new ScrollPane();
            sp.setPrefSize(400, 400);
            sp.setContent(textFlow);
            sp.setVvalue(1D);
            //sp.setBackground(); 设置聊天的背景 后续可能用的上

            //将布局加入到根结点内
            BorderPane root = new BorderPane();
            root.setPrefSize(400,600);
            Group group = new Group();
            group.getChildren().addAll(sp);
            root.setTop(group);
            root.setBottom(downBox);

            //处理发送按钮
            chatHandler = new ClientReadAndPrint(). new ChatViewHandler();
            chatHandler.setClientView(primaryStage);
            chatHandler.setJTextArea(textFlow);
            chatHandler.setJTextField(textIn);
            chatHandler.setScrollPane(sp);
            send.setOnAction(chatHandler);

            //处理文件按钮
            fileHandler = new FileChooseHnadler();
            fileChoose.setOnAction(fileHandler);

            Scene scene = new Scene(root, 400, 450);
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

