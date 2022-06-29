package view_graphic;

import enums.Commands;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import model.User;

import java.util.ArrayList;

public class AutoSaveMenu {
    private static String selectedAutoSave;
    private static int autoSaveNumber;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;
    @FXML
    private Button setButton;
    @FXML
    private Label autoSaveLabel;
    @FXML
    private TextField autoSave;
    public void initialize() {
        autoSaveNumber = 0;
        Platform.runLater(() -> borderPane.requestFocus());
        setButton.setDisable(true);
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        autoSaveLabel.setTextFill(Color.rgb(232, 200, 22));
        ArrayList<String> autoSaves=new ArrayList<>();
        autoSaves.add("after founding a city");
        autoSaves.add("after studying a technology");
        autoSaves.add("after building a unit");
        autoSaves.add("after every improvement");
        ComboBox<String> comboBox=new ComboBox<>(FXCollections.observableArrayList(autoSaves));
        comboBox.setMinWidth(100);
        vbox.getChildren().add(comboBox);
        setButton.setOnMouseClicked(mouseEvent -> {
            selectedAutoSave=comboBox.getValue();
            autoSaveNumber=Integer.parseInt(autoSave.getText());
            App.changeMenu("GameMenu");
        });
    }

    public void type() {
        String autoSaveText=autoSave.getText();
        if (((Commands.getCommandMatcher(autoSaveText,Commands.WHICH_NUMBER))!=null)) {
                autoSave.setStyle("-fx-border-width: 0");
                setButton.setDisable(false);
        } else {
            autoSave.setStyle("-fx-border-color: #ff0066;");
            setButton.setDisable(true);
        }
    }

    public void back (MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }

    public static String getSelectedAutoSave() {
        return selectedAutoSave;
    }

    public static int getAutoSaveNumber() {
        return autoSaveNumber;
    }
}
