package UI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tools.IconImage;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class UserMenu extends Application {
    @FXML
    private AnchorPane userMenu;
    @FXML
    private Text uidText, sexText, userNameText, emailText, signText;


    @FXML
    private TextField userNameInput, emailInput, signInput;

    @FXML
    private ChoiceBox<String> sexBox;

    @FXML
    private AnchorPane iconPane;

    private Info info;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void initialize(){
        // 从哪里读取数据？
        info = new Info();

        // 背景
        BackgroundImage BI = new BackgroundImage(new Image(sysSrc + "\\menu-background.jpg", 450, 300, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        userMenu.setBackground(new Background(BI));

//         字体
        Font font1 = Font.font("黑体", FontWeight.BOLD, FontPosture.REGULAR, 15);
        userNameText.setFont(font1);
        emailText.setFont(font1);
        sexText.setFont(font1);
        signText.setFont(font1);
        uidText.setFont(font1);

        userNameText.setFill(Color.web("#43035EFF"));
        emailText.setFill(Color.web("#43035EFF"));
        sexText.setFill(Color.web("#43035EFF"));
        signText.setFill(Color.web("#43035EFF"));
        uidText.setFill(Color.web("#43035EFF"));

        userNameText.setStroke(Color.web("#F8EBFEFF"));
        userNameText.setStrokeWidth(2);
        emailText.setStroke(Color.web("#F8EBFEFF"));
        emailText.setStrokeWidth(2);
        sexText.setStroke(Color.web("#F8EBFEFF"));
        sexText.setStrokeWidth(2);
        signText.setStroke(Color.web("#F8EBFEFF"));
        signText.setStrokeWidth(2);
        uidText.setStroke(Color.web("#F8EBFEFF"));
        uidText.setStrokeWidth(2);


        // 头像
        IconImage icon = new IconImage();
        if(info.iconSrc != null){
            icon.setImage(new Image(info.iconSrc));
            icon.setPrefSize(110, 110);
        }
        else{
            icon.setImage(new Image(sysSrc + '\\' + "default-icon.png"));
            icon.setPrefSize(110, 110);
        }
        iconPane.getChildren().add(icon);

        // uid
        uidText.setText("uid:" + info.uid);

        // 性别
        sexText.setText("性别");
        String[] sex = {"男", "女", "保密"};
        sexBox.getItems().addAll(sex);
        if(info.sex == 1){
            sexBox.setValue("男");
        }
        else if(info.sex == 0){
            // 默认值为0！
            sexBox.setValue("保密");
        }
        else if(info.sex == -1){
            sexBox.setValue("女");
        }

        // 姓名
        userNameText.setText("用户名");
        info.userName = "123";
        if(info.userName != null)
            userNameInput.setPromptText("👩" + info.userName);
        else
            userNameInput.setPromptText("👩" + "");
        // 邮箱
        emailText.setText("用户邮箱");
        if(info.email != null)
            emailInput.setPromptText("@" + info.email);
        else
            emailInput.setPromptText("@" + "");

        // 签名
        signText.setText("用户签名");
        signInput.setAlignment(Pos.TOP_LEFT);
        if(info.sign == null)
            signInput.setPromptText("\uD83D\uDD8A" + "写一条个性签名吧（不要超过60个字哦~）");
        else
            signInput.setPromptText("\uD83D\uDD8A️" + info.sign);
    }


    public void getInfo(String userName){
    }

    public void modifyUserName(String newName){

    }

    public void modifySex(int newSex){

    }

    public void modifyIcon(String newSrc){

    }

    public void modifyEmail(String newEmail){

    }

    public void modifySign(String newSign){

    }


    static class Info{
        private String iconSrc;
        private int uid;
        private int sex;
        private String userName;
        private String email;
        private String sign;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
