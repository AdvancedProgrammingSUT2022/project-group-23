package view_graphic;

import controller.MainMenuController;
import controller.ProfileController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.User;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

public class ProfilePage {
    ProfileController profileController;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Text title;
    private Text text;
    @FXML
    private VBox vbox;
    @FXML
    private VBox vbox1;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField nickname;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField currentPassword;
    @FXML
    private Button nicknameButton;
    @FXML
    private Button passwordButton;
    private HBox hbox1;
    private HBox hbox2;
    private Circle currentAvatar;
    public void initialize() {
        Platform.runLater(() -> borderPane.requestFocus());
        hbox1=new HBox();
        hbox2=new HBox();
        Text text1 = new Text("your current avatar:  ");
        text1.setFill(Color.rgb(200,50,50));
        text1.getStyleClass().add("text1");
        currentAvatar=new Circle(20);
        ImagePattern imagePattern=new ImagePattern(new Image(User.getUserLogged().getProfilePictureURL()));
        currentAvatar.setFill(imagePattern);
        hbox1.getChildren().add(text1);
        hbox1.getChildren().add(currentAvatar);
        Text text2 = new Text("choose new avatar:  ");
        text2.setFill(Color.rgb(200,50,50));
        text2.getStyleClass().add("text2");
        hbox2.getChildren().add(text2);
        for(int i=0;i<6;i++){
            Circle circle=new Circle(20);
            ImagePattern imagePattern1=new ImagePattern(new Image(getClass().getResource("/images/profilePictures/"+i+".png").toExternalForm()));
            circle.setFill(imagePattern1);
            hbox2.getChildren().add(circle);
            int finalI = i;
            circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle (MouseEvent mouseEvent) {
                    currentAvatar.setFill(imagePattern1);
                    User.getUserLogged().setProfilePictureURL(getClass().getResource("/images/profilePictures/"+ finalI +".png").toExternalForm());
                    User.updateUsersInfo();
                }
            });
        }
        Button button=new Button();
        button.setText("other...");
        button.getStyleClass().add("button1");
        hbox2.getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle (MouseEvent mouseEvent) {
                File file=new FileChooser().showOpenDialog(App.getStage());
                try {
                    User.getUserLogged().setProfilePictureURL(file.toURI().toURL().toExternalForm());
                    ImagePattern imagePattern1=new ImagePattern(new Image(User.getUserLogged().getProfilePictureURL()));
                    currentAvatar.setFill(imagePattern1);
                    User.updateUsersInfo();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        nicknameLabel.setTextFill(Color.rgb(232, 200, 22));
        passwordLabel.setTextFill(Color.rgb(232, 200, 22));
        title.setFill(Color.rgb(1, 231, 212));
        profileController = ProfileController.getInstance();
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));
        text = new Text();
        vbox.getChildren().add(text);
        vbox1.getChildren().add(hbox1);
        vbox1.getChildren().add(hbox2);
        passwordButton.setDisable(true);
        nicknameButton.setDisable(true);

    }

    public void typePassword(KeyEvent keyEvent) {
        if (newPassword.getText().length() < 5 || currentPassword.getText().length() < 5) {
            newPassword.setStyle("-fx-border-color: #ff0066;");
            currentPassword.setStyle("-fx-border-color: #ff0066;");
            passwordButton.setDisable(true);
        } else if(newPassword.getText().length() >= 5 && currentPassword.getText().length() >= 5){
            newPassword.setStyle("-fx-border-width: 0");
            currentPassword.setStyle("-fx-border-width: 0");
            passwordButton.setDisable(false);
        }
    }

    public void typeNickname(KeyEvent keyEvent) {
        if (nickname.getText().length() < 1) {
            nickname.setStyle("-fx-border-color: #ff0066;");
            nicknameButton.setDisable(true);
        } else{
            nickname.setStyle("-fx-border-width: 0");
            nicknameButton.setDisable(false);
        }
    }

    public void changeNickname(){
        String output=profileController.changeNickname(nickname.getText());
        if(output.equals("nickname changed successfully!")){
            nickname.setText("");
            nicknameButton.setDisable(true);
            text.setFill(Color.GREEN);
            text.setText(output);
        } else {
            text.setFill(Color.RED);
            text.setText(output);
        }
    }

    public void changePassword(){
        String output=profileController.changePassword(currentPassword.getText(),newPassword.getText());
        if(output.equals("password changed successfully!")){
            newPassword.setText("");
            currentPassword.setText("");
            passwordButton.setDisable(true);
            text.setFill(Color.GREEN);
            text.setText(output);
        } else {
            text.setFill(Color.RED);
            text.setText(output);
        }
    }

    public void back(MouseEvent mouseEvent) {
        App.changeMenu("MainMenuPage");
    }
}
