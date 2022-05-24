package view_graphic;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class ChatPage {

    @FXML
    private BorderPane borderPane;

    public void initialize(){
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }

    public void enterPublicChatMenu(MouseEvent mouseEvent) {
        App.changeMenu("PublicChatPage");
    }

    public void enterPrivateChatMenu(MouseEvent mouseEvent) {
    }

    public void enterChatRooms(MouseEvent mouseEvent) {
    }
}
