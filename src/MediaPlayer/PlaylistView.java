package MediaPlayer;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import tools.IconImage;

import java.io.File;
import java.util.Optional;

import static MediaPlayer.MediaPlayerGlobal.defaultSrc;
import static MediaPlayer.MediaPlayerGlobal.sysSrc;


public class PlaylistView {
    private ListView<Playlist> playlistView;
    private ObservableList<Playlist> playlistData;

    private PlayerController playerController;
    private ObservableList<String> songListData;

    private String userName;

    /** 全局变量 **/
    static Font font1 = Font.font("微软雅黑", FontWeight.MEDIUM, FontPosture.REGULAR, 20);


    public PlaylistView(ListView<Playlist> playlistView, ObservableList<Playlist> playlistData, PlayerController playerController, ObservableList<String> songListData){
        this.playlistView = playlistView;
        this.playlistData = playlistData;
        this.playerController = playerController;
        this.songListData = songListData;
    }
    public void init(){
        playlistView.getItems().clear();
        playlistData.clear();
        // 设置数据源
        playlistView.setItems(playlistData);
        playlistData.add(new PlaylistView.Playlist("轻音乐", sysSrc + '\\' +"harp.jpeg"));
        setWatchService(defaultSrc + "\\轻音乐");
        playlistData.add(new PlaylistView.Playlist("白噪音", sysSrc + '\\' +"heavy_rain.jpg"));
        setWatchService(defaultSrc + "\\白噪音");
        // 进入时自动扫描新歌单
        // 意义不大！需要知道icon位置
        // 不如添加配置文件


        playlistData.add(new PlaylistView.Playlist("创建歌单", sysSrc + '\\' +"plus.png"));
        // 设置单元格生成器（工厂）
        playlistView.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {
            @Override
            public ListCell<Playlist> call(ListView<Playlist> param) {
                return new PlaylistCell();
            }
        });

        // 增加切换歌单监听器（注意，是click！）
        playlistView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Playlist curList = playlistView.getSelectionModel().getSelectedItem();
                // 已经存在歌单
                if(curList == null){
                    return;
                }
                if(curList.name != "创建歌单") {
                    playerController.playlistInit(curList.name);
                    return;
                }
                // 新建歌单
                else{
                    // 弹出输入框取新名字
                    String newName;
                    TextInputDialog td = new TextInputDialog("名字叫...");
                    td.setHeaderText("为新歌单取个名字吧！");
                    Optional<String> res = td.showAndWait();
                    if(res.isPresent()){
                        newName = td.getEditor().getText();
                    }
                    else{
                        return;
                    }
                    // 创建新文件夹
                    File playlistSrc = new File(defaultSrc+'\\'+ userName + '\\' + newName+ '\\');
                    if(!playlistSrc.exists()){
                        playlistSrc.mkdirs();
                    }

                    // 对文件夹进行监听：WatchService
//                    setWatchService(playlistSrc.getAbsolutePath());


                    // 弹出提示框
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("新歌单已创建完成！");
                    a.setContentText("歌单目录位于:\n"+ defaultSrc+'\\'+ userName + '\\' + newName + "\n请将本地音乐文件放置至此");
                    a.showAndWait();

                    // 设置歌单封面
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("选择专辑封面");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("图片文件", ".jpg", ".jpeg", ".png");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File selectedFile = fileChooser.showOpenDialog(null);

                    String iconSrc;
                    if(selectedFile != null) {
                        iconSrc = selectedFile.getAbsolutePath();
                        a.setAlertType(Alert.AlertType.INFORMATION);
                        a.setTitle("您已选择歌单封面！");
                        a.setContentText("歌单封面路径位于\n" + iconSrc);
                        a.showAndWait();
                    }
                    else {
                        iconSrc = null;
                        a.setAlertType(Alert.AlertType.INFORMATION);
                        a.setTitle("您未选择歌单封面！");
                        a.setContentText("歌单封面将使用默认封面");
                        a.showAndWait();
                    }

                    // 新建表项
                    int index = playlistData.size()-1;
                    playlistData.remove(index);
                    playlistData.add(new Playlist(newName, iconSrc));
                    playlistData.add(new Playlist("创建歌单", sysSrc + '\\' +"plus.png"));
                    // 有bug，更新列表后附表会乱移动！
                }
            }
        });
    }

    public void setWatchService(String playlistSrc){
        SongListWatcher newWatcher = new SongListWatcher(playlistSrc, playerController);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                newWatcher.start();
            }
        });
    }

    public void setUserName(String userName){
        this.userName = userName;
    }


    // 负责单元格Cell的显示
    static class PlaylistCell extends ListCell<Playlist>{
        @Override
        public void updateItem(Playlist item, boolean empty){
            super.updateItem(item, empty);

            if (empty || item == null) {
                this.setGraphic(null);
            }
            else {
                // 自定义显示
                IconImage icon = new IconImage();
                icon.setImage(item.getIcon());
                icon.setPrefHeight(60);
                icon.setPrefWidth(60);

                Label label = new Label(item.name);
                label.setPadding(new Insets(10));
                label.setFont(font1);

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(icon, label);
                this.setGraphic(hbox);
                new FadeIn(hbox).play();
            }
        }


    }


    // 管理Playlist的数据
    static class Playlist{
        public String name;
        // 头像来源
        public String iconSrc;

        public Playlist(String name, String iconSrc){
            this.name = name;
            this.iconSrc = iconSrc;
        }


        public Image getIcon(){
            // 默认图案
            if(iconSrc == null)
                return new Image(sysSrc + "\\" + "default.jpg");
            // 自选图案
            else{
                System.out.println(iconSrc);
                return new Image(iconSrc);
            }
        }
    }


}
