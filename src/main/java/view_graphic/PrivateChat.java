package view_graphic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;

public class PrivateChat {
    @FXML
    private VBox messageBox;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane borderPane;

    public void initialize(){
        Platform.runLater(() -> borderPane.requestFocus());
        Platform.runLater(() -> textArea.setMinWidth(borderPane.getWidth() - sendButton.getWidth()));
    }

    public void sendMessage(MouseEvent mouseEvent) {
        String message = textArea.getText();
        if(message.length() != 0){
            HBox hBox = new HBox();
            Text text = new Text(message);
            text.setStyle("-fx-font-size: 20");
            Circle currentAvatar =new Circle(20);
            ImagePattern imagePattern=new ImagePattern(new Image(User.getUserLogged().getProfilePictureURL()));
            currentAvatar.setFill(imagePattern);
            hBox.getChildren().add(currentAvatar);
            hBox.getChildren().add(text);
            messageBox.getChildren().add(hBox);
            hBox.getStyleClass().add("sendMessageBox");
            textArea.setText("");
        }
    }
}
