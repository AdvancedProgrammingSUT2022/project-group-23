package view_graphic;

import controller.GameController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.City;
import model.Tile;
import model.User;

import java.util.ArrayList;

public class Diplomacy {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;
    @FXML
    private VBox vboxUp;
    @FXML
    private HBox hBox;
    @FXML
    private Button selectButton;
    private static User discussedCivilization;
    private ArrayList<String> messages=new ArrayList<>();
    private ArrayList<String> givingTrade=new ArrayList<>();
    private ArrayList<String> receivingTrade=new ArrayList<>();
    private Text dialog = new Text();

    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        dialog.setFill(Color.rgb(180,160,10));
        ArrayList<String > usersUsernames=new ArrayList<>();
        for (User user : GameController.getPlayers()) {
            if(!user.equals(User.getUserLogged())){
                usersUsernames.add(user.getUsername());
            }
        }
        ComboBox<String> comboBox=new ComboBox<>(FXCollections.observableArrayList(usersUsernames));
        comboBox.setMinWidth(100);
        vbox.getChildren().add(comboBox);
        selectButton.setOnMouseClicked(mouseEvent -> {
            if(comboBox.getValue() != null) {
                discussedCivilization=User.getUserByUsername(comboBox.getValue());
                openDiscussion();
            }
        });
    }

    private void openDiscussion(){
        title.setText("Discussion");
        vboxUp.getChildren().remove(selectButton);
        vbox.getChildren().clear();
        TextArea chatBox=new TextArea();
        chatBox.setPromptText("discuss whit this civilization");
        chatBox.getStyleClass().add("chatTextArea");
        hBox.getChildren().add(chatBox);
        Button chatButton=new Button("Send");
        chatButton.getStyleClass().add("sendButton");
        hBox.getChildren().add(chatButton);
        chatButton.setOnMouseClicked(mouseEvent -> {
            if(chatBox.getText().length()>0){
                messages.add(chatBox.getText());
                chatBox.clear();
                dialog.setText("Message Sent!");
            }
        });
        Button declareWar=new Button("Declare War");
        declareWar.getStyleClass().add("primary-btn");
        vbox.getChildren().add(declareWar);
        declareWar.setOnMouseClicked(mouseEvent -> {
            if(!GameController.getCurrentPlayer().getEnemies().contains(discussedCivilization)){
                GameController.getCurrentPlayer().getEnemies().add(discussedCivilization);
                GameController.getCurrentPlayer().getConfederate().remove(discussedCivilization);
                discussedCivilization.getEnemies().add(discussedCivilization);
                discussedCivilization.getConfederate().remove(discussedCivilization);
                dialog.setText("you declared war with this civilization");
            }else {
                dialog.setText("you already have declared war with this civilization");
            }
        });
        Button peace =new Button("Make Peace");
        peace.getStyleClass().add("primary-btn");
        vbox.getChildren().add(peace);
        peace.setOnMouseClicked(mouseEvent -> {
            if(!GameController.getCurrentPlayer().getConfederate().contains(discussedCivilization)){
                GameController.getCurrentPlayer().getConfederate().add(discussedCivilization);
                GameController.getCurrentPlayer().getEnemies().remove(discussedCivilization);
                discussedCivilization.getConfederate().add(discussedCivilization);
                discussedCivilization.getEnemies().remove(discussedCivilization);
                dialog.setText("you made peace with this civilization");
            }else {
                dialog.setText("you already have made peace with this civilization");
            }
        });
        HBox trade=new HBox();
        trade.setSpacing(10);
        VBox givingVBox=new VBox();
        givingVBox.setAlignment(Pos.CENTER);
        Text givingText =new Text("You Give: ");
        givingVBox.getChildren().add(givingText);
        ArrayList<String> giving=new ArrayList<>();
        giving.add("+5 Gold");
        for (City city : GameController.getCurrentPlayer().getCities()) {
            for (Tile tile : city.getTiles()) {
                if(tile.getResource()!=null){
                    giving.add(tile.getResource().getName());
                }
            }
        }
        ComboBox<String> givingComboBox=new ComboBox<>(FXCollections.observableArrayList(giving));
        givingComboBox.setMinWidth(150);
        givingVBox.getChildren().add(givingComboBox);
        Button addGiving=new Button("Add");
        addGiving.getStyleClass().add("primary-btn");
        addGiving.setMaxWidth(150);
        givingVBox.getChildren().add(addGiving);
        addGiving.setOnMouseClicked(mouseEvent -> {
            if(givingComboBox.getValue()!=null){
                Text givingTextDialog=new Text(givingComboBox.getValue()+" added!");
                givingTextDialog.setFill(Color.rgb(180,160,10));
                givingVBox.getChildren().add(givingTextDialog);
                givingTrade.add(givingComboBox.getValue());
            }
        });
        trade.getChildren().add(givingVBox);
        trade.setAlignment(Pos.CENTER);
        VBox receivingVBox=new VBox();
        receivingVBox.setAlignment(Pos.CENTER);
        Text receivingText =new Text("You receive: ");
        receivingVBox.getChildren().add(receivingText);
        ArrayList<String> receiving =new ArrayList<>();
        receiving.add("+5 Gold");
        for (City city : discussedCivilization.getCities()) {
            for (Tile tile : city.getTiles()) {
                if(tile.getResource()!=null){
                    receiving.add(tile.getResource().getName());
                }
            }
        }
        ComboBox<String> receivingComboBox =new ComboBox<>(FXCollections.observableArrayList(receiving));
        receivingComboBox.setMinWidth(150);
        receivingVBox.getChildren().add(receivingComboBox);
        Button addReceiving=new Button("Add");
        addReceiving.getStyleClass().add("primary-btn");
        addReceiving.setMaxWidth(150);
        addReceiving.setOnMouseClicked(mouseEvent -> {
            if(receivingComboBox.getValue()!=null){
                Text receivingTextDialog=new Text(receivingComboBox.getValue()+" added!");
                receivingTextDialog.setFill(Color.rgb(180,160,10));
                receivingVBox.getChildren().add(receivingTextDialog);
                receivingTrade.add(receivingComboBox.getValue());
            }
        });
        receivingVBox.getChildren().add(addReceiving);
        trade.getChildren().add(receivingVBox);
        vbox.getChildren().add(trade);
        Button tradeButton=new Button("Trade");
        tradeButton.getStyleClass().add("primary-btn");
        tradeButton.setOnMouseClicked(mouseEvent -> {
            dialog.setText("Trade Sent To Civilization!");
        });
        vbox.getChildren().add(tradeButton);
        vbox.getChildren().add(dialog);
    }

    public void back (MouseEvent mouseEvent) {
        App.changeMenu("Game");
    }
}
