package view_graphic;

import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import model.GraphicTile;
import model.Tile;
import view.GameView;

import java.util.ArrayList;

public class Game {
    @FXML
    private AnchorPane tileMap;
    @FXML
    private HBox hbox;
    private GraphicTile[][] tiles;
    private double size = 100,v=Math.sqrt(3)/2.0;
    private CivilizationController civilizationController;
    private UnitController unitController;
    private CityController cityController;

    public void initialize(){
        Platform.runLater(()->tileMap.requestFocus());
        civilizationController=new CivilizationController(GameMenuPage.players);
        unitController=civilizationController.getUnitController();
        cityController=civilizationController.getCityController();
        hbox.setMinHeight(100);
        hbox.setMinWidth(1280);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/topBar.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        hbox.setBackground(new Background(backgroundImage));
        tileMap.setOnKeyPressed(this::move);
        tiles=new GraphicTile[civilizationController.getMapHeight()][civilizationController.getMapWidth()];
        for(double y=0,i=0;i<civilizationController.getMapHeight();y+=size*Math.sqrt(3),i+=1)
        {
            for(double x=size/2,dy=y,j=0;j<civilizationController.getMapWidth();x+=(3.0/2.0)*size,j+=1)
            {
                GraphicTile tile = new GraphicTile(x,dy,
                        x+size,dy,
                        x+size*(3.0/2.0),dy+size*v,
                        x+size,dy+size*Math.sqrt(3),
                        x,dy+size*Math.sqrt(3),
                        x-(size/2.0),dy+size*v,(int)i+","+(int)j);
                Tile[][] modelTiles= civilizationController.getTiles();
                    tile.setFill(Color.rgb(40, 220, 230));
                    tile.setStroke(Color.rgb(15, 65, 135));
                if(modelTiles[(int) i][(int) j].getVisibilityForUser(civilizationController.getTurn()).equals("visible")){
                    tile.setFill(Color.rgb(250,210,0));
                    tile.setStroke(Color.rgb(100,90,20));
                }else if(modelTiles[(int) i][(int) j].getVisibilityForUser(civilizationController.getTurn()).equals("revealed")){
                    tile.setFill(Color.rgb(180,130,225));
                    tile.setStroke(Color.rgb(50,20,100));
                }
                tile.setStrokeWidth(4);
                tileMap.getChildren().add(tile);
                tileMap.getChildren().add(tile.getLocation());
                tiles[(int) i][(int) j]=tile;
                dy = dy==y ? dy+size*v : y;
            }
        }
    }

    public void move(KeyEvent keyEvent){
        double dx=0,dy=0;
        if(keyEvent.getCode().getName().equals("Right") && tiles[civilizationController.getMapHeight()-1][civilizationController.getMapWidth()-1].getPoints().get(4)>1280){
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
        if(keyEvent.getCode().getName().equals("Down") && tiles[civilizationController.getMapHeight()-1][1].getPoints().get(7)>620){
            dx = 0.0;
            dy = -10.0;
        }
        for(int i=0;i<civilizationController.getMapHeight();i++){
            for(int j=0;j<civilizationController.getMapWidth();j++){
                tiles[i][j].getPoints().set(0,(tiles[i][j].getPoints().get(0)+dx));
                tiles[i][j].getPoints().set(1,(tiles[i][j].getPoints().get(1)+dy));
                tiles[i][j].getPoints().set(2,(tiles[i][j].getPoints().get(2)+dx));
                tiles[i][j].getPoints().set(3,(tiles[i][j].getPoints().get(3)+dy));
                tiles[i][j].getPoints().set(4,(tiles[i][j].getPoints().get(4)+dx));
                tiles[i][j].getPoints().set(5,(tiles[i][j].getPoints().get(5)+dy));
                tiles[i][j].getPoints().set(6,(tiles[i][j].getPoints().get(6)+dx));
                tiles[i][j].getPoints().set(7,(tiles[i][j].getPoints().get(7)+dy));
                tiles[i][j].getPoints().set(8,(tiles[i][j].getPoints().get(8)+dx));
                tiles[i][j].getPoints().set(9,(tiles[i][j].getPoints().get(9)+dy));
                tiles[i][j].getPoints().set(10,(tiles[i][j].getPoints().get(10)+dx));
                tiles[i][j].getPoints().set(11,(tiles[i][j].getPoints().get(11)+dy));
                tiles[i][j].getLocation().setX(tiles[i][j].getLocation().getX()+dx);
                tiles[i][j].getLocation().setY(tiles[i][j].getLocation().getY()+dy);
                if(tiles[i][j].getPoints().get(1)<0){
                    tiles[i][j].setDisable(true);
                    tiles[i][j].setOpacity(0);
                    tileMap.getChildren().remove(tiles[i][j].getLocation());
                }
                else if(tiles[i][j].getOpacity()==0.0){
                    tiles[i][j].setDisable(false);
                    tiles[i][j].setOpacity(1);
                    tileMap.getChildren().add(tiles[i][j].getLocation());
                }
            }
        }
    }
}
