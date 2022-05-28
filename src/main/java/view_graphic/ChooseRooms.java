package view_graphic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class ChooseRooms {
    public static ArrayList<ArrayList<User>> rooms=new ArrayList<>();
    @FXML
    private VBox vbox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        Button button0=new Button("Create New Room");
        button0.getStyleClass().add("secondary-btn");
        vbox.getChildren().add(button0);
        button0.setOnMouseClicked(mouseEvent -> {
            App.changeMenu("CreateRoomPage");
        });
        for(int i=0;i<rooms.size();i++){
            Button button=new Button("Room Number "+(i+1));
            button.getStyleClass().add("secondary-btn");
            int finalI = i;
            button.setOnMouseClicked(mouseEvent -> {
                ChatPage.setChatType("Room");
                ChatPage.setChatName("Room:" + finalI);
                App.changeMenu("ChatPage");
            });
            vbox.getChildren().add(button);
        }
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("ChatMenuPage");
    }
}
