package MediaPlayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));


            Scene menu = new Scene(root);
            stage.setScene(menu);
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
}
