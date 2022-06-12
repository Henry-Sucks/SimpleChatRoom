package tools;
//设置图片的展示格式 铺满、居中
import javafx.geometry.Rectangle2D;

public class ImagePut
{
    // 拉伸图片填满
    public static Rectangle2D fitXY(Rectangle2D source, Rectangle2D target)
    {
        return target;
    }

    public static Rectangle2D fitCenter(Rectangle2D source, Rectangle2D target)
    {
        double target_w = target.getWidth();
        double target_h = target.getHeight();

        double image_w = source.getWidth();
        double image_h = source.getHeight();

        if(image_w <= 0) image_w = 1;
        if(image_h <= 0) image_h = 1;

        double scaled_w = target_w;
        double scaled_h = image_h * target_w / image_w;
        if(scaled_h > target_h)
        {
            scaled_h = target_h;
            scaled_w = target_h * image_w / image_h;
        }

        // 坐在中心
        double x = (target_w - scaled_w)/2;
        double y = (target_h - scaled_h)/2;
        x += target.getMinX();
        y += target.getMinY();
        return new Rectangle2D(x, y, scaled_w, scaled_h);
    }

    // 居中显示 (或图片超出则按比例缩小, 否则原图显示)
    public static Rectangle2D centerInside(Rectangle2D source, Rectangle2D target)
    {
        if( source.getWidth() > target.getWidth() || source.getHeight() > target.getHeight())
            return fitCenter(source, target);

        double x = ( target.getWidth() - source.getWidth() ) / 2;
        double y = ( target.getHeight() - source.getHeight() ) / 2;
        x += target.getMinX();
        y += target.getMinY();

        return new Rectangle2D(x, y, source.getWidth(), source.getHeight());
    }

    // 居中显示 (或图片超出则截掉超出的部分, 否则原图显示)
    public static Rectangle2D centerCrop(Rectangle2D source, Rectangle2D target)
    {
        return null;
    }

}
