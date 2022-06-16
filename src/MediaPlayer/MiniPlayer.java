package MediaPlayer;

import animatefx.animation.*;
import com.sun.media.jfxmedia.MediaPlayer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class MiniPlayer{
    private AnchorPane miniPlayer = new AnchorPane();

    /** 播放器目录设置 **/
    private ToggleButton start = new ToggleButton();
    private Label menuLabel = new Label("此处还没有歌曲播放...");

    /** 播放器布局设置 **/
    private Label songNameLabel = new Label("目前未播放任何歌曲"),
            runtimeLabel = new Label("0:00"), 
            volumeLabel = new Label("50");
    ;
    private Slider processBar = new Slider(),
            volumeBar = new Slider();
    private HBox btnBox = new HBox();
    private Button prev = new Button("上一首"),
            play = new Button("播放"),
            next = new Button("下一首");

    static int buttonSize1 = 42;
    static int buttonSize2 =35;

    /** 与mediaPlayer的交互与监听 **/
    PlayerController playerController;
    BooleanProperty ifPlayerStart;
    Main main;

    public void miniPlayerMenu(){
        miniPlayer.setBackground(null);
        miniPlayer.setPrefSize(400, 100);
        menuLabel.setPrefSize(400, 14);
        menuLabel.setLayoutY(6);
        menuLabel.setAlignment(Pos.CENTER);

        Label hintLabel = new Label("点击开启你的专注空间！");
        hintLabel.setPrefSize(400, 14);
        hintLabel.setAlignment(Pos.CENTER);
        hintLabel.setLayoutY(30);

        Font font1 = Font.font("黑体", FontWeight.BOLD, FontPosture.REGULAR, 15);
        Font font2 = Font.font("黑体", FontWeight.LIGHT, FontPosture.REGULAR, 12);
        menuLabel.setFont(font1);
        hintLabel.setFont(font2);

        start.setLayoutX(160);
        start.setLayoutY(50);
        Image buttonBI = new Image(sysSrc + "\\enter.png", 80, 80, true, false);
        start.setBackground(null);
        start.setGraphic(new ImageView(buttonBI));

        miniPlayer.getChildren().addAll(menuLabel, start, hintLabel);
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main = new Main();
                Stage newStage = new Stage();
                try {
                    main.start(newStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                miniPlayer.getChildren().removeAll(menuLabel, start, hintLabel);
                miniPlayerInit();

                /** 开启对Player开启状态的监听 **/
                ifPlayerStart = main.getIfPlayerStart();
                ifPlayerStart.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        setMiniPlayer();
                    }
                });
            }
        });

    }

    public void miniPlayerInit() {
        songNameLabel.setPrefSize(400, 14);
        miniPlayer.getChildren().addAll(songNameLabel, runtimeLabel, volumeLabel);
        songNameLabel.setLayoutY(6);
        runtimeLabel.setLayoutY(39);
        volumeLabel.setLayoutX(333);
        volumeLabel.setLayoutY(61);

        processBar.setPrefSize(400, 14);
        volumeBar.setOrientation(Orientation.VERTICAL);
        volumeBar.setPrefHeight(62);
        miniPlayer.getChildren().addAll(processBar, volumeBar);
        processBar.setLayoutY(21);
        volumeBar.setLayoutX(371);
        volumeBar.setLayoutY(40);

        Image image = new Image(sysSrc + '\\' + "pause1.png", buttonSize1, buttonSize1, true, false);
        play.setPrefSize(buttonSize1, buttonSize1);
        play.setText("");
        play.setBackground(null);
        play.setGraphic(new ImageView(image));

        image = new Image(sysSrc + '\\' + "rewind-button.png", buttonSize2, buttonSize2, true, false);
        prev.setPrefSize(buttonSize2, buttonSize2);
        prev.setGraphic(new ImageView(image));
        prev.setText("");
        prev.setBackground(null);

        image = new Image(sysSrc + '\\' + "forward-button.png", buttonSize2, buttonSize2, true, false);
        next.setPrefSize(buttonSize2, buttonSize2);
        next.setGraphic(new ImageView(image));
        next.setText("");
        next.setBackground(null);

        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPrefSize(190, 62);
        btnBox.getChildren().addAll(prev, play, next);
        miniPlayer.getChildren().add(btnBox);
        btnBox.setLayoutX(105);
        btnBox.setLayoutY(38);

        /** 默认音量设置 **/
        volumeBar.setValue(50);
        volumeLabel.textProperty().bind(Bindings.format("%.0f", volumeBar.valueProperty()));
    }

    public AnchorPane getMiniPlayer(){
        miniPlayerMenu();
        return miniPlayer;
    }

    public void setMiniPlayer(){
        playerController = main.getPlayerController();

        songNameLabel.textProperty().bind(playerController.getSongName());
        runtimeLabel.textProperty().bind(playerController.getRunTime());
        volumeBar.valueProperty().bindBidirectional(playerController.getVolume());
        processBar.valueProperty().bindBidirectional(playerController.getProcessBar());

        playerController.setMiniProcessBar(processBar);
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new FadeIn(play).play();
                playerController.playMedia();
                switchState(playerController.running);
            }
        });

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new ZoomIn(next).play();
                playerController.nextMedia();
            }
        });

        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new ZoomIn(prev).play();
                playerController.prevMedia();
            }
        });
    }

    public void switchState(boolean running){
        if(running) {
            Image image = new Image(sysSrc + '\\' + "pause1.png", buttonSize1, buttonSize1, true, false);
            play.setGraphic(new ImageView(image));
        }
        else{
            Image image = new Image(sysSrc + '\\' + "play-button1.png", buttonSize1, buttonSize1, true, false);
            play.setGraphic(new ImageView(image));
        }
    }
}
