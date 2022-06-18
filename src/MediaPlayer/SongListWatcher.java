package MediaPlayer;

import java.io.IOException;
import java.nio.file.*;

public class SongListWatcher extends Thread{
    String srcPath;
    private PlayerController playerController;
    @Override
    public void run() {
        System.out.println("死循环？");
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            System.out.println("1");
            Path path = Paths.get(srcPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            System.out.println("2");
            WatchKey key;
            System.out.println("3");
            while((key = watchService.take()) != null){
                System.out.println("4");
                for(WatchEvent<?> event : key.pollEvents()){
                    // 得到的是删除或增加songName
//                    if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE){
//                        // 如果多了歌曲文件，则读取它
//                        songListData.add(Objects.toString(event.context()));
//                    } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
//                    // 如果少了歌曲文件，就删掉它
//                        songListData.remove(Objects.toString(event.context()));
//                    }
                    playerController.songListReset();
                }
        }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SongListWatcher(String path, PlayerController playerController){
        this.srcPath = path;
        this.playerController = playerController;
    }
}
