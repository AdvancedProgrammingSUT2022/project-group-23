package view_graphic;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ChatPage {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    public void initialize(){
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        title.setFill(Color.rgb(1, 231, 212));
    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }

    public void enterPublicChatMenu(MouseEvent mouseEvent) {
        App.changeMenu("PublicChatPage");
    }

    public void enterPrivateChatMenu(MouseEvent mouseEvent) {
        App.changeMenu("ChoosePrivateChat");
    }

    public void enterChatRooms(MouseEvent mouseEvent) {
        App.changeMenu("ChooseRoomPage");
    }
}
