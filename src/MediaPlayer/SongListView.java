package MediaPlayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class SongListView {
    private ListView<String> songListView;
    private ObservableList<String> songListData;

    private PlayerController playerController;

    public SongListView(ListView<String> songListView, ObservableList<String> songListData, PlayerController playerController){
        this.songListView = songListView;
        this.songListData = songListData;
        this.playerController = playerController;
    }

    public void init(){
        songListView.getItems().clear();
        songListData.clear();
        songListView.setItems(songListData);

        // 设置单元格生成器（工厂）
        songListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new SongListCell();
            }
        });

        // 添加切换歌曲监听器(是click监听器！)

        songListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String curSongName = songListView.getSelectionModel().getSelectedItem();
                playerController.setCurSong(curSongName);
            }
        });
    }

    static class SongListCell extends ListCell<String>{
        @Override
        public void updateItem(String item, boolean empty){
            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText(null);
            }
            else{
                this.setText(item);
            }
        }
    }

    public void addSong(String songName){
        songListData.add(songName);
    }
}
