package MediaPlayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.IconImage;

import java.io.File;
import java.io.FileFilter;
import java.util.Optional;

import static MediaPlayer.MediaPlayerGlobal.*;

public class PlaylistView {
    private ListView<Playlist> playlistView;
    private ObservableList<Playlist> playlistData;

    private PlayerController playerController;
    public PlaylistView(ListView<Playlist> playlistView, ObservableList<Playlist> playlistData, PlayerController playerController){
        this.playlistView = playlistView;
        this.playlistData = playlistData;
        this.playerController = playerController;
    }
    public void init(){
        playlistView.getItems().clear();
        playlistData.clear();
        // 设置数据源
        playlistView.setItems(playlistData);
        playlistData.add(new PlaylistView.Playlist("轻音乐", sysSrc + '\\' +"harp.jpeg"));
        playlistData.add(new PlaylistView.Playlist("白噪音", sysSrc + '\\' +"heavy_rain.jpg"));
        playlistData.add(new PlaylistView.Playlist("创建歌单", sysSrc + '\\' +"plus-icon-black-2.png"));
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
                    File playlistSrc = new File(defaultSrc+'\\'+newName+ '\\' + "songs");
                    if(!playlistSrc.exists()){
                        playlistSrc.mkdirs();
                    }

                    // 弹出提示框
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("新歌单已创建完成！");
                    a.setContentText("歌单目录位于:\n"+ defaultSrc+'\\'+newName+ '\\' + "songs" + "\n请将本地音乐文件放置至此");
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
                    playlistData.add(new Playlist("创建歌单", sysSrc + '\\' +"plus-icon-black-2.png"));
                    // 有bug，更新列表后附表会乱移动！
                }
            }
        });
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

                HBox hbox = new HBox();
                hbox.getChildren().addAll(icon, label);
                this.setGraphic(hbox);
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
                return new Image(iconSrc);
            }
        }
    }


}
