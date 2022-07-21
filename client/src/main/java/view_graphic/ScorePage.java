package view_graphic;

import controller.NetworkController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import model.Request;
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
    private Timeline updateTimeline;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        title.setFill(Color.rgb(1, 231, 212));
        addScoresToVbox();
        updateTimeline = new Timeline(new KeyFrame(Duration.seconds(2),actionEvent -> {
            vbox1.getChildren().clear();
            addScoresToVbox();
        }));
        updateTimeline.setCycleCount(-1);
        updateTimeline.play();
    }
    public void addScoresToVbox(){
        Request request = new Request("userInfo");
        User.loadUserInfo(NetworkController.sendRequest(request));
        ArrayList<User> sortedUsers = User.getUsers();
        Collections.sort(sortedUsers);
        int i=1;
        for (User user : sortedUsers) {
            HBox hBox=new HBox();
            Circle circle=new Circle(20);
            ImagePattern imagePattern=new ImagePattern(new Image(user.getProfilePictureURL()));
            circle.setFill(imagePattern);
            Text text;
            if(user.getLastOnline().equals("Online"))
                text = new Text(i+"-  "+user.getNickname()+"     Score:  "+user.getHighScore()+"    Last Win:  "+user.getLastWin()+"     Online");
            else
                text = new Text(i+"-  "+user.getNickname()+"     Score:  "+user.getHighScore()+"    Last Win:  "+user.getLastWin()+"     Last Online:    "+user.getLastOnline());

            text.getStyleClass().add("text2");
            text.setFill(Color.rgb(200,50,50));
            if(user.getUsername().equals(User.getUsernameLogged())){
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
        updateTimeline.stop();
        App.changeMenu("MainMenuPage");
    }
}
