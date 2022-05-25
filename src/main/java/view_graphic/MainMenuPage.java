package view_graphic;

import controller.MainMenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class MainMenuPage {
    private MainMenuController mainMenuController;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        title.setFill(Color.rgb(1, 231, 212));
        mainMenuController=MainMenuController.getInstance();
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
    }

    public void profileMenu(){
        App.changeMenu("ProfilePage");
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void logout (MouseEvent mouseEvent) {
        mainMenuController.logout();
        App.changeMenu("LoginPage");
    }

    public void scoreMenu (MouseEvent mouseEvent) {
        App.changeMenu("ScorePage");
    }

    public void chatMenu (MouseEvent mouseEvent) {
        App.changeMenu("ChatPage");
    }

    public void gameMenu(){
        //TODO change scene to gameMenu
    }
}
