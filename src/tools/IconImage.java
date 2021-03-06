package tools;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class IconImage extends Pane{
    Canvas canvas = new Canvas();
    Image image;

    public IconImage(){
        getChildren().add(canvas);
    }

    public void setImage(Image image){
        this.image = image;
        update();
    }

    @Override
    protected void layoutChildren(){
        double w = getWidth();
        double h = getHeight();

        canvas.setWidth(w);
        canvas.setHeight(h);
        canvas.resizeRelocate(0,0,w,h);
        update();
    }

    public void update(){
        if (image == null){
            return;
        }
        //获取窗口的大小
        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0){
            return;
        }
        //获取图片的大小
        double imgh = image.getHeight();
        double imgw = image.getWidth();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,w,h);
        //确定图像的半径
        double radius = w/2;
        if (radius > h){
            radius = h/2;
        }
        //保存绘制参数
        gc.save();
        gc.beginPath();
        gc.arc(w/2, h/2, radius, radius,0,360);
        //设定剪辑区域
        gc.clip();
        //绘制图片
        Rectangle2D source = new Rectangle2D(0,0,imgh, imgw);
        Rectangle2D target = new Rectangle2D(0,0, w, h);
        Rectangle2D fitRect = ImagePut.centerInside(source, target);
        drawImage(gc, image, fitRect);

        // 恢复绘制参数
        gc.restore();
    }

    // 画图
    private void drawImage(GraphicsContext gc, Image image, Rectangle2D r)
    {
        gc.drawImage(image,
                0, 0, image.getWidth(), image.getHeight(),
                r.getMinX(), r.getMinY(), r.getWidth(),r.getHeight()
        );
    }
}





