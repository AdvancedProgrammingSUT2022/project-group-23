package view_graphic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class CreateRoomPage {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;
    @FXML
    private Button selectButton;
    public ArrayList<User> usersChatting = new ArrayList<>();
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        ArrayList<String > usersUsernames=new ArrayList<>();
        for (User user : User.getUsers()) {
            if(!user.equals(User.getUserLogged())){
                usersUsernames.add(user.getUsername());
            }
        }
        ComboBox<String> comboBox=new ComboBox<>(FXCollections.observableArrayList(usersUsernames));
        comboBox.setMinWidth(100);
        vbox.getChildren().add(comboBox);
        selectButton.setOnMouseClicked(mouseEvent -> {
            if(comboBox.getValue() != null && !usersChatting.contains(User.getUserByUsername(comboBox.getValue()))) {
                Text text = new Text(comboBox.getValue() + " added");
                text.getStyleClass().add("text2");
                text.setFill(Color.rgb(70, 210, 25));
                vbox.getChildren().add(text);
                usersChatting.add(User.getUserByUsername(comboBox.getValue()));
                usersUsernames.remove(comboBox.getValue());
            }
        });
    }



    public void back(MouseEvent mouseEvent) {
        App.changeMenu("ChooseRoomPage");
    }

    public void createRoom (MouseEvent mouseEvent) {
        ChooseRooms.rooms.add(usersChatting);
        App.changeMenu("ChooseRoomPage");
    }
}
