//package UI;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
////只是用来测试一些方法的 没有实际意义
//public class Test extends Application {
//
//    @Override
//    public void start (Stage primaryStage){
//        try{
//            TextFlow test = new TextFlow();
//            test.getChildren().add(new Text(null));
//            test.getChildren().add(new Text("123"));
//            Scene scene = new Scene(test, 400, 300);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
//
//class t{
//    public static void main(String[] args){
//        String regex = "%\\d\\d";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher("123%12dasda");
//        matcher.find();
//        System.out.println(matcher.start());
//        System.out.println(matcher.end());
//    }
//}