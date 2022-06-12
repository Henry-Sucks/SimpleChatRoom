package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.IconImage;

public class FriendList extends Application
{
    // 创建ListView，指定数据项类型
    ListView<Role> listView = new ListView<Role>();

    // 数据源
    ObservableList<Role> listData = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            // 准备数据
            listData.add(new Role(1, "黄志鸿", true));
            listData.add(new Role(2, "万光曦", false));
            listData.add(new Role(3, "何睿", true));
            listData.add(new Role(4, "贾亦韬", true));
            listData.add(new Role(5, "丁一洋", true));

            // 设置数据源
            listView.setItems( listData );

            // 设置单元格生成器 （工厂）
            listView.setCellFactory(new Callback<ListView<Role>,ListCell<Role>>() {
                @Override
                public ListCell<Role> call(ListView<Role> param)
                {
                    return new MyListCell();
                }
            });

            BorderPane root = new BorderPane();
            root.setCenter(listView);

            Scene scene = new Scene(root, 400, 600);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 负责单元格Cell的显示
    static class MyListCell extends ListCell<Role>
    {
        @Override
        public void updateItem(Role item, boolean empty)
        {
            // FX框架要求必须先调用 super.updateItem()
            super.updateItem(item, empty);

            // 自己的代码
            if (empty || item == null)
            {
                this.setGraphic(null);
            }
            else
            {
                // 自定义显示
                IconImage icon = new IconImage();
                icon.setImage( item.icon);
                icon.setPrefWidth(60); // 设置图标大小
                icon.setPrefHeight(60);
                Label label = new Label(item.name);
                label.setPadding(new Insets(10));


                BackgroundSize backgroundSize = new BackgroundSize(400,600,true,true,true,true);
                BackgroundImage myBI= new BackgroundImage(new Image("Source/Background/登录背景.jpeg",450,300,false,true),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        backgroundSize);
                HBox hbox= new HBox();
                hbox.setBackground(new Background(myBI));
                hbox.getChildren().addAll(icon, label);
                this.setGraphic( hbox );
            }
        }
    }

    // 数据项
    static class Role
    {
        public int id;
        public String name;
        public boolean sex;
        public Image icon;


        public Role(int id, String name, boolean sex)
        {
            this.id = id;
            this.name = name;
            this.sex= sex;

            // 头像
            this.icon = new Image ("Source\\Background\\登录背景.jpeg");
        }

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
