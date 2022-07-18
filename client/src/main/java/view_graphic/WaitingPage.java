package view_graphic;

import com.google.gson.Gson;
import controller.CivilizationController;
import controller.NetworkController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Request;
import model.User;

import java.io.IOException;

public class WaitingPage {
    @FXML
    private BorderPane borderPane;

    public void initialize(){
        Platform.runLater(() -> borderPane.requestFocus());
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));

        new Thread(() -> {
            try {
                String response = NetworkController.getSecondInputStream().readUTF();
                Request request = new Gson().fromJson(response, Request.class);
                if (request.getType().equals("startGame")) {
                    User.loadUserInfo(request.getInfo().get("userData"));
                    Game.setCivilizationController(new CivilizationController(User.getUsers()));
                    NetworkController.getSecondOutputStream().writeUTF(User.getGameData());
                    NetworkController.getSecondOutputStream().flush();
                    App.changeMenu("Game");
                } else if (request.getType().equals("gameStarted")) {
                    User.loadGameInfo(request.getInfo().get("gameData"));
                    App.changeMenu("Game");
                }

            } catch (IOException e) {
                System.out.println("can't start game");
                exit(null);
            }
        }).start();
    }

    public void exit(MouseEvent mouseEvent) {
        Request request = new Request("exitWaitingForGame");
        NetworkController.sendRequest(request);
        App.changeMenu("Lobby");
    }
}
