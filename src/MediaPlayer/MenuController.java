package MediaPlayer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
    private FXMLLoader loader;
    private PlayerController playerController;

    BooleanProperty ifPlayerStart = new SimpleBooleanProperty(false);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Menu initialized");
        loader = new FXMLLoader(getClass().getResource("Player.fxml"));

        // 设置背景
        BackgroundImage backgroundImage= new BackgroundImage(new Image(sysSrc + '\\' + "yoga.jpg",900,675,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        menuPane.setBackground(new Background(backgroundImage));


    }


    // player.fxml必须编译通过才能正常显示
    private void switchToPlayer(ActionEvent event, String playList) throws IOException{
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

    public void lightMusicPlayer(ActionEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "轻音乐");
        System.out.println("轻音乐");

    }

    public void whiteNoisePlayer(ActionEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "白噪音");
    }

    public void myListPlayer(ActionEvent actionEvent) throws IOException{
        switchToPlayer(actionEvent, "");
    }

    public void menuExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出");
        alert.setHeaderText("退出程序");
        alert.setContentText("您确定要退出吗？");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) menuPane.getScene().getWindow();
            stage.close();
        }
    }

    public PlayerController getPlayerController(){
        return playerController;
    }
}
