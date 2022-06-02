package view_graphic;

import controller.CityController;
import controller.CivilizationController;
import controller.UnitController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.GraphicTile;
import model.Tile;
import model.Unit;

public class Game {
    @FXML
    private AnchorPane tileMap;
    @FXML
    private HBox bar;
    private GraphicTile[][] tiles;
    private double size = 100,v=Math.sqrt(3)/2.0;
    public static CivilizationController civilizationController;
    private UnitController unitController;
    private CityController cityController;
    private VBox tileInformation;
    private VBox unitInformation;
    private Text nextTurnError=new Text();

    public void initialize(){
        Timeline focusTimeline = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> {
            tileMap.requestFocus();
        }));
        focusTimeline.setCycleCount(-1);
        focusTimeline.play();
        unitController=civilizationController.getUnitController();
        cityController=civilizationController.getCityController();
        bar.setMinHeight(70);
        bar.setMinWidth(1280);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/topBar.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        bar.setBackground(new Background(backgroundImage));
        bar.setSpacing(30);
        Text userNickname=new Text(civilizationController.getCurrentPlayer().getNickname());
        userNickname.setY(45);
        userNickname.getStyleClass().add("info");
        bar.getChildren().add(userNickname);
        Circle gold=new Circle(30);
        gold.setCenterY(15);
        ImagePattern goldImage=new ImagePattern(new Image(getClass().getResource("/images/info/Gold.png").toExternalForm()));
        gold.setFill(goldImage);
        bar.getChildren().add(gold);
        Text goldAmount=new Text("Gold:  "+civilizationController.getCurrentPlayer().getGold());
        goldAmount.setY(45);
        goldAmount.getStyleClass().add("info");
        bar.getChildren().add(goldAmount);
        Circle happiness=new Circle(30);
        happiness.setCenterY(15);
        ImagePattern happinessImage=new ImagePattern(new Image(getClass().getResource("/images/info/Happiness.png").toExternalForm()));
        happiness.setFill(happinessImage);
        bar.getChildren().add(happiness);
        Text happinessAmount=new Text("Happiness:  "+civilizationController.getCurrentPlayer().getHappiness());
        happinessAmount.setY(45);
        happinessAmount.getStyleClass().add("info");
        bar.getChildren().add(happinessAmount);
        Circle science=new Circle(30);
        science.setCenterY(15);
        ImagePattern scienceImage=new ImagePattern(new Image(getClass().getResource("/images/info/Science.png").toExternalForm()));
        science.setFill(scienceImage);
        bar.getChildren().add(science);
        Text scienceAmount=new Text("Science:  "+civilizationController.getCurrentPlayer().totalCup());
        scienceAmount.setY(45);
        scienceAmount.getStyleClass().add("info");
        bar.getChildren().add(scienceAmount);
        VBox nextTurnVBox=new VBox();
        Button nextTurn=new Button("Next Turn");
        nextTurn.getStyleClass().add("primary-btn");
        nextTurn.setMaxWidth(100);
        nextTurnVBox.getChildren().add(nextTurn);
        nextTurn.setOnMouseClicked(mouseEvent -> {
            String output=civilizationController.nextTurn();
            if(output.equals("ok")) App.changeMenu("Game");
            else {
                nextTurnError.setText(output);
                nextTurnError.setFill(Color.rgb(50,100,200));
                if(!nextTurnVBox.getChildren().contains(nextTurnError)){
                    nextTurnVBox.getChildren().add(nextTurnError);
                }
            }
        });
        bar.getChildren().add(nextTurnVBox);
        tileMap.setOnKeyPressed(this::move);
        tiles=new GraphicTile[civilizationController.getMapHeight()][civilizationController.getMapWidth()];
        Tile[][] modelTiles= civilizationController.getTiles();
        for(double y=0,i=0;i<civilizationController.getMapHeight();y+=size*Math.sqrt(3),i+=1)
        {
            for(double x=size/2,dy=y,j=0;j<civilizationController.getMapWidth();x+=(3.0/2.0)*size,j+=1)
            {
                GraphicTile tile = new GraphicTile(x,dy,
                        x+size,dy,
                        x+size*(3.0/2.0),dy+size*v,
                        x+size,dy+size*Math.sqrt(3),
                        x,dy+size*Math.sqrt(3),
                        x-(size/2.0),dy+size*v,(int)i+","+(int)j,modelTiles[(int)i][(int)j],tileMap,civilizationController);
                ImagePattern imagePattern=new ImagePattern(new Image(getClass().getResource("/images/tile/"+modelTiles[(int)i][(int)j].getTerrain().getName()+".png").toExternalForm()));
                tile.setFill(imagePattern);
                if(modelTiles[(int) i][(int) j].getVisibilityForUser(civilizationController.getTurn()).equals("fog of war")){
                    ImagePattern fog=new ImagePattern(new Image(getClass().getResource("/images/tile/Fog.png").toExternalForm()));
                    tile.setFill(fog);
                }
                tile.setStroke(Color.rgb(15, 65, 135));
                tile.setStrokeWidth(4);
                tileMap.getChildren().add(tile);
                if(modelTiles[(int) i][(int) j].getFeature()!=null && !modelTiles[(int) i][(int) j].getVisibilityForUser(civilizationController.getTurn()).equals("fog of war")){
                    ImagePattern imagePattern1=new ImagePattern(new Image(getClass().getResource("/images/tile/"+modelTiles[(int)i][(int)j].getFeature().getName()+".png").toExternalForm()));
                    tile.getFeature().setFill(imagePattern1);
                    tileMap.getChildren().add(tile.getFeature());
                }
                tileMap.getChildren().add(tile.getLocation());
                tiles[(int) i][(int) j]=tile;
                VBox infos=new VBox();
                infos.setMinHeight(60);
                infos.setMinWidth(200);
                BackgroundImage backgroundImage1 = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        backgroundSize);
                infos.setBackground(new Background(backgroundImage1));
                if(tile.getTile().getResource()!=null){
                    HBox resourceHBox=new HBox();
                    Rectangle resource=new Rectangle();
                    ImagePattern resourceImage=new ImagePattern(new Image(getClass().getResource("/images/Resources/"+tile.getTile().getResource().getName()+".png").toExternalForm()));
                    resource.setHeight(100);
                    resource.setWidth(100);
                    resource.setFill(resourceImage);
                    resourceHBox.getChildren().add(resource);
                    Text resourceName=new Text("    Resource: "+tile.getTile().getResource().getName());
                    Text resourceValues=new Text("    Resource Values: Gold: "+tile.getTile().getResource().getGold()+"  Food: "+tile.getTile().getResource().getFood()+"   Production: "+tile.getTile().getResource().getProduction());
                    resourceName.setFill(Color.WHITE);
                    resourceName.getStyleClass().add("tileInfo");
                    resourceValues.setFill(Color.WHITE);
                    resourceValues.getStyleClass().add("tileInfo");
                    VBox resourceInfo=new VBox();
                    resourceInfo.getChildren().add(resourceName);
                    resourceInfo.getChildren().add(resourceValues);
                    resourceHBox.getChildren().add(resourceInfo);
                    infos.getChildren().add(resourceHBox);
                }
                Text tileInfo=new Text("Tile Values: Gold: "+tile.getTile().getGold()+"  Production:  "+tile.getTile().getProduction()+"  Food:  "+tile.getTile().getFood());
                tileInfo.setFill(Color.WHITE);
                tileInfo.getStyleClass().add("tileInfo");
                infos.getChildren().add(tileInfo);
                tile.setOnMouseClicked(mouseEvent -> {
                    if (tileInformation != null) {
                        tileMap.getChildren().remove(tileInformation);
                    }
                    if(!tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("fog of war")) {
                        if(!(tileInformation!=null&&tileInformation.equals(infos))) {
                            tileMap.getChildren().add(infos);
                            tileInformation = infos;
                        }else {
                            tileInformation=null;
                        }
                    }else tileInformation=null;
                });
                if(tile.getTile().getFeature()!=null){
                    tile.getFeature().setOnMouseClicked(mouseEvent -> {
                        if (tileInformation != null) {
                            tileMap.getChildren().remove(tileInformation);
                        }
                        if(!tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("fog of war")) {
                            if(!(tileInformation!=null&&tileInformation.equals(infos))) {
                                tileMap.getChildren().add(infos);
                                tileInformation = infos;
                            }else {
                                tileInformation=null;
                            }
                        }else tileInformation=null;
                    });
                }
                if(tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("visible")) {
                    if (unitController.getTileNonCombatUnit((int) i, (int) j) != null) {
                        Unit unit=unitController.getTileNonCombatUnit((int) i, (int) j);
                        tile.addUnit(unit);
                        VBox unitInfo=new VBox();
                        unitInfo.setBackground(new Background(backgroundImage1));
                        HBox unitHBox=new HBox();
                        Rectangle unitPicture=new Rectangle();
                        unitPicture.setHeight(100);
                        unitPicture.setWidth(100);
                        ImagePattern unitImage=new ImagePattern(new Image(getClass().getResource("/images/unitIcon/"+unit.getName()+".png").toExternalForm()));
                        unitPicture.setFill(unitImage);
                        unitHBox.getChildren().add(unitPicture);
                        Text unitName=new Text("  "+unit.getName());
                        unitName.setFill(Color.WHITE);
                        unitName.getStyleClass().add("tileInfo");
                        unitHBox.getChildren().add(unitName);
                        unitInfo.getChildren().add(unitHBox);
                        Rectangle graphicUnit = tile.getGraphicUnits().get(unit);
                        graphicUnit.setOnMouseClicked(mouseEvent -> {
                            if(tileInformation!=null){
                                tileMap.getChildren().remove(tileInformation);
                                tileInformation=null;
                            }
                            if (unitInformation != null) {
                                tileMap.getChildren().remove(unitInformation);
                            }
                            if(!(unitInformation!=null&&unitInformation.equals(unitInfo))) {
                                tileMap.getChildren().add(unitInfo);
                                unitInformation = unitInfo;
                            }else {
                                unitInformation=null;
                            }
                            if(graphicUnit.getOpacity()==1){
                                graphicUnit.setOpacity(0.5);
                            }else graphicUnit.setOpacity(1);
                        });
                    }
                    if (unitController.getTileCombatUnit((int) i, (int) j) != null) {
                        tile.addUnit(unitController.getTileCombatUnit((int) i, (int) j));
                        Unit unit=unitController.getTileCombatUnit((int) i, (int) j);
                        tile.addUnit(unit);
                        VBox unitInfo=new VBox();
                        unitInfo.setBackground(new Background(backgroundImage1));
                        HBox unitHBox=new HBox();
                        Rectangle unitPicture=new Rectangle();
                        unitPicture.setHeight(100);
                        unitPicture.setWidth(100);
                        ImagePattern unitImage=new ImagePattern(new Image(getClass().getResource("/images/unitIcon/"+unit.getName()+".png").toExternalForm()));
                        unitPicture.setFill(unitImage);
                        unitHBox.getChildren().add(unitPicture);
                        Text unitName=new Text("  "+unit.getName());
                        unitName.setFill(Color.WHITE);
                        unitName.getStyleClass().add("tileInfo");
                        unitHBox.getChildren().add(unitName);
                        unitInfo.getChildren().add(unitHBox);
                        Rectangle graphicUnit = tile.getGraphicUnits().get(unit);
                        graphicUnit.setOnMouseClicked(mouseEvent -> {
                            if(tileInformation!=null){
                                tileMap.getChildren().remove(tileInformation);
                                tileInformation=null;
                            }
                            if (unitInformation != null) {
                                tileMap.getChildren().remove(unitInformation);
                            }
                            if(!(unitInformation!=null&&unitInformation.equals(unitInfo))) {
                                tileMap.getChildren().add(unitInfo);
                                unitInformation = unitInfo;
                            }else {
                                unitInformation=null;
                            }
                            if(graphicUnit.getOpacity()==1){
                                graphicUnit.setOpacity(0.5);
                            }else graphicUnit.setOpacity(1);
                        });
                    }
                }
                dy = dy==y ? dy+size*v : y;
            }
        }
    }

    public void move(KeyEvent keyEvent){
        if(tileInformation!=null){
            tileMap.getChildren().remove(tileInformation);
            tileInformation=null;
        }
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
                tiles[i][j].move(dx,dy);
            }
        }
    }
}
