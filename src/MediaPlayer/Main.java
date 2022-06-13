package MediaPlayer;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
    FXMLLoader loader = new FXMLLoader((getClass().getResource("Menu.fxml")));
    MenuController menuController;
    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = loader.load();
            Scene menu = new Scene(root);
            stage.setScene(menu);
            stage.show();

            menuController = loader.getController();
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
