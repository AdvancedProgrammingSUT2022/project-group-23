package view_graphic;

import com.google.gson.Gson;
import controller.CivilizationController;
import controller.NetworkController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Request;
import model.User;

import java.io.IOException;
import java.util.ArrayList;

public class InvitePage {
    @FXML
    private Button receivedInvitesButton;
    @FXML
    private Label capacityLabel;
    @FXML
    private VBox containerVbox;
    @FXML
    private TextField capacityTextField;
    @FXML
    private Button createButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;

    private ArrayList<String> invitedUsers;

    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        createButton.setDisable(true);
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));



    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }

    public void createGame(MouseEvent mouseEvent) {
        Request request = new Request("createGame");
        request.getInfo().put("capacity", capacityTextField.getText());
        String response = NetworkController.sendRequest(request);
        if(response.contains("successful")){
            invitedUsers = new ArrayList<>();
            request = new Request("userInfo");
            User.loadUserInfo(NetworkController.sendRequest(request));
            containerVbox.getChildren().remove(capacityLabel);
            containerVbox.getChildren().remove(capacityTextField);
            containerVbox.getChildren().remove(createButton);
            containerVbox.getChildren().remove(receivedInvitesButton);
            Button sendInviteButton = new Button("Send invite");
            sendInviteButton.getStyleClass().add("primary-btn");
            containerVbox.getChildren().add(sendInviteButton);

            ArrayList<String> usersUsernames = new ArrayList<>();
            for (User user : User.getUsers()) {
                if (!user.getUsername().equals(User.getUsernameLogged())) {
                    usersUsernames.add(user.getUsername());
                }
            }
            ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(usersUsernames));
            comboBox.setMinWidth(100);
            vbox.getChildren().add(comboBox);
            sendInviteButton.setOnMouseClicked(mouseEvent1 -> {
                if (comboBox.getValue() != null && !invitedUsers.contains(comboBox.getValue())) {
                    Text text = new Text(comboBox.getValue() + " invited");
                    text.getStyleClass().add("text2");
                    text.setFill(Color.rgb(70, 210, 25));
                    vbox.getChildren().add(text);
                    invitedUsers.add(comboBox.getValue());
                    Request inviteRequest = new Request("sendInvite");
                    inviteRequest.getInfo().put("source", User.getUsernameLogged());
                    inviteRequest.getInfo().put("destination", comboBox.getValue());
                    NetworkController.sendRequest(inviteRequest);
                    usersUsernames.remove(comboBox.getValue());
                }
            });
            new Thread(() -> {
                try {
                    while (true) {
                        String responseToInvite = NetworkController.getSecondInputStream().readUTF();
                        if(!responseToInvite.contains("accept") && !responseToInvite.contains("reject")) {
                            if(!responseToInvite.contains("startGame"))break;
                            Request requestStartGame = new Gson().fromJson(responseToInvite, Request.class);
                            if (requestStartGame.getType().equals("startGame")) {
                                User.loadUserInfo(requestStartGame.getInfo().get("userData"));
                                Game.setCivilizationController(new CivilizationController(User.getUsers()));
                                NetworkController.getSecondOutputStream().writeUTF(User.getGameData());
                                NetworkController.getSecondOutputStream().flush();
                                App.changeMenu("Game");
                            }
                            break;
                        }

                        Platform.runLater(() -> {
                            Text text = new Text(responseToInvite);
                            text.getStyleClass().add("text2");
                            text.setFill(Color.rgb(3, 210, 25));
                            vbox.getChildren().add(text);
                        });
                    }

                } catch (IOException e) {
                    System.out.println("can't read listener stream");
                }
            }).start();



        }

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

    public void openReceivedInvitationsPage(MouseEvent mouseEvent) {
        App.changeMenu("ReceivedInvitesPage");
    }
}
