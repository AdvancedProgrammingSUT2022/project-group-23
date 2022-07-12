package view_graphic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class IntroPage {

    @FXML
    private BorderPane pane;

    private static MediaView introMediaView;
    private static MediaPlayer introMediaPlayer;

    static {
        introMediaPlayer = new MediaPlayer(new Media(LoginPage.class.getResource("/media/Civilization V- Brave New World Intro.mp4").toExternalForm()));
        introMediaView = new MediaView(introMediaPlayer);
    }

    public void initialize() {
        pane.setStyle("-fx-background-color: black");
        pane.getChildren().add(introMediaView);
        Platform.runLater(() -> {
            introMediaView.requestFocus();
            introMediaView.setFitWidth(pane.getWidth());
            introMediaView.setFitHeight(pane.getHeight());
            introMediaPlayer.setAutoPlay(true);
            introMediaPlayer.play();
            introMediaPlayer.setOnEndOfMedia(this::stopIntroVideo);
            introMediaView.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().getName().equals("Space")) {
                    introMediaPlayer.stop();
                    stopIntroVideo();
                }
            });
        });
    }

    private void stopIntroVideo() {
        pane.getChildren().remove(introMediaView);
        introMediaView = null;
        introMediaPlayer = null;
        App.changeMenu("LoginPage");
    }
}
