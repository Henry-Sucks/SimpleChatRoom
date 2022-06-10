package tools;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
实现图片的压缩。将用户想要操作的图片转化为合适的大小格式
changeSize方法中sourcPath是待操作图片的绝对路径，objectPath是希望储存的绝对路径
h 为目标图片高度, w为目标图片宽度
 */
public class Image2SuitableSize {
    public static void changeSize(String sourcePath, String objectPath, int h, int w){
        try {
            BufferedImage image = ImageIO.read(new File(sourcePath));
            //设置压缩后的图片大小
            int height2 = h;
            int width2 = w;
            BufferedImage image2 = new BufferedImage(width2, height2,BufferedImage.TYPE_INT_RGB);
            image2.getGraphics().drawImage(image.getScaledInstance(width2, height2, Image.SCALE_SMOOTH),0 ,0,null);
            //获取文件的格式
            String formatName = objectPath.substring(objectPath.lastIndexOf(".") + 1);
            ImageIO.write(image2,formatName, new File(objectPath));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        changeSize("D:\\Downloads\\聊天背景.jpeg","D:\\俄罗斯方块1234\\聊天背景.jpeg",100,600);
    }

}
