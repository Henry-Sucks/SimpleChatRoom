package MediaPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

import static MediaPlayer.MediaPlayerGlobal.defaultSrc;
import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class PlayerController implements Initializable {

    /** 基本布局组件 **/
    @FXML
    private AnchorPane playerPane;
    @FXML
    private VBox playlistBox, songNameBox;
    @FXML
    private Label songNameLabel, volumeLabel, runtimeLabel;
    @FXML
    private Button revBtn, nextBtn, playBtn;
    @FXML
    private Slider songProgressBar, volumeBar;

    /** 媒体播放相关 **/
    File playListDir;
    File playlistSongDir;
    LinkedHashMap<String, File> songs = new LinkedHashMap<>();
    final String playlistSrc = defaultSrc;
    int songIndex = -1;
    Media media;
    MediaPlayer mediaPlayer;
    boolean running = false;


    /** 左边栏歌单列表 **/
    @FXML
    ListView<PlaylistView.Playlist> playlistView = new ListView<PlaylistView.Playlist>();
    ObservableList<PlaylistView.Playlist> playlistData = FXCollections.observableArrayList();

    /** 右边栏歌曲列表 **/
    @FXML
    ListView<String> songListView = new ListView<>();
    ObservableList<String> songListData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** 设置桌面 **/
        BackgroundImage backgroundImage= new BackgroundImage(new Image(sysSrc + '\\' + "yoga.jpg",900,675,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        playerPane.setBackground(new Background(backgroundImage));


        /** 默认音量设置 **/
        volumeBar.setValue(50);
        volumeLabel.setText("当前音量:" + 50);

    }


    public void playlistInit(String playlistName){
        System.out.println("Start init");
        // 初始化歌单界面
        PlaylistView playlistViewController = new PlaylistView(playlistView, playlistData);
        playlistViewController.init();

        if(!playlistName.isEmpty()){
            // 初始化歌单
            playListDir = getPlaylistDir(playlistName);
            playlistSongDir = getPlaylistSongDir(playListDir.getAbsolutePath());
            File[] temp;
            temp = playlistSongDir.listFiles();

            // 导入歌单
            SongListView songListViewController = new SongListView(songListView, songListData);
            songListViewController.init();
            songs.clear();
            for(File song : temp){
                songs.put(song.getName(), song);
                songListViewController.addSong(song.getName());
            }



            // 设置现在播放歌曲
            songIndex = 0;
            setCurSong();
        }
    }

    public void setCurSong(){
        var songKeyList = songs.keySet().toArray();

        File curSong = songs.get(songKeyList[songIndex]);
        media = new Media(curSong.toURI().toString());
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        // 初始化播放器
        playerInit();

        songNameLabel.setText(curSong.getName());
    }

    public void playerInit(){
        /** 设置音量与音量监听 **/
        mediaPlayer.setVolume(volumeBar.getValue());
        volumeBar.valueProperty().addListener(e -> {
            mediaPlayer.setVolume(volumeBar.getValue() * 0.01);
            volumeLabel.setText("当前音量:" + (int) volumeBar.getValue());
        });

        // 设置slider尺寸

        /** 设置播放器对播放结束的监听 **/
        mediaPlayer.currentTimeProperty().addListener(e ->{
            if(mediaPlayer.getCurrentTime().equals(mediaPlayer.getStopTime())) {
                // 如果有在播放的歌曲
                if (running) {
                    // 设置文本框的颜色，播放完成状态
                }

                // 自动播放下一首歌曲
                nextMedia(null);
            }

                runtimeLabel.setText(seconds2str(mediaPlayer.getCurrentTime().toSeconds())
                        + " : " + seconds2str(mediaPlayer.getStopTime().toSeconds()));
                songProgressBar.setValue(mediaPlayer.getCurrentTime().toMillis()//到毫秒
                        / mediaPlayer.getStopTime().toMillis() * 100);

        });

        /** 设置进度条与进度条监听 **/
        songProgressBar.valueProperty().addListener(e->{
            if (songProgressBar.isValueChanging()){
                mediaPlayer.seek(new Duration(
                       mediaPlayer.getStopTime().multiply(songProgressBar.getValue() / 100).toMillis()
                ));
            }
        });
    }


    public void playMedia(ActionEvent actionEvent) {
        if(!running) {
            mediaPlayer.play();
            running = true;
//            switchState();
        }
        else{
            mediaPlayer.pause();
            running = false;
//            switchState();
        }
    }

    public void prevMedia(ActionEvent actionEvent) {
        if(songIndex > 0){
            songIndex--;
        }
        else{
            songIndex = songs.size()-1;
        }

        mediaPlayer.stop();
        running = false;

        setCurSong();
        playMedia(null);
    }

    public void nextMedia(ActionEvent actionEvent) {
        if(songIndex < songs.size() - 1){
            songIndex++;
        }
        else{
            songIndex = 0;
        }

        mediaPlayer.stop();
        running = false;

        setCurSong();
        playMedia(null);
    }



    /** 工具类函数 **/
    public File getPlaylistDir(String playlistName){
        return new File(playlistSrc + '\\' + playlistName);
    }

    public File getPlaylistSongDir(String path){
        return new File(path+"\\songs");
    }


    static String seconds2str(Double seconds) {
        int count = seconds.intValue();
        count = count % 3600;
        int Minutes = count / 60;//获取当前分数
        count = count % 60;//获取当前秒数
        if(count < 10)
            return Minutes + ":" + "0" + count;
        else
            return Minutes + ":" + count;
    }
}
