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
    private Button newGameButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button inviteButton;
    @FXML
    private Button backButton;
    private Text error;
    @FXML
    private VBox vbox;
    @FXML
    private Button mapButton;
    @FXML
    private Button autoSaveButton;
    public static ArrayList<User> players =new ArrayList<>();
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        error=new Text("you have to invite at least 1 other player from invite menu");
        error.setFill(Color.RED);
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        Tooltip tooltip1=new Tooltip("starting a new game");
        newGameButton.setTooltip(tooltip1);
        Tooltip tooltip2=new Tooltip("continuing your previous plays");
        continueButton.setTooltip(tooltip2);
        Tooltip tooltip3=new Tooltip("invite other players to play with you");
        inviteButton.setTooltip(tooltip3);
        Tooltip tooltip4=new Tooltip("go back to main menu");
        backButton.setTooltip(tooltip4);
        Tooltip tooltip5=new Tooltip("set map width and map height");
        mapButton.setTooltip(tooltip5);
        Tooltip tooltip6=new Tooltip("set when and how many times you want auto save");
        autoSaveButton.setTooltip(tooltip6);
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }

    public void invite (MouseEvent mouseEvent) {
        App.changeMenu("InvitePage");
    }

    public void save(){
        App.changeMenu("ChooseSavePage");
    }

    public void newGame (MouseEvent mouseEvent) {
        if(players.size()<2){
            if(!vbox.getChildren().contains(error))
                vbox.getChildren().add(error);
        }else {
            if(GameController.mapWidth==0 && GameController.mapHeight==0){
                GameController.setMapHeight(5*players.size());
                GameController.setMapWidth(5*players.size());
            }
            Game.civilizationController=new CivilizationController(players);
            App.changeMenu("Game");
        }
    }

    public void map (MouseEvent mouseEvent) {
        App.changeMenu("MapSizeMenu");
    }

    public void autoSave (MouseEvent mouseEvent) {
        App.changeMenu("AutoSaveMenu");
    }
}
