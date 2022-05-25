package view_graphic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;
import java.util.Collections;

public class ScorePage {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox1;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        title.setFill(Color.rgb(1, 231, 212));
        ArrayList<User> sortedUsers = User.getUsers();
        Collections.sort(sortedUsers);
        int i=1;
        for (User user : sortedUsers) {
            HBox hBox=new HBox();
            Circle circle=new Circle(20);
            ImagePattern imagePattern=new ImagePattern(new Image(user.getProfilePictureURL()));
            circle.setFill(imagePattern);
            Text text=new Text(i+"-  "+user.getNickname()+"     Score:  "+user.getScore()+"    Last Win:  "+user.getLastWin()+"     Last Online:    "+user.getLastOnline());
            text.getStyleClass().add("text2");
            text.setFill(Color.rgb(200,50,50));
            if(user.equals(User.getUserLogged())){
                text.getStyleClass().add("scoreUserLogged");
                text.setFill(Color.rgb(68,240,21));
                circle.setRadius(30);
            }
            hBox.getChildren().add(circle);
            hBox.getChildren().add(text);
            vbox1.getChildren().add(hBox);
            i++;
        }
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }
}
