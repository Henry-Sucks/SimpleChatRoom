package MediaPlayer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
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
import javafx.stage.Window;
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
    private Label songNameLabel, volumeLabel, runtimeLabel, playlistLabel;
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


    /** 与miniPlayer绑定 **/
    private Slider miniProcessBar;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(playerPane.getChildren());
        /** 设置桌面 **/
        BackgroundImage backgroundImage= new BackgroundImage(new Image(sysSrc + '\\' + "yoga.jpg",900,675,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        playerPane.setBackground(new Background(backgroundImage));

        /** 一些label的初始化 **/
        songNameLabel.setText("尚无播放中歌曲");
        runtimeLabel.setText("0:00");

        /** 默认音量设置 **/
        volumeBar.setValue(50);
        volumeLabel.setText("当前音量:" + 50);

        /** 初始化歌单界面 **/
        PlaylistView playlistViewController = new PlaylistView(playlistView, playlistData, this);
        playlistViewController.init();
    }


    public void playlistInit(String playlistName){
        if(!playlistName.isEmpty()){
            // 设置标签
            playlistLabel.setText("正在播放：" + playlistName);
            // 初始化歌单
            playListDir = getPlaylistDir(playlistName);
            playlistSongDir = getPlaylistSongDir(playListDir.getAbsolutePath());
            File[] temp;
            temp = playlistSongDir.listFiles();

            // 导入歌单
            SongListView songListViewController = new SongListView(songListView, songListData, this);
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
        else{
            // 设置标签
            playlistLabel.setText("尚未选择歌单");
        }
    }

    // 根据songIndex选取歌曲
    public void setCurSong(){
        var songKeyList = songs.keySet().toArray();

        File curSong = songs.get(songKeyList[songIndex]);
        media = new Media(curSong.toURI().toString());
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        // 初始化播放器
        playerInit(curSong.getName());
    }

    // 根据歌名选取歌曲
    public void setCurSong(String songName){
        File curSong = songs.get(songName);
        media = new Media(curSong.toURI().toString());
        if(mediaPlayer != null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        // 初始化播放器
        playerInit(curSong.getName());
    }

    public void playerInit(String songName){
        System.out.println("开始init:" + songName);
        /** 设置歌名 **/
        songNameLabel.setText(songName);

        /** 设置音量与音量监听 **/
        mediaPlayer.setVolume(volumeBar.getValue());
        volumeBar.valueProperty().addListener(e -> {
            mediaPlayer.setVolume(volumeBar.getValue() * 0.01);
            volumeLabel.setText("当前音量:" + (int) volumeBar.getValue());
        });

        /** 将进度条清空,播放进度清0 **/
        System.out.println(songName + "进度条清零");
        songProgressBar.setValue(0);
        runtimeLabel.setText(seconds2str(mediaPlayer.getCurrentTime().toSeconds())
                + " : " + seconds2str(mediaPlayer.getStopTime().toSeconds()));

        /** 设置播放器对播放结束的监听 **/
        mediaPlayer.currentTimeProperty().addListener(e ->{
            if(mediaPlayer.getCurrentTime().equals(mediaPlayer.getStopTime())) {
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
            if (songProgressBar.isValueChanging() || miniProcessBar.isValueChanging()){
                mediaPlayer.seek(new Duration(
                       mediaPlayer.getStopTime().multiply(songProgressBar.getValue() / 100).toMillis()
                ));
            }
        });

        running = false;

        /** 设置mediaPlayer和miniPlayer的bind **/


        playMedia(null);
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
    /** 与miniPlayer相关 **/


    public StringProperty getSongName(){
        return songNameLabel.textProperty();
    }

    public StringProperty getRunTime(){
        return runtimeLabel.textProperty();
    }

    public DoubleProperty getVolume(){
        return volumeBar.valueProperty();
    }

    public DoubleProperty getProcessBar(){
        return songProgressBar.valueProperty();
    }

    public void setMiniProcessBar(Slider slider){
        this.miniProcessBar = slider;
    }
}
