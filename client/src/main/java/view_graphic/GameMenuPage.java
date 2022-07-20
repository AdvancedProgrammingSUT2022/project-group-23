package view_graphic;

import controller.CivilizationController;
import controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class GameMenuPage {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private Button lobbyButton;
    @FXML
    private Button inviteButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox vbox;
    @FXML
    private Button mapButton;

  //  public static ArrayList<User> players =new ArrayList<>();
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());

        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        Tooltip tooltip1=new Tooltip("go to lobby");
        lobbyButton.setTooltip(tooltip1);
        Tooltip tooltip3=new Tooltip("invite other players to play with you");
        inviteButton.setTooltip(tooltip3);
        Tooltip tooltip4=new Tooltip("go back to main menu");
        backButton.setTooltip(tooltip4);
        Tooltip tooltip5=new Tooltip("set map width and map height");
        mapButton.setTooltip(tooltip5);
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }

    public void invite (MouseEvent mouseEvent) {
        if(GameController.getMapWidth()==0 && GameController.getMapHeight()==0){
            GameController.setMapHeight(10);
            GameController.setMapWidth(10);
        }
        App.changeMenu("InvitePage");
    }


    public void Lobby (MouseEvent mouseEvent) {
        if(GameController.getMapWidth()==0 && GameController.getMapHeight()==0){
            GameController.setMapHeight(10);
            GameController.setMapWidth(10);
        }
        App.changeMenu("Lobby");
    }

    public void map (MouseEvent mouseEvent) {
        App.changeMenu("MapSizeMenu");
    }

}
