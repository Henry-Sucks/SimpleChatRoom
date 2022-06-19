package ClockStage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class ClockStage extends Application {
    int[] time = new int[3];
    int[] timeCo = time.clone();
    private GridPane pane = new GridPane();
    private TextField[] tf = new TextField[3];
    private Label[] labels = new Label[3];
    private Button reset = new Button();
    private Button stop = new Button();
    private Button end = new Button();
    private Button begin = new Button();
    private boolean ifRun;
    private boolean ifStop;
    private Timeline timeline;
    private int signLen = 50;

    @Override
    public void start(Stage stage) throws Exception {
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(20);
        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 50);
        Font font1 = Font.font("黑体", FontWeight.BOLD, FontPosture.REGULAR, 15);

        ifRun = false;
        setBG(begin, sysSrc + '\\' +"start.png");
        setBG(reset, sysSrc + '\\' +"reset.png");
        setBG(end, sysSrc + '\\' +"end.png");
        setBG(stop, sysSrc + '\\' +"pause2.png");
        pane.add(reset, 0, 2);
        pane.add(begin, 2, 2);
        pane.add(end, 4, 2);
        for (int i = 0;i < 3;i++){
            if (tf[i] == null) {
                tf[i] = new TextField();
                tf[i].setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
                tf[i].setMinSize(50, 50);
                tf[i].setMaxSize(60, 60);
            }
            pane.add(tf[i], 2*i, 0);
            if (labels[i] == null) {
                labels[i] = new Label();
                labels[i].setFont(font);
            }
        }
        Label space = new Label(":");
        Label space2 = new Label(":");
        space.setFont(font);
        space2.setFont(font);
        pane.add(space, 1, 0);
        pane.add(space2, 3, 0);

        BackgroundImage bg= new BackgroundImage(new Image(sysSrc + '\\' +"ClockBG.png",450,300,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(bg));

        begin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!ifRun) startTime();
                for (int i = 0;i < 3;i++){
                    labels[i].setAlignment(Pos.CENTER);
                }
                ifStop = false;
            }
        });
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetTime();
                ifStop = false;
            }
        });
        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stopTime();
            }
        });
        end.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(int i = 0;i < 3;i++){
                    time[i] = 0;
                    timeCo[i] = 0;
                }
                if (ifRun) endTime();
            }
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> timelabel()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene=new Scene(pane,450,300);
        stage.setTitle("简单计时器！");
        stage.getIcons().add(new Image(sysSrc + '\\' + "icon.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setBG(Button btn, String name){
        BackgroundImage Pi= new BackgroundImage(new Image(name,signLen,signLen,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        btn.setBackground(new Background(Pi));
        btn.setMinSize(50, 50);
    }

    public void stopTime(){
        ifStop = !ifStop;
        if (ifStop) setBG(stop, sysSrc + '\\' +"start.png");
        else setBG(stop, sysSrc + '\\' +"pause2.png");
    }

    public void startTime(){
        for (int i = 0;i < 3;i++){
            try{
                time[i] = timeCo[i] = Integer.parseInt(tf[i].getText());
            }catch (Exception e){
                time[i] = timeCo[i] = 0;
            }
            if (time[i] > (i==0?24:60)){
                for (int j = 0;j < 3;j++){
                    time[j] = timeCo[j] = 0;
                    tf[j].setText("");
                }
                return;
            }
        }
        for (int i = 0;i < 3;i++){
            pane.getChildren().remove(tf[i]);
            labels[i].setText(get2char(timeCo[i], i==0?24:60));
            pane.add(labels[i], 2*i, 0);
        }
        pane.getChildren().remove(begin);
        pane.add(stop, 2, 2);
        ifRun = true;
    }

    public void timelabel() {
        if (!ifRun);
        else if (allZero(timeCo)) endTime();
        else{
            if(!ifStop) timePass();
            for (int i = 0;i < 3;i++) {
                labels[i].setText(get2char(timeCo[i], i == 0 ? 24 : 60));
            }
        }
    }

    public void timePass(){
        if (timeCo[2] > 0) timeCo[2]--;
        else if (timeCo[1] > 0) {
            timeCo[1]--;
            timeCo[2] = 59;
        }
        else if (timeCo[0] > 0){
            timeCo[0]--;
            timeCo[2] = 59;
            timeCo[1] = 59;
        }
        else endTime();
    }

    public void endTime(){
        for (int i = 0;i < 3;i++){
            time[i] = timeCo[i] = 0;
            labels[i].setText("00");
            tf[i].setText("");
            pane.getChildren().remove(labels[i]);
            pane.add(tf[i], 2*i,0);
        }
        pane.getChildren().remove(stop);
        pane.add(begin, 2, 2);
        ifRun = false;
    }

    public void resetTime(){
        if (!ifRun){
            for(int i = 0;i < 3;i++){
                tf[i].setText("");
            }
        }else {
            for (int i = 0; i < 3; i++) {
                timeCo[i] = time[i];
                labels[i].setText(get2char(timeCo[i], i == 0 ? 24 : 60));
            }
        }
    }

    public boolean allZero(int[] ints){
        for (int i : ints){
            if (i > 0) return false;
        }
        return true;
    }

    public String get2char(int i, int max){
        String res = null;
        if (i > max || i <= 0) res = "00";
        else{
            res = String.format("%d", i);
            if (i < 10) res = "0"+res;
        }
        return res;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
