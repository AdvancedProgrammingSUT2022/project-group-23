package view_graphic;

import controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Game {
    @FXML
    private AnchorPane tileMap;
    @FXML
    private HBox hbox;
    private Polygon[][] tiles;
    private double size = 100,v=Math.sqrt(3)/2.0;

    public void initialize(){
        Platform.runLater(()->tileMap.requestFocus());
        hbox.setMinHeight(100);
        hbox.setMinWidth(1280);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/topBar.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        hbox.setBackground(new Background(backgroundImage));
        tileMap.setOnKeyPressed(keyEvent -> {
            move(keyEvent);
        });
        tiles=new Polygon[10][10];
        for(double y=0,i=0;i<10;y+=size*Math.sqrt(3),i+=1)
        {
            for(double x=size/2,dy=y,j=0;j<10;x+=(3.0/2.0)*size,j+=1)
            {
                Polygon tile = new Polygon();
                tile.getPoints().addAll(x,dy,
                        x+size,dy,
                        x+size*(3.0/2.0),dy+size*v,
                        x+size,dy+size*Math.sqrt(3),
                        x,dy+size*Math.sqrt(3),
                        x-(size/2.0),dy+size*v);
                tile.setFill(Color.rgb(40,190,230));
                tile.setStrokeWidth(4);
                tile.setStroke(Color.rgb(15,65,135));
                tileMap.getChildren().add(tile);
                tiles[(int) i][(int) j]=tile;
                dy = dy==y ? dy+size*v : y;
            }
        }
    }

    public void move(KeyEvent keyEvent){
        double dx=0,dy=0;
        if(keyEvent.getCode().getName().equals("Right") && tiles[9][9].getPoints().get(4)>1280){
                dx = -10.0;
                dy = 0.0;
        }
        if(keyEvent.getCode().getName().equals("Left") && tiles[0][0].getPoints().get(10)<0){
            dx = 10.0;
            dy = 0.0;
        }
        if(keyEvent.getCode().getName().equals("Up") && tiles[0][0].getPoints().get(1)<0){
            dx = 0.0;
            dy = 10.0;
        }
        if(keyEvent.getCode().getName().equals("Down") && tiles[9][1].getPoints().get(7)>620){
            dx = 0.0;
            dy = -10.0;
        }
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                tiles[i][j].getPoints().setAll(tiles[i][j].getPoints().get(0)+dx,tiles[i][j].getPoints().get(1)+dy,
                        tiles[i][j].getPoints().get(2)+dx,tiles[i][j].getPoints().get(3)+dy,
                        tiles[i][j].getPoints().get(4)+dx,tiles[i][j].getPoints().get(5)+dy,
                        tiles[i][j].getPoints().get(6)+dx,tiles[i][j].getPoints().get(7)+dy,
                        tiles[i][j].getPoints().get(8)+dx,tiles[i][j].getPoints().get(9)+dy,
                        tiles[i][j].getPoints().get(10)+dx,tiles[i][j].getPoints().get(11)+dy);
                if(tiles[i][j].getPoints().get(1)<0){
                    tiles[i][j].setDisable(true);
                    tiles[i][j].setOpacity(0);
                }
                else {
                    tiles[i][j].setDisable(false);
                    tiles[i][j].setOpacity(1);
                }
            }
        }
    }
}
