package ui;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChoose {
    public static File choose(Stage stage){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("打开目录");
        File dir = chooser.showDialog(stage);
        return dir;
    }
}
