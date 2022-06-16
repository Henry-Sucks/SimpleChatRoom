package MediaPlayer;

import animatefx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static MediaPlayer.MediaPlayerGlobal.*;

public class MenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane menuPane;

    @FXML
    Button buttonLm, buttonWn, buttonMl, buttonExit;

    @FXML
    private Text recommendText, welcomeText;

    private FXMLLoader loader;
    private PlayerController playerController;

    Text text1 = new Text();
    Text text2 = new Text();
    Text text3 = new Text();
    Text text4 = new Text();

    BooleanProperty ifPlayerStart = new SimpleBooleanProperty(false);

    /** 与聊天室的联系 **/
    private String userName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Menu initialized");
        loader = new FXMLLoader(getClass().getResource("Player.fxml"));

        welcomeText.setVisible(false);
        recommendText.setVisible(false);

        // 设置背景
        BackgroundImage backgroundImage= new BackgroundImage(new Image(sysSrc + '\\' + "yoga.jpg",900,675,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        menuPane.setBackground(new Background(backgroundImage));

        // 设置按钮
        buttonExit.setText("");
        buttonExit.setBackground(null);
        Image image = new Image(sysSrc + "\\" + "exit.png", 100, 100, true, false);
        buttonExit.setGraphic(new ImageView(image));

        image = new Image(sysSrc + "\\" + "music-notes.png",100, 100, true, false);
        buttonLm.setGraphic(new ImageView(image));
        buttonLm.setBackground(null);

        image = new Image(sysSrc + "\\" + "forest.png",100, 100, true, false);
        buttonWn.setGraphic(new ImageView(image));
        buttonWn.setBackground(null);

        image = new Image(sysSrc + "\\" + "listening.png",100, 100, true, false);
        buttonMl.setGraphic(new ImageView(image));
        buttonMl.setBackground(null);

        // 标语动画
        welcomeText.setVisible(true);
        recommendText.setVisible(true);
        new FadeInUp(welcomeText).play();
        new FadeInDown(recommendText).play();
    }


    // player.fxml必须编译通过才能正常显示
    private void switchToPlayer(MouseEvent event, String playList) throws IOException{
        try {
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            // 设置样式：透明
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        } catch (Exception e){
            e.printStackTrace();
        }

        playerController = loader.getController();
        playerController.playlistInit(playList);
        stage.show();
        ifPlayerStart.setValue(true);
    }

    public void lightMusicPlayer(MouseEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "轻音乐");
    }
    public void lightMusicEnterAni(MouseEvent actionEvent) throws IOException{
        text1.setText("轻音乐");
        buttonEnterAni(buttonLm, text1);
    }

    public void lightMusicExitAni(MouseEvent actionEvent) throws IOException{
        buttonLeaveAni(buttonLm, text1);
    }

    public void whiteNoisePlayer(MouseEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "白噪音");
    }

    public void whiteNoiseEnterAni(MouseEvent actionEvent) throws IOException{
        text2.setText("白噪音");
        buttonEnterAni(buttonWn, text2);
    }

    public void whiteNoiseExitAni(MouseEvent actionEvent) throws IOException{
        buttonLeaveAni(buttonWn, text2);
    }

    public void myListPlayer(MouseEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "");
    }

    public void myListEnterAni(MouseEvent actionEvent) throws IOException{
        text3.setText("自建歌单");
        buttonEnterAni(buttonMl, text3);
    }

    public void myListExitAni(MouseEvent actionEvent) throws IOException{
        buttonLeaveAni(buttonMl, text3);
    }


    public void menuExitEnterAni(MouseEvent actionEvent) throws IOException{
        text4.setText("退出");
        buttonEnterAni(buttonExit, text4);
    }
    public void menuExitExitAni(MouseEvent actionEvent) throws IOException{
        buttonLeaveAni(buttonExit, text4);
    }

    public void menuExit(MouseEvent actionEvent) {
        new FadeOut(buttonExit).play();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出");
        alert.setHeaderText("退出程序");
        alert.setContentText("您确定要退出吗？");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) menuPane.getScene().getWindow();
            stage.close();
        }
        else
            new FadeIn(buttonExit).play();
    }

    public PlayerController getPlayerController(){
        return playerController;
    }

    public void buttonEnterAni(Button button, Text text){
        text.setFont(Font.font("微软雅黑", FontWeight.MEDIUM, FontPosture.REGULAR, 20));
        text.setFill(Color.web("#81537FFF"));
        text.setStroke(Color.WHITE);
        System.out.println("触发");
        text.setLayoutX(button.getLayoutX() + button.getPrefWidth()*0.5-15);
        text.setLayoutY(button.getLayoutY() + button.getPrefHeight()*0.5);
        menuPane.getChildren().add(text);
        new FadeOut(button).play();
        new FadeInUp(text).play();
    }

    public void buttonLeaveAni(Button button, Text text){
        new FadeOutUp(text).play();
        new FadeIn(button).play();
        menuPane.getChildren().remove(text);
    }
}
