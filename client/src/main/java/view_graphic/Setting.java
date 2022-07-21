package view_graphic;

import controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Setting {
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
        Button mute=new Button("Mute");
        mute.getStyleClass().add("secondary-btn");
        mute.setOnMouseClicked(mouseEvent -> {
            LoginPage.getAudioMediaPlayer().setMute(!LoginPage.getAudioMediaPlayer().isMute());
        });
        vbox.getChildren().add(mute);
        Button fullScreen =new Button("Full Screen");
        fullScreen.getStyleClass().add("secondary-btn");
        fullScreen.setOnMouseClicked(mouseEvent -> {
            App.getStage().setFullScreen(!App.getStage().isFullScreen());
        });
        vbox.getChildren().add(fullScreen);

        Button exitGame = new Button("Exit");
        exitGame.getStyleClass().add("secondary-btn");
        exitGame.setOnMouseClicked(mouseEvent -> {
            GameController.setMapWidth(0);
            GameController.setMapHeight(0);
            App.changeMenu("GameMenu");
        });
        vbox.getChildren().add(exitGame);
    }


    public void back (MouseEvent mouseEvent) {
        App.changeMenu("Game");
    }
}