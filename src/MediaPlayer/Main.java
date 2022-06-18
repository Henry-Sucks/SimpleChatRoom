package MediaPlayer;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class Main extends Application {
    FXMLLoader loader = new FXMLLoader((getClass().getResource("Menu.fxml")));
    MenuController menuController;

    private String userName;

    public Main(){}
    public Main(String userName){
        this.userName =userName;
    }
    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = loader.load();
            Scene menu = new Scene(root);
            stage.setScene(menu);

            menuController = loader.getController();
            menuController.setUserName(userName);
            // 增加icon
            Image image = new Image(sysSrc + "\\lotus.png");
            stage.getIcons().add(image);
            stage.setTitle("欢迎来到专注空间！");
            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                menuExit(stage);

            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void menuExit(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出");
        alert.setHeaderText("退出程序");
        alert.setContentText("您确定要退出吗？");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }

    public PlayerController getPlayerController(){
        return menuController.getPlayerController();
    }

    public BooleanProperty getIfPlayerStart(){
        return menuController.ifPlayerStart;
    }
}
