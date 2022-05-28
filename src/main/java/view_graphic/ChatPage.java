package view_graphic;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class ChatPage {
    @FXML
    private VBox messageBox;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane borderPane;

    private static String chatType;
    private static String chatName;

    public void initialize(){
        Platform.runLater(() -> borderPane.requestFocus());
        Platform.runLater(() -> textArea.setMinWidth(borderPane.getWidth() - sendButton.getWidth()));
        textArea.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().getName().equals("Enter")) {
                if (!keyEvent.isControlDown() && !keyEvent.isShiftDown())
                    sendMessage(null);
                else textArea.appendText("\n");
            }
        });
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
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.getStyleClass().add("editButton");
            deleteButton.getStyleClass().add("deleteButton");
            deleteButton.setOnMouseClicked(mouseEvent1 -> {
                messageBox.getChildren().remove(hBox);
            });
            hBox.setSpacing(5);
            hBox.getChildren().add(currentAvatar);
            hBox.getChildren().add(text);
            hBox.getChildren().add(editButton);
            hBox.getChildren().add(deleteButton);
            Platform.runLater(() -> hBox.setMaxWidth(hBox.getWidth()));
            messageBox.getChildren().add(hBox);
            hBox.getStyleClass().add("sendMessageBox");
            textArea.setText("");
        }
    }

    public static void setChatType(String chatType) {
        ChatPage.chatType = chatType;
    }

    public static void setChatName(String chatName) {
        ChatPage.chatName = chatName;
    }
}
