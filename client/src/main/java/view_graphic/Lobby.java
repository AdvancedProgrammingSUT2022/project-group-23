package view_graphic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.NetworkController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Request;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    @FXML
    private Button createButton;
    @FXML
    private TextField capacityTextField;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;

    private Text responseText;

    public void initialize(){
        Platform.runLater(() -> borderPane.requestFocus());
        createButton.setDisable(true);
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));

        String response = NetworkController.sendRequest(new Request("lobbyGames"));
        ArrayList<ArrayList<String>> gameList = new Gson().fromJson(response, new TypeToken<List<List<String>>>(){}.getType());


        for (int i = 0; i < gameList.size(); i++) {
            int capacity = Integer.parseInt(gameList.get(i).get(0));
            Text text = new Text("Game " + (i + 1) + " Capacity : " + capacity);
            VBox gameVbox = new VBox();
            StringBuilder nicknames = new StringBuilder("nicknames : ");
            for (int j = 1; j < gameList.get(i).size(); j++) {
                nicknames.append(j).append("- ").append(gameList.get(i).get(j)).append(" ");
            }
            text.setText(text.getText() + "\n" + nicknames);
            text.setFill(Color.BLACK);
            text.setFont(Font.font ("arial", 20));
            gameVbox.getChildren().add(text);
            String cssLayout = "-fx-border-color: red;\n" +
                    "-fx-border-insets: 5;\n" +
                    "-fx-border-width: 3;\n" ;
            gameVbox.setStyle(cssLayout);
            vbox.getChildren().add(gameVbox);
            int finalI = i;
            gameVbox.setOnMouseClicked(mouseEvent -> {
                Request request = new Request("joinGame");
                request.getInfo().put("gameNumber", String.valueOf(finalI));
                responseText.setText(NetworkController.sendRequest(request));
            });
        }
        responseText = new Text("");
        vbox.getChildren().add(responseText);
        responseText.setFill(Color.GREEN);
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }

    public void createGame(MouseEvent mouseEvent) {
        Request request = new Request("createGame");
        request.getInfo().put("capacity", capacityTextField.getText());
        responseText.setText(NetworkController.sendRequest(request));
        App.changeMenu("Lobby");
    }

    public void typeCapacity(KeyEvent keyEvent) {
        String capacityString = capacityTextField.getText();
        try {
            int capacity = Integer.parseInt(capacityString);
            if(capacity < 2)throw new NumberFormatException();
            createButton.setDisable(false);
            capacityTextField.setStyle("-fx-border-width: 0");
        }catch (NumberFormatException e){
            capacityTextField.setStyle("-fx-border-color: #ff0066;");
            createButton.setDisable(true);
        }
    }
}
