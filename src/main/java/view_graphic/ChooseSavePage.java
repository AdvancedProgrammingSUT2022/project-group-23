package view_graphic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;

public class ChooseSavePage {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    @FXML
    private VBox vbox;
    @FXML
    private Button selectButton;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        GameMenuPage.players = new ArrayList<>();
        title.setFill(Color.rgb(1, 231, 212));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        String[] fileNames=new File("src/main/resources/saves").list();
        for (String fileName : fileNames) {
            String name=fileName.toString().replaceAll(".json","");
            Button save=new Button(name);
            save.getStyleClass().add("secondary-btn");
            vbox.getChildren().add(save);
            save.setOnMouseClicked(mouseEvent -> {
                //TODO start game from save
            });
        }
    }


    public void back(MouseEvent mouseEvent) {
        App.changeMenu("GameMenu");
    }
}
