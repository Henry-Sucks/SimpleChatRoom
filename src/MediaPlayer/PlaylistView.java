package MediaPlayer;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.IconImage;

import java.io.File;
import java.io.FileFilter;

import static MediaPlayer.MediaPlayerGlobal.defaultSrc;

public class PlaylistView {
    private ListView<Playlist> playlistView;
    private ObservableList<Playlist> playlistData;

    public PlaylistView(ListView<Playlist> playlistView, ObservableList<Playlist> playlistData){
        this.playlistView = playlistView;
        this.playlistData = playlistData;
    }
    public void init(){
        playlistView.getItems().clear();
        playlistData.clear();
        // 设置数据源
        playlistView.setItems(playlistData);
        playlistData.add(new PlaylistView.Playlist("轻音乐", defaultSrc+ '\\' +"轻音乐"));
        playlistData.add(new PlaylistView.Playlist("白噪音", defaultSrc+ '\\' +"白噪音"));
        playlistData.add(new PlaylistView.Playlist("创建歌单", defaultSrc+ '\\' +"创建歌单"));
        // 设置单元格生成器（工厂）
        playlistView.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {
            @Override
            public ListCell<Playlist> call(ListView<Playlist> param) {
                return new PlaylistCell();
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

        // 最标准的写法
        public String src;

        // 当然也可以单独传入icon
        public String iconSrc;
        public Playlist(String name, String src){
            this.name = name;
            this.src = src;
        }

        public Playlist(String name, String src, String iconSrc){
            this.name = name;
            this.src = src;
            this.iconSrc = iconSrc;
        }

        public Image getIcon(){
            // 如果自己设置了图标
            if(iconSrc != null)
                return new Image(iconSrc);
            else{
                // 按照默认情况，在歌单文件夹里搜寻图片
                FileFilter filter = file -> !file.isHidden() && (
                        file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png"));
                File files[] = new File(src).listFiles(filter);
                return new Image(files[0].getAbsolutePath());
            }
        }
    }

}
