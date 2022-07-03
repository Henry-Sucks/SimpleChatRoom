package ui;

import Client.ClientReadAndPrint;
import Global.UserProtocol;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Callback;
import tools.IconImage;

import static MediaPlayer.MediaPlayerGlobal.sysSrc;

public class FriendListPane{
    ObservableList<Friend> listData;
    ListView<Friend> listView;

    // 记录初始化时的顺序
    private TextFlow textFlow;

    private String userName;

    private ClientReadAndPrint.ChatViewDirectSend chatViewDirectSend;

    public FriendListPane(ObservableList<Friend> listData, ListView<Friend> listView){
        this.listData = listData;
        this.listView = listView;
    }



    public void init(String loginSet){
        listData.clear();
        listView.getItems().clear();
        listView.setPrefWidth(400);
        // 设置数据源
//        listData.add(new Friend(null, userName, null, 0));

        // 设置目前在房间里的所有名单
        int num = Integer.parseInt(loginSet.split(UserProtocol.SPLIT_SIGN)[0]);
        for(int i = 1; i <= num; i++){
            String temp = loginSet.split(UserProtocol.SPLIT_SIGN)[i];
            listData.add(new Friend(null, temp, null, 0));
        }

        listView.setItems(listData);
        // 设置单元格生成器
        listView.setCellFactory(new Callback<ListView<Friend>, ListCell<Friend>>() {
            @Override
            public ListCell<Friend> call(ListView<Friend> friendListView) {
                return new FriendListCell();
            }
        });





    }
    class FriendListCell extends ListCell<Friend>{
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
                Label nameLabel = new Label();
                if(userName.equals(item.name))
                    nameLabel.setText(item.name + " （您）");
                else
                    nameLabel = new Label(item.name);
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

    public void setUserName(String userName){
        this.userName = userName;
    }
    // 私聊监听器
    public void setChatViewDirectSend(ClientReadAndPrint.ChatViewDirectSend chatViewDirectSend){
        this.chatViewDirectSend = chatViewDirectSend;
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                    String name = listView.getSelectionModel().getSelectedItem().getName();
                    Text newText = new Text();
                    newText.setFill(Color.web("#999999FF"));
                    if (!name.equals(userName)) {
                        chatViewDirectSend.DirectSend("%&%~" + (listView.getSelectionModel().getSelectedIndex() + 1));
                        newText.setText('\n' + "                                      开启私聊模式-> " + name);
                        textFlow.getChildren().add(newText);
                    } else {
                        chatViewDirectSend.DirectSend("%&%~" + 7);
                        newText.setText('\n' + "                                      关闭私聊模式");
                        textFlow.getChildren().add(newText);
                    }
//                else if(mouseEvent.isSecondaryButtonDown()){
//                    UserMenu userMenu = new UserMenu();
//                    try {
//                        userMenu.start(new Stage());
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            }
        });
    }

    public void setTextFlow(TextFlow textFlow){
        this.textFlow = textFlow;
    }

    public static class Friend{
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

        public String getName(){
            return name;
        }
    }
}


