package view_graphic;

import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.SettlerUnit;
import view_graphic.component.GraphicTile;
import model.Tile;
import model.Unit;

public class Game {
    @FXML
    private AnchorPane tileMap;
    @FXML
    private HBox bar;
    private GraphicTile[][] tiles;
    private double size = 100, v = Math.sqrt(3) / 2.0;
    public static CivilizationController civilizationController;
    private UnitController unitController;
    private CityController cityController;
    private VBox tileInformation;
    private VBox unitInformation;
    private VBox vBoxError=new VBox();
    private Text error = new Text();
    private Timeline timelineError=new Timeline();

    public void initialize() {
        Timeline focusTimeline = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> {
            tileMap.requestFocus();
        }));
        focusTimeline.setCycleCount(-1);
        focusTimeline.play();
        unitController = civilizationController.getUnitController();
        cityController = civilizationController.getCityController();
        bar.setMinHeight(70);
        bar.setMinWidth(1280);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        createTopBar(backgroundSize);
        vBoxError.getChildren().add(error);
        vBoxError.setLayoutX(900);
        vBoxError.setLayoutY(20);
        vBoxError.setBackground(bar.getBackground());
        error.setFill(Color.rgb(250, 250, 0));
        error.getStyleClass().add("vBoxError");
        timelineError.getKeyFrames().add(new KeyFrame(Duration.seconds(2)));
        timelineError.setOnFinished(actionEvent -> {
            tileMap.getChildren().remove(vBoxError);
        });
        tileMap.setOnKeyPressed(this::move);
        tiles = new GraphicTile[civilizationController.getMapHeight()][civilizationController.getMapWidth()];
        Tile[][] modelTiles = civilizationController.getTiles();
        int i = 0;
        for (double y = 0; i < civilizationController.getMapHeight(); y += size * Math.sqrt(3), i++) {
            int j = 0;
            for (double x = size / 2, dy = y; j < civilizationController.getMapWidth(); x += (3.0 / 2.0) * size, j++) {
                GraphicTile tile = new GraphicTile(x, dy,
                        x + size, dy,
                        x + size * (3.0 / 2.0), dy + size * v,
                        x + size, dy + size * Math.sqrt(3),
                        x, dy + size * Math.sqrt(3),
                        x - (size / 2.0), dy + size * v, modelTiles[i][j], tileMap, civilizationController);

                tiles[i][j] = tile;
                int finalI = i;
                int finalJ = j;
                Polygon select = tile;
                if (tile.getTile().getFeature() != null) select = tile.getFeature();
                select.setOnMouseClicked(mouseEvent -> {
                    if (unitController.getSelectedUnit() != null) {
                        int x1 = unitController.getSelectedUnit().getX(), y1 = unitController.getSelectedUnit().getY();
                        String output = unitController.moveSelectedUnit(finalI, finalJ);
                        if (output.equals("unit is moving")) {
                            tiles[x1][y1].deleteUnit(unitController.getSelectedUnit());
                            tileMap.getChildren().remove(unitInformation);
                            GameController.setSelectedUnit(null);
                            reBuildTiles();
                        } else {
                            error.setText(output);
                            tileMap.getChildren().add(vBoxError);
                            timelineError.play();
                            tiles[unitController.getSelectedUnit().getX()][unitController.getSelectedUnit().getY()].getGraphicUnits().get(unitController.getSelectedUnit()).setOpacity(1);
                            tileMap.getChildren().remove(unitInformation);
                            GameController.setSelectedUnit(null);
                        }
                    } else {
                        VBox infos = tile.getInfosBox(backgroundSize);
                        if (tileInformation != null) {
                            tileMap.getChildren().remove(tileInformation);
                        }
                        if (tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("visible")) {
                            if (!(tileInformation != null && tileInformation.equals(infos))) {
                                tileMap.getChildren().add(infos);
                                tileInformation = infos;
                            } else {
                                tileInformation = null;
                            }
                        } else tileInformation = null;
                    }
                });
                Unit unit;
                if (!tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("fog of war") && (unitController.getTileNonCombatUnit(i, j) != null || unitController.getTileCombatUnit(i, j) != null)) {
                    if (unitController.getTileNonCombatUnit(i, j) != null) {
                        unit = unitController.getTileNonCombatUnit(i, j);
                    } else {
                        unit = unitController.getTileCombatUnit(i, j);
                    }
                    tile.addUnit(unit);
                    selectUnit(unit, tile);
                }
                dy = dy == y ? dy + size * v : y;
            }
        }
    }



    private void createTopBar(BackgroundSize backgroundSize) {
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/topBar.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        bar.setBackground(new Background(backgroundImage));
        bar.setSpacing(30);
        Text userNickname = new Text(civilizationController.getCurrentPlayer().getNickname());
        userNickname.setY(45);
        userNickname.getStyleClass().add("info");
        bar.getChildren().add(userNickname);
        Circle gold = new Circle(30);
        gold.setCenterY(15);
        ImagePattern goldImage = new ImagePattern(new Image(getClass().getResource("/images/info/Gold.png").toExternalForm()));
        gold.setFill(goldImage);
        bar.getChildren().add(gold);
        Text goldAmount = new Text("Gold:  " + civilizationController.getCurrentPlayer().getGold());
        goldAmount.setY(45);
        goldAmount.getStyleClass().add("info");
        bar.getChildren().add(goldAmount);
        Circle happiness = new Circle(30);
        happiness.setCenterY(15);
        ImagePattern happinessImage = new ImagePattern(new Image(getClass().getResource("/images/info/Happiness.png").toExternalForm()));
        happiness.setFill(happinessImage);
        bar.getChildren().add(happiness);
        Text happinessAmount = new Text("Happiness:  " + civilizationController.getCurrentPlayer().getHappiness());
        happinessAmount.setY(45);
        happinessAmount.getStyleClass().add("info");
        bar.getChildren().add(happinessAmount);
        Circle science = new Circle(30);
        science.setCenterY(15);
        ImagePattern scienceImage = new ImagePattern(new Image(getClass().getResource("/images/info/Science.png").toExternalForm()));
        science.setFill(scienceImage);
        bar.getChildren().add(science);
        Text scienceAmount = new Text("Science:  " + civilizationController.getCurrentPlayer().totalCup());
        scienceAmount.setY(45);
        scienceAmount.getStyleClass().add("info");
        bar.getChildren().add(scienceAmount);
        VBox nextTurnVBox = new VBox();
        Button nextTurn = new Button("Next Turn");
        nextTurn.getStyleClass().add("primary-btn");
        nextTurn.setMaxWidth(100);
        nextTurnVBox.getChildren().add(nextTurn);
        nextTurn.setOnMouseClicked(mouseEvent -> {
            String output = civilizationController.nextTurn();
            if (output.startsWith("it")) App.changeMenu("Game");
            else {
                error.setText(output);
                tileMap.getChildren().add(vBoxError);
                timelineError.play();
            }
        });
        bar.getChildren().add(nextTurnVBox);
    }

    public void move(KeyEvent keyEvent) {
        if (tileInformation != null) {
            tileMap.getChildren().remove(tileInformation);
            tileInformation = null;
        }
        double dx = 0, dy = 0;
        if (keyEvent.getCode().getName().equals("Right") && tiles[civilizationController.getMapHeight() - 1][civilizationController.getMapWidth() - 1].getPoints().get(4) > 1280) {
            dx = -10.0;
            dy = 0.0;
        }
        if (keyEvent.getCode().getName().equals("Left") && tiles[0][0].getPoints().get(10) < 0) {
            dx = 10.0;
            dy = 0.0;
        }
        if (keyEvent.getCode().getName().equals("Up") && tiles[0][0].getPoints().get(1) < 0) {
            dx = 0.0;
            dy = 10.0;
        }
        if (keyEvent.getCode().getName().equals("Down") && tiles[civilizationController.getMapHeight() - 1][1].getPoints().get(7) > 620) {
            dx = 0.0;
            dy = -10.0;
        }
        for (int i = 0; i < civilizationController.getMapHeight(); i++) {
            for (int j = 0; j < civilizationController.getMapWidth(); j++) {
                tiles[i][j].move(dx, dy);
            }
        }
    }

    public void reBuildTiles() {
        for (int i = 0; i < civilizationController.getMapHeight(); i++) {
            for (int j = 0; j < civilizationController.getMapWidth(); j++) {
                tiles[i][j].reBuildTile();
                Unit unit;
                if (!tiles[i][j].getTile().getVisibilityForUser(civilizationController.getTurn()).equals("fog of war") && (unitController.getTileNonCombatUnit(i, j) != null || unitController.getTileCombatUnit(i, j) != null)) {
                    if (unitController.getTileNonCombatUnit(i, j) != null) {
                        unit = unitController.getTileNonCombatUnit(i, j);
                    } else {
                        unit = unitController.getTileCombatUnit(i, j);
                    }
                    if(!tiles[i][j].getGraphicUnits().containsKey(unit)) {
                        tiles[i][j].addUnit(unit);
                        selectUnit(unit, tiles[i][j]);
                    }
                }
            }
        }
    }

    public void selectUnit(Unit unit, GraphicTile tile) {
        if (tile.getTile().getVisibilityForUser(civilizationController.getTurn()).equals("visible")) {
            Rectangle graphicUnit = tile.getGraphicUnits().get(unit);
            Unit finalUnit = unit;
            graphicUnit.setOnMouseClicked(mouseEvent -> {
                VBox unitInfo = new VBox();
                BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
                BackgroundImage backgroundImage1 = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        backgroundSize);
                unitInfo.setBackground(new Background(backgroundImage1));
                HBox unitHBox = new HBox();
                Rectangle unitPicture = new Rectangle();
                unitPicture.setHeight(100);
                unitPicture.setWidth(100);
                ImagePattern unitImage = new ImagePattern(new Image(getClass().getResource("/images/unitIcon/" + finalUnit.getName() + ".png").toExternalForm()));
                unitPicture.setFill(unitImage);
                unitHBox.getChildren().add(unitPicture);
                VBox unitValues = new VBox();
                Text unitName = new Text("  " + finalUnit.getName());
                unitName.setFill(Color.WHITE);
                unitName.getStyleClass().add("tileInfo");
                Text unitSpec = new Text("  State: " + finalUnit.getState() + "  Health: " + finalUnit.getHealth() + "  Remaining Moves: " + finalUnit.getRemainingMoves());
                unitSpec.setFill(Color.WHITE);
                unitSpec.getStyleClass().add("tileInfo");
                unitValues.getChildren().add(unitName);
                unitValues.getChildren().add(unitSpec);
                unitHBox.getChildren().add(unitValues);
                unitInfo.getChildren().add(unitHBox);
                Button sleep = new Button("Sleep");
                sleep.getStyleClass().add("secondary-btn");
                sleep.setMaxWidth(50);
                sleep.setOnMouseClicked(mouseEvent1 -> {
                    unitController.sleep();
                });
                unitHBox.getChildren().add(sleep);
                if(unit instanceof SettlerUnit){
                    Button foundCity=new Button("Found City");
                    foundCity.getStyleClass().add("secondary-btn");
                    foundCity.setMaxWidth(80);
                    foundCity.setOnMouseClicked(mouseEvent1 -> {
                        String output=unitController.foundCity();
                        if(output.equals("city founded")){
                            tile.deleteUnit(unitController.getSelectedUnit());
                            tileMap.getChildren().remove(unitInformation);
                            GameController.setSelectedUnit(null);
                            reBuildTiles();
                        }
                            error.setText(output);
                            tileMap.getChildren().add(vBoxError);
                            timelineError.play();
                    });
                    unitHBox.getChildren().add(foundCity);
                }
                if (tileInformation != null) {
                    tileMap.getChildren().remove(tileInformation);
                    tileInformation = null;
                }
                if (graphicUnit.getOpacity() == 1) {
                    GameController.setSelectedUnit(finalUnit);
                    graphicUnit.setOpacity(0.5);
                    if (unitInformation != null) {
                        tileMap.getChildren().remove(unitInformation);
                    }
                    tileMap.getChildren().add(unitInfo);
                    unitInformation = unitInfo;
                } else {
                    GameController.setSelectedUnit(null);
                    graphicUnit.setOpacity(1);
                    tileMap.getChildren().remove(unitInformation);
                    unitInformation = null;
                }
            });
        }
    }
}
