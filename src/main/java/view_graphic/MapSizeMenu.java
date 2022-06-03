package view_graphic;

import controller.GameController;
import enums.Commands;
import javafx.application.Platform;
import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.regex.Matcher;

public class MapSizeMenu {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    private Text error;
    @FXML
    private Label widthLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Button setButton;
    @FXML
    private TextField width;
    @FXML
    private TextField height;

    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        error = new Text("you have to set map width and map height both!");
        error.setFill(Color.RED);
        title.setFill(Color.rgb(1, 231, 212));
        widthLabel.setTextFill(Color.rgb(232, 200, 22));
        heightLabel.setTextFill(Color.rgb(232, 200, 22));
        setButton.setDisable(true);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
    }

    public void type(KeyEvent keyEvent) {
        String w=width.getText();
        String h=height.getText();
        if (((Commands.getCommandMatcher(w,Commands.WHICH_NUMBER))!=null)&&((Commands.getCommandMatcher(h,Commands.WHICH_NUMBER))!=null)) {
            if((Integer.parseInt(w)<6)||(Integer.parseInt(h)<6))
            {
                width.setStyle("-fx-border-color: #ff0066;");
                height.setStyle("-fx-border-color: #ff0066;");
                setButton.setDisable(true);
            }else {
                width.setStyle("-fx-border-width: 0");
                height.setStyle("-fx-border-width: 0");
                setButton.setDisable(false);
            }
        } else {
            width.setStyle("-fx-border-color: #ff0066;");
            height.setStyle("-fx-border-color: #ff0066;");
            setButton.setDisable(true);
        }
    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }

    public void set (MouseEvent mouseEvent) {
        GameController.setMapWidth(Integer.parseInt(width.getText()));
        GameController.setMapHeight(Integer.parseInt(height.getText()));
        App.changeMenu("GameMenu");
    }

    public void reset (MouseEvent mouseEvent) {
        GameController.setMapWidth(0);
        GameController.setMapHeight(0);
        App.changeMenu("GameMenu");
    }
}
