package view_graphic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class InvitePage {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;
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
        vbox.getChildren().add(comboBox);
        comboBox.setOnAction(actionEvent -> {
            if(!GameMenuPage.players.contains(User.getUserByUsername(comboBox.getValue().toString()))) {
                Text text = new Text(comboBox.getValue().toString() + " selected");
                text.getStyleClass().add("text2");
                text.setFill(Color.rgb(70, 210, 25));
                vbox.getChildren().add(text);
                GameMenuPage.players.add(User.getUserByUsername(comboBox.getValue().toString()));
                usersUsernames.remove(comboBox.getValue());
            }
        });
    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }
}
