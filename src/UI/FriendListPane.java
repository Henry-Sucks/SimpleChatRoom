package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.IconImage;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class FriendListPane{
    ObservableList<Friend> listData;
    ListView<Friend> listView;

    public FriendListPane(ObservableList<Friend> listData, ListView<Friend> listView){
        System.out.println(1);
        this.listData = listData;
        this.listView = listView;
    }

    public void init(){
        System.out.println(2);
        listData.clear();
        listView.getItems().clear();
        // 设置数据源
        // 如何判断是否是房主(登录用户)?
        listData.add(new Friend(null, "何睿", "大家好啊，我是电棍", -1));
        listData.add(new Friend(null, "万光曦", null, -1));

        listView.setItems(listData);
        // 设置单元格生成器
        listView.setCellFactory(new Callback<ListView<Friend>, ListCell<Friend>>() {
            @Override
            public ListCell<Friend> call(ListView<Friend> friendListView) {
                return new FriendListCell();
            }
        });


    }
    static class FriendListCell extends ListCell<Friend>{
        @Override
        public void updateItem(Friend item, boolean empty){
            super.updateItem(item,empty);
            if (empty || item == null){
                this.setGraphic(null);
            }
            else
            {
                // 设置图标
                // 如果不为空
                IconImage icon = new IconImage();
                if(item.icon != null) {
                    icon.setImage(new Image(item.icon));
                    icon.setPrefSize(45, 45);
                }
                else{
                    icon.setImage(new Image(sysSrc + '\\' + "default-icon.png"));
                    icon.setPrefSize(45, 45);
                }

                // 设置性别图标
                IconImage sexIcon = new IconImage();
                // 男性 1
                if(item.sex == 1)
                    sexIcon.setImage(new Image(sysSrc + '\\' + "male.png"));
                else if (item.sex == -1)
                // 女性 -1
                    sexIcon.setImage(new Image(sysSrc + '\\' + "female.png"));
                // 保密 0 (默认值）
                else
                    sexIcon.setImage(new Image(sysSrc + '\\' + "question-mark.png"));

                sexIcon.setPrefSize(15, 15);



                // 设置姓名标签与签名标签
                Label nameLabel = new Label(item.name);
                Label signLabel = new Label();

                // 设置字体
                Font font1 = Font.font("微软雅黑", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
                Font font2 = Font.font("黑体", FontWeight.LIGHT, FontPosture.ITALIC, 12);
                nameLabel.setFont(font1);
                signLabel.setFont(font2);

//                nameLabel.setTextFill();
                signLabel.setTextFill(Color.web("#5F6262FF"));


                // 如果签名是否为空?
                if(item.sign != null)
                    if(item.sign.length() > 15)
                        signLabel.setText(item.sign.substring(0, 16) + "...");
                    else
                        signLabel.setText(item.sign);
                else
                    signLabel.setText("还未设置个性签名！");

                HBox hBox1 = new HBox(5);
                hBox1.getChildren().addAll(icon, nameLabel, sexIcon);
                hBox1.setAlignment(Pos.CENTER_LEFT);
                HBox hBox2 = new HBox();
                hBox2.getChildren().addAll(signLabel);


                // 运用VBox来装
                VBox vBox = new VBox(10);
//                GridPane gridPane = new GridPane();
//                GridPane.setRowIndex(hBox1, 0);
//                GridPane.setRowIndex(hBox2, 1);
                vBox.getChildren().addAll(hBox1, hBox2);
                this.setGraphic(vBox);
            }
        }
    }

    public Node getListView(){
        init();
        return listView;
    }

    static class Friend{
        // 图片地址
        String icon;
        String name;
        // 性别
        int sex;
        // 签名
        String sign;

        public Friend(String icon, String name, String sign, int sex){
            this.icon = icon;
            this.name = name;
            this.sign = sign;
            this.sex = sex;
        }
    }
}


