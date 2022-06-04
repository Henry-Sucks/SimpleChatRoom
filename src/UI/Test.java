package UI;

import javafx.scene.control.Alert;

//只是用来测试一些方法的 没有实际意义
public class Test {
    public static void main(String[] args){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("没有输入内容");
        alert.setTitle("输入错误");
        alert.setHeaderText("");
        alert.show();
    }
}
