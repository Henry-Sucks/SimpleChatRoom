package MediaPlayer;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SongListView {
    private ListView<String> songListView;
    private ObservableList<String> songListData;

    public SongListView(ListView<String> songListView, ObservableList<String> songListData){
        this.songListView = songListView;
        this.songListData = songListData;
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
