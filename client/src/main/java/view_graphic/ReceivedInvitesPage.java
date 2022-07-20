package view_graphic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.NetworkController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Request;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class ReceivedInvitesPage {

    @FXML
    private Button acceptButton;
    @FXML
    private Button rejectButton;
    @FXML
    private VBox vbox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;

    public void initialize(){
        Platform.runLater(() -> borderPane.requestFocus());
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        Request request = new Request("getInvites");
        request.getInfo().put("username", User.getUsernameLogged());
        String response = NetworkController.sendRequest(request);
        ArrayList<String> invites = new Gson().fromJson(response, new TypeToken<List<String>>(){}.getType());
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(invites));
        comboBox.setMinWidth(100);
        vbox.getChildren().add(comboBox);
        acceptButton.setOnMouseClicked(mouseEvent -> {
            if (comboBox.getValue() != null){
                Request answer = new Request("answerInvite");
                answer.getInfo().put("answer", "accept");
                answer.getInfo().put("inviter", comboBox.getValue());
                NetworkController.sendRequest(answer);
                App.changeMenu("WaitingPage");
            }
        });
        rejectButton.setOnMouseClicked(mouseEvent -> {
            if (comboBox.getValue() != null){
                Request answer = new Request("answerInvite");
                answer.getInfo().put("answer", "reject");
                answer.getInfo().put("inviter", comboBox.getValue());
                NetworkController.sendRequest(answer);
            }
        });

    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("InvitePage");
    }
}
