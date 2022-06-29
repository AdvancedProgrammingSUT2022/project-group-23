package view_graphic;

import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import database.ImprovementDatabase;
import database.TechnologyDatabase;
import enums.Commands;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.*;
import view_graphic.component.GraphicTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Game {
    @FXML
    private AnchorPane tileMap;
    @FXML
    private HBox bar;
    private GraphicTile[][] tiles;
    private double size = 100, v = Math.sqrt(3) / 2.0;
    private static CivilizationController civilizationController;
    private UnitController unitController;
    private CityController cityController;
    private VBox tileInformation;
    private VBox unitInformation;
    private VBox vBoxError=new VBox();
    private Text error = new Text();
    private Timeline timelineError=new Timeline();
    private VBox cityPanel;
    private ScrollPane technologyPanel;
    private boolean putCitizenToTile;
    private boolean deleteCitizen;
    private boolean purchaseTile;
    private boolean unitAttack;
    private boolean cityAttack;

    public void initialize() {
        Timeline focusTimeline = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> {
            tileMap.requestFocus();
        }));
        focusTimeline.setCycleCount(-1);
        focusTimeline.play();
        KeyCombination keyCombination=new KeyCodeCombination(KeyCode.C,KeyCombination.SHIFT_DOWN,KeyCombination.CONTROL_DOWN);
        unitController = civilizationController.getUnitController();
        cityController = civilizationController.getCityController();
        bar.setMinHeight(70);
        bar.setMinWidth(1280);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        createTopBar(backgroundSize);
        vBoxError.getChildren().add(error);
        vBoxError.setLayoutX(0);
        vBoxError.setLayoutY(20);
        vBoxError.setBackground(bar.getBackground());
        error.setFill(Color.rgb(250, 250, 0));
        error.getStyleClass().add("vBoxError");
        timelineError.getKeyFrames().add(new KeyFrame(Duration.seconds(2)));
        timelineError.setOnFinished(actionEvent -> {
            tileMap.getChildren().remove(vBoxError);
        });
        tileMap.setOnKeyPressed(keyEvent -> {
            if(keyCombination.match(keyEvent)){
                TextInputDialog cheatDialog=new TextInputDialog("enter cheat code");
                cheatDialog.setTitle("Cheat Code");
                cheatDialog.showAndWait();
                cheat(cheatDialog.getEditor().getText(),backgroundSize);
            }else {
                move(keyEvent);
            }
        });
        tiles = new GraphicTile[GameController.getMapHeight()][GameController.getMapWidth()];
        Tile[][] modelTiles = civilizationController.getTiles();
        int i = 0;
        for (double y = 0; i < GameController.getMapHeight(); y += size * Math.sqrt(3), i++) {
            int j = 0;
            for (double x = size / 2, dy = y; j < GameController.getMapWidth(); x += (3.0 / 2.0) * size, j++) {
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
                    if (GameController.getSelectedUnit() != null) {
                        if(!unitAttack) {
                            int x1 = GameController.getSelectedUnit().getX(), y1 = GameController.getSelectedUnit().getY();
                            String output = unitController.moveSelectedUnit(finalI, finalJ);
                            if (output.equals("unit is moving")) {
                                tiles[x1][y1].deleteUnit(GameController.getSelectedUnit());
                                tileMap.getChildren().remove(unitInformation);
                                GameController.setSelectedUnit(null);
                                reBuildTiles(backgroundSize);
                            } else {
                                showMessage(output);
                                tiles[GameController.getSelectedUnit().getX()][GameController.getSelectedUnit().getY()].getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
                                tileMap.getChildren().remove(unitInformation);
                                GameController.setSelectedUnit(null);
                            }
                        }else {
                            String output="";
                            int x1 =GameController.getSelectedUnit().getX(), y1 =GameController.getSelectedUnit().getY();
                            City city=cityController.getCityAtCoordinate(tile.getTile().getX(),tile.getTile().getY());
                            if(city==null)showMessage("There is no city on this tile!");
                            else {
                                if(!unitController.checkSelectedUnit().equals("ok")){
                                    showMessage(unitController.checkSelectedUnit());
                                }else {
                                        if (GameController.getSelectedUnit().getRemainingMoves() == 0) {
                                            showMessage("this unit can't attack now!");
                                        } else {
                                            ArrayList<City> reachableCities=unitController.reachableCities();
                                            if(!reachableCities.contains(city)){
                                                showMessage("you can't attack this city");
                                            }else {
                                                output=unitController.attackCity(city);
                                                showMessage(output);
                                            }
                                        }
                                }
                            }
                            unitAttack=false;
                            if(output.equals("your unit died!")){
                                tiles[x1][y1].deleteUnit(GameController.getSelectedUnit());
                            }
                            if(output.equals("dominated")){
                                VBox which=new VBox();
                                Text whichOne=new Text("you dominated this city, do you want to eliminate it or annex it?");
                                whichOne.getStyleClass().add("info");
                                which.getChildren().add(whichOne);
                                Button eliminate=new Button("Eliminate");
                                eliminate.getStyleClass().add("secondary-btn");
                                which.getChildren().add(eliminate);
                                Button annex =new Button("Annex");
                                annex.getStyleClass().add("secondary-btn");
                                which.getChildren().add(annex);
                                unitInformation.getChildren().add(which);
                                eliminate.setOnMouseClicked(mouseEvent1 -> {
                                    cityController.getCityOwner(city).getCities().remove(city);
                                    showMessage("city eliminated!");
                                    tileMap.getChildren().remove(unitInformation);
                                    tiles[x1][y1].getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
                                    GameController.setSelectedUnit(null);
                                    reBuildTiles(backgroundSize);
                                    bar.getChildren().clear();
                                    createTopBar(backgroundSize);
                                });
                                annex.setOnMouseClicked(mouseEvent1 -> {
                                    cityController.getCityOwner(city).getCities().remove(city);
                                    city.setHealth(20);
                                    GameController.getCurrentPlayer().setHappiness(GameController.getCurrentPlayer().getHappiness() - 5);
                                    GameController.getCurrentPlayer().getCities().add(city);
                                    showMessage("you annexed this city to your cities!");
                                    tileMap.getChildren().remove(unitInformation);
                                    tiles[x1][y1].getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
                                    GameController.setSelectedUnit(null);
                                    reBuildTiles(backgroundSize);
                                    bar.getChildren().clear();
                                    createTopBar(backgroundSize);
                                });
                            }else {
                                tileMap.getChildren().remove(unitInformation);
                                tiles[x1][y1].getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
                                GameController.setSelectedUnit(null);
                            }
                            reBuildTiles(backgroundSize);
                            bar.getChildren().clear();
                            createTopBar(backgroundSize);
                        }
                    } else {
                        if(!putCitizenToTile) {
                            if (!deleteCitizen) {
                                if(!purchaseTile) {
                                    if(tileInformation==null){
                                        tileInformation=tile.getInfosBox(backgroundSize);
                                        tileMap.getChildren().add(tileInformation);
                                    }else {
                                        tileMap.getChildren().remove(tileInformation);
                                        VBox tileInfos=tile.getInfosBox(backgroundSize);
                                     if(tileInfos.getLayoutX()!=tileInformation.getLayoutX()) {
                                         tileInformation = tileInfos;
                                         tileMap.getChildren().add(tileInformation);
                                     }else tileInformation=null;
                                    }
                                    for (City city : GameController.getCurrentPlayer().getCities()) {
                                        if(tile.getTile().equals(city.getCapital())) {
                                            if (GameController.getSelectedCity() == null) {
                                                GameController.setSelectedCity(city);
                                                for (Tile tile1 : city.getTiles()) {
                                                    getGraphicByModel(tile1).setOpacity(0.7);
                                                    if (tile1.getFeature() != null)
                                                        getGraphicByModel(tile1).getFeature().setOpacity(0.7);
                                                }
                                                fillCityPanel(city);
                                                tileMap.getChildren().add(cityPanel);
                                            } else {
                                                GameController.setSelectedCity(null);
                                                for (Tile tile1 : city.getTiles()) {
                                                    getGraphicByModel(tile1).setOpacity(1);
                                                    if (tile1.getFeature() != null)
                                                        getGraphicByModel(tile1).getFeature().setOpacity(1);
                                                }
                                                tileMap.getChildren().remove(cityPanel);
                                            }
                                        }
                                    }
                                }else {
                                    showMessage(cityController.purchaseTile(tile.getTile()));
                                    tileMap.getChildren().remove(cityPanel);
                                    fillCityPanel(GameController.getSelectedCity());
                                    tileMap.getChildren().add(cityPanel);
                                    purchaseTile=false;
                                }
                            }else {
                                showMessage(cityController.removeCitizen(finalI,finalJ));
                                tileMap.getChildren().remove(cityPanel);
                                fillCityPanel(GameController.getSelectedCity());
                                tileMap.getChildren().add(cityPanel);
                                deleteCitizen=false;
                            }
                        }else {
                            showMessage(cityController.putCitizenToWork(finalI, finalJ));
                            tileMap.getChildren().remove(cityPanel);
                            fillCityPanel(GameController.getSelectedCity());
                            tileMap.getChildren().add(cityPanel);
                            putCitizenToTile=false;
                        }
                    }
                });
                Unit unit;
                if (!tile.getTile().getVisibilityForUser(GameController.getTurn()).equals("fog of war") && (unitController.getTileNonCombatUnit(i, j) != null || unitController.getTileCombatUnit(i, j) != null)) {
                    if (unitController.getTileNonCombatUnit(i, j) != null) {
                        unit = unitController.getTileNonCombatUnit(i, j);
                    } else {
                        unit = unitController.getTileCombatUnit(i, j);
                    }
                    tile.addUnit(unit);
                    selectUnit(unit, tile,backgroundSize);
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
        Text userNickname = new Text(GameController.getCurrentPlayer().getNickname());
        userNickname.setY(45);
        userNickname.getStyleClass().add("info");
        bar.getChildren().add(userNickname);
        Circle gold = new Circle(30);
        gold.setCenterY(15);
        ImagePattern goldImage = new ImagePattern(new Image(getClass().getResource("/images/info/Gold.png").toExternalForm()));
        gold.setFill(goldImage);
        bar.getChildren().add(gold);
        Text goldAmount = new Text("Gold:  " + GameController.getCurrentPlayer().getGold());
        goldAmount.setY(45);
        goldAmount.getStyleClass().add("info");
        bar.getChildren().add(goldAmount);
        Circle happiness = new Circle(30);
        happiness.setCenterY(15);
        ImagePattern happinessImage = new ImagePattern(new Image(getClass().getResource("/images/info/Happiness.png").toExternalForm()));
        happiness.setFill(happinessImage);
        bar.getChildren().add(happiness);
        Text happinessAmount = new Text("Happiness:  " + GameController.getCurrentPlayer().getHappiness());
        happinessAmount.setY(45);
        happinessAmount.getStyleClass().add("info");
        bar.getChildren().add(happinessAmount);
        Circle science = new Circle(30);
        science.setCenterY(15);
        ImagePattern scienceImage = new ImagePattern(new Image(getClass().getResource("/images/info/Science.png").toExternalForm()));
        science.setFill(scienceImage);
        bar.getChildren().add(science);
        Text scienceAmount = new Text("Science:  " + GameController.getCurrentPlayer().totalCup());
        scienceAmount.setY(45);
        scienceAmount.getStyleClass().add("info");
        bar.getChildren().add(scienceAmount);
        if(!GameController.getCurrentPlayer().getCities().isEmpty()){
            Button technologyPanelButton = new Button("Technology Panel");
            technologyPanelButton.getStyleClass().add("primary-btn");
            technologyPanelButton.setMaxWidth(150);
            technologyPanelButton.setOnMouseClicked(mouseEvent -> {
                if(tileMap.getChildren().contains(technologyPanel)){
                    tileMap.getChildren().remove(technologyPanel);
                    technologyPanel=null;
                }else {
                    fillTechnologyPanel(backgroundSize);
                    tileMap.getChildren().add(technologyPanel);
                }
            });
            bar.getChildren().add(technologyPanelButton);
        }
        Button nextTurn = new Button("Next Turn");
        nextTurn.getStyleClass().add("primary-btn");
        nextTurn.setMaxWidth(100);
        nextTurn.setOnMouseClicked(mouseEvent -> {
            String output = civilizationController.nextTurn();
            if (output.startsWith("it")) App.changeMenu("Game");
            else {
                showMessage(output);
            }
        });
        bar.getChildren().add(nextTurn);
    }

    public void move(KeyEvent keyEvent) {
        if (tileInformation != null) {
            tileMap.getChildren().remove(tileInformation);
            tileInformation = null;
        }
        if(GameController.getSelectedCity()!=null){
            for (Tile tile : GameController.getSelectedCity().getTiles()) {
                getGraphicByModel(tile).setOpacity(1);
                if(tile.getFeature()!=null) getGraphicByModel(tile).getFeature().setOpacity(1);
            }
            GameController.setSelectedCity(null);
            tileMap.getChildren().remove(cityPanel);
            putCitizenToTile=false;
            purchaseTile=false;
            deleteCitizen=false;
        }
        double dx = 0, dy = 0;
        if (keyEvent.getCode().getName().equals("Right") && tiles[GameController.getMapHeight() - 1][GameController.getMapWidth() - 1].getPoints().get(4) > 1280) {
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
        if (keyEvent.getCode().getName().equals("Down") && tiles[GameController.getMapHeight() - 1][1].getPoints().get(7) > 620) {
            dx = 0.0;
            dy = -10.0;
        }
        for (int i = 0; i < GameController.getMapHeight(); i++) {
            for (int j = 0; j < GameController.getMapWidth(); j++) {
                tiles[i][j].move(dx, dy);
            }
        }
    }

    public void reBuildTiles(BackgroundSize backgroundSize) {
        for (int i = 0; i < GameController.getMapHeight(); i++) {
            for (int j = 0; j < GameController.getMapWidth(); j++) {
                tiles[i][j].reBuildTile();
                    for (City city : GameController.getCurrentPlayer().getCities()) {
                        if(city.getCapital().equals(tiles[i][j].getTile())){
                            Polygon select=tiles[i][j];
                            if(tiles[i][j].getTile().getFeature()!=null) select=tiles[i][j].getFeature();
                            select.setOnMouseClicked(mouseEvent -> {
                                if(GameController.getSelectedCity()==null){
                                    GameController.setSelectedCity(city);
                                for (Tile tile : city.getTiles()) {
                                    getGraphicByModel(tile).setOpacity(0.7);
                                    if (tile.getFeature() != null) getGraphicByModel(tile).getFeature().setOpacity(0.7);
                                }
                                fillCityPanel(city);
                                tileMap.getChildren().add(cityPanel);
                                }else {
                                    GameController.setSelectedCity(null);
                                    for (Tile tile : city.getTiles()) {
                                        getGraphicByModel(tile).setOpacity(1);
                                        if (tile.getFeature() != null) getGraphicByModel(tile).getFeature().setOpacity(1);
                                    }
                                    tileMap.getChildren().remove(cityPanel);
                                }
                            });
                        }
                    }
                Unit unit;
                if (!tiles[i][j].getTile().getVisibilityForUser(GameController.getTurn()).equals("fog of war") && (unitController.getTileNonCombatUnit(i, j) != null || unitController.getTileCombatUnit(i, j) != null)) {
                    if (unitController.getTileNonCombatUnit(i, j) != null) {
                        unit = unitController.getTileNonCombatUnit(i, j);
                    } else {
                        unit = unitController.getTileCombatUnit(i, j);
                    }
                    if(!tiles[i][j].getGraphicUnits().containsKey(unit)) {
                        tiles[i][j].addUnit(unit);
                        selectUnit(unit, tiles[i][j],backgroundSize);
                    }
                    selectUnit(unit,tiles[i][j],backgroundSize);
                }
            }
        }
    }

    public void selectUnit(Unit unit, GraphicTile tile,BackgroundSize backgroundSize) {
        if (tile.getTile().getVisibilityForUser(GameController.getTurn()).equals("visible")) {
            Rectangle graphicUnit = tile.getGraphicUnits().get(unit);
            graphicUnit.setOnMouseClicked(mouseEvent -> {
                if(!unitAttack) {
                    if(!cityAttack) {
                        if (GameController.getCurrentPlayer().getUnits().contains(unit)) {
                            fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                        }
                    }else {
                        ArrayList<Unit> reachableUnits=cityController.reachableUnits();
                        String output="";
                        if(!reachableUnits.contains(unit)){
                            showMessage("you can't attack this unit!");
                        }else {
                            output=cityController.attackUnit(unit);
                            showMessage(output);
                        }
                        tileMap.getChildren().remove(cityPanel);
                        cityPanel=null;
                        for (Tile tile1 : GameController.getSelectedCity().getTiles()) {
                            getGraphicByModel(tile1).setOpacity(1);
                            if(tile1.getFeature()!=null) getGraphicByModel(tile1).getFeature().setOpacity(1);
                        }
                        if(output.equals("you killed the unit!")){
                            tiles[unit.getX()][unit.getY()].deleteUnit(unit);
                        }
                        GameController.setSelectedCity(null);
                        reBuildTiles(backgroundSize);
                        purchaseTile=false;
                        deleteCitizen=false;
                        putCitizenToTile=false;
                        cityAttack=false;
                    }
                }else {
                    ArrayList<Unit> reachableUnits=unitController.reachableUnits();
                    int x=GameController.getSelectedUnit().getX(),y=GameController.getSelectedUnit().getY();
                    String output = "";
                    if(!reachableUnits.contains(unit)){
                        showMessage("You Can't Attack This Unit");
                    }else {
                        if(!unitController.checkSelectedUnit().equals("ok")){
                            showMessage(unitController.checkSelectedUnit());
                        }else {
                            if (GameController.getSelectedUnit().getRemainingMoves() == 0) {
                                showMessage("this unit can't attack now!");
                            } else {
                                output = unitController.attackUnit(unit);
                                showMessage(output);
                            }
                        }
                    }
                    unitAttack=false;
                    tileMap.getChildren().remove(unitInformation);
                    tiles[x][y].getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
                    if(output.equals("your unit died!")){
                        tiles[x][y].deleteUnit(GameController.getSelectedUnit());
                    }
                    if(output.equals("you killed the unit")){
                        MilitaryUnit militaryUnit=(MilitaryUnit) GameController.getSelectedUnit();
                        tile.deleteUnit(unit);
                        if (militaryUnit.getRange() == -1) {
                            tiles[x][y].deleteUnit(GameController.getSelectedUnit());
                            tile.addUnit(GameController.getSelectedUnit());
                        }
                    }
                    GameController.setSelectedUnit(null);
                    reBuildTiles(backgroundSize);
                    bar.getChildren().clear();
                    createTopBar(backgroundSize);
                }
            });
        }
    }

    public Button createUnitActionButton(String actionName){
        Button actionButton = new Button();
        ImageView buttonImageView = new ImageView(new Image(getClass().getResource("/images/unitActions/"+ actionName +".png").toExternalForm()));
        buttonImageView.setFitWidth(30);
        buttonImageView.setFitHeight(30);
        actionButton.setGraphic(buttonImageView);
        actionButton.getStyleClass().add("secondary-btn");
        actionButton.setMaxWidth(30);
        Tooltip actionTooltip = new Tooltip(actionName);
        actionButton.setTooltip(actionTooltip);
        return actionButton;
    }
    public void fillUnitInformation(GraphicTile tile,Unit unit,Rectangle graphicUnit,BackgroundSize backgroundSize){
        VBox unitInfo = new VBox();
        unitInfo.setAlignment(Pos.CENTER);
        unitInfo.setSpacing(15);
        BackgroundImage backgroundImage1 = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        unitInfo.setBackground(new Background(backgroundImage1));
        HBox unitHBox = new HBox();
        Rectangle unitPicture = new Rectangle();
        unitPicture.setHeight(100);
        unitPicture.setWidth(100);
        ImagePattern unitImage = new ImagePattern(new Image(getClass().getResource("/images/unitIcon/" + unit.getName() + ".png").toExternalForm()));
        unitPicture.setFill(unitImage);
        unitHBox.getChildren().add(unitPicture);
        VBox unitValues = new VBox();
        Text unitName = new Text("  " + unit.getName());
        unitName.setFill(Color.WHITE);
        unitName.getStyleClass().add("tileInfo");
        Text unitSpec = new Text("  State: " + unit.getState() + "  Health: " + unit.getHealth() + "  Remaining Moves: " + unit.getRemainingMoves());
        unitSpec.setFill(Color.WHITE);
        unitSpec.getStyleClass().add("tileInfo");
        unitValues.getChildren().add(unitName);
        unitValues.getChildren().add(unitSpec);
        unitHBox.getChildren().add(unitValues);
        unitInfo.getChildren().add(unitHBox);
        VBox firstColumnActions=new VBox();
        VBox secondColumnActions=new VBox();
        Button sleep = createUnitActionButton("Sleep");
        sleep.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.sleep());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        firstColumnActions.getChildren().add(sleep);
        Button wake = createUnitActionButton("Wake");
        wake.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.wake());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        firstColumnActions.getChildren().add(wake);
        Button fortify = createUnitActionButton("Fortify");
        fortify.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.fortify());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        firstColumnActions.getChildren().add(fortify);
        Button garrison = createUnitActionButton("Garrison");
        garrison.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.garrison());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        firstColumnActions.getChildren().add(garrison);
        Button alert = createUnitActionButton("Alert");
        alert.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.alert());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        secondColumnActions.getChildren().add(alert);
        Button range = createUnitActionButton("SetupRange");
        range.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.rangeSetup());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        secondColumnActions.getChildren().add(range);

        Button cancelAction = createUnitActionButton("CancelAction");
        cancelAction.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.cancelActions());
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        secondColumnActions.getChildren().add(cancelAction);

        Button deleteUnit = createUnitActionButton("DeleteUnit");
        deleteUnit.setOnMouseClicked(mouseEvent1 -> {
            showMessage(unitController.deleteSelectedUnit(true));
            tileMap.getChildren().remove(unitInformation);
            tile.getGraphicUnits().get(GameController.getSelectedUnit()).setOpacity(1);
            tile.deleteUnit(GameController.getSelectedUnit());
            GameController.setSelectedUnit(null);
            reBuildTiles(backgroundSize);
        });
        secondColumnActions.getChildren().add(deleteUnit);



        unitHBox.getChildren().add(firstColumnActions);
        unitHBox.getChildren().add(secondColumnActions);
        if (unit instanceof MilitaryUnit) {
            VBox militaryActions=new VBox();
            Button attack = new Button("Attack");
            attack.getStyleClass().add("secondary-btn");
            attack.setMaxWidth(100);
            attack.setOnMouseClicked(mouseEvent1 -> {
                if (unitAttack) attack.setOpacity(1);
                else attack.setOpacity(0.5);
                unitAttack = !unitAttack;
            });
            militaryActions.getChildren().add(attack);
            Button loot = createUnitActionButton("Pillage");
            loot.setOnMouseClicked(mouseEvent1 -> {
                showMessage(unitController.lootTile());
            });
            militaryActions.getChildren().add(loot);
            unitHBox.getChildren().add(militaryActions);
        }
        if (unit instanceof SettlerUnit) {
            Button foundCity = createUnitActionButton("FoundCity");
            foundCity.setOnMouseClicked(mouseEvent1 -> {
                String output = unitController.foundCity();
                if (output.equals("city founded")) {
                    tile.deleteUnit(GameController.getSelectedUnit());
                    tileMap.getChildren().remove(unitInformation);
                    GameController.setSelectedUnit(null);
                    reBuildTiles(backgroundSize);
                    bar.getChildren().clear();
                    createTopBar(backgroundSize);
                }
                showMessage(output);
            });
            unitHBox.getChildren().add(foundCity);
        }
        if(unit instanceof WorkerUnit){
            VBox workerActions=new VBox();
            Button improve=new Button("Improve Tile");
            improve.getStyleClass().add("secondary-btn");
            improve.setMaxWidth(100);
            workerActions.getChildren().add(improve);
            improve.setOnMouseClicked(mouseEvent1 -> {
                unitInformation.getChildren().clear();
                HashMap<Tile,Improvement> waitedImprovement=GameController.getCurrentPlayer().getImprovingTiles();
                HashMap<Tile,Integer> processingTiles=GameController.getCurrentPlayer().getProcessingTiles();
                ArrayList<Tile> eliminatingFeatures=GameController.getCurrentPlayer().getEliminatingFeatures();
                String currentWork="";
                int totalTurn=0;
                if(waitedImprovement.get(tile.getTile())==null && processingTiles.get(tile.getTile())!=null && !eliminatingFeatures.contains(tile.getTile())){
                    currentWork="Building Road";
                    totalTurn=3;
                }else {
                    if(processingTiles.get(tile.getTile())==null){
                        currentWork="no work!";
                    }
                    if(waitedImprovement.get(tile.getTile())!=null){
                        if(eliminatingFeatures.contains(tile.getTile())){
                            currentWork=waitedImprovement.get(tile.getTile()).getName()+" + eliminating "+tile.getTile().getFeature().getName();
                            totalTurn=12;
                        }else {
                            currentWork=waitedImprovement.get(tile.getTile()).getName();
                            if(tile.getTile().getLootedImprovement()==null)
                            totalTurn=6;
                            else totalTurn=3;
                        }
                    }else if(eliminatingFeatures.contains(tile.getTile())){
                        currentWork="eliminating "+tile.getTile().getFeature().getName();
                        totalTurn=6;
                    }
                }
                ProgressBar progressBar=new ProgressBar();
                Text currentWorkText=new Text("Current Improvement: "+currentWork);
                currentWorkText.getStyleClass().add("info");
                unitInformation.getChildren().add(currentWorkText);
                if(processingTiles.get(tile.getTile())!=null){
                    double progress=1.0-((processingTiles.get(tile.getTile())).doubleValue()/totalTurn);
                    if(progress==0) progress=0.1;
                    progressBar.setProgress(progress);
                    unitInformation.getChildren().add(progressBar);
                }
                Button roadButton=new Button("Road");
                roadButton.getStyleClass().add("secondary-btn");
                roadButton.setMinWidth(100);
                unitInformation.getChildren().add(roadButton);
                roadButton.setOnMouseClicked(mouseEvent2 -> {
                    showMessage(unitController.buildRoad("Road"));
                    unitInformation.getChildren().clear();
                    fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                });
                for (Improvement improvement : ImprovementDatabase.getImprovements()) {
                    Button improvementButton=new Button(improvement.getName());
                    improvementButton.getStyleClass().add("secondary-btn");
                    improvementButton.setMinWidth(70);
                    unitInformation.getChildren().add(improvementButton);
                    improvementButton.setOnMouseClicked(mouseEvent2 -> {
                        showMessage(unitController.improveTile(improvement));
                        unitInformation.getChildren().clear();
                        fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                    });
                }
                Button back =new Button("Back");
                back.getStyleClass().add("secondary-btn");
                back.setMinWidth(100);
                unitInformation.getChildren().add(back);
                back.setOnMouseClicked(mouseEvent2 -> {
                    unitInformation.getChildren().clear();
                    fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                });
            });
            Button eliminate =new Button("Eliminate");
            eliminate.getStyleClass().add("secondary-btn");
            eliminate.setMaxWidth(100);
            workerActions.getChildren().add(eliminate);
            eliminate.setOnMouseClicked(mouseEvent1 -> {
                unitInformation.getChildren().clear();
                Text which=new Text("Choose one to eliminate:");
                which.getStyleClass().add("info");
                unitInformation.getChildren().add(which);
                Button road=new Button("Road");
                road.getStyleClass().add("secondary-btn");
                unitInformation.getChildren().add(road);
                road.setOnMouseClicked(mouseEvent2 -> {
                    showMessage(unitController.eliminateRoad());
                    unitInformation.getChildren().clear();
                    fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                });
                Button feature =new Button("Feature");
                feature.getStyleClass().add("secondary-btn");
                unitInformation.getChildren().add(feature);
                feature.setOnMouseClicked(mouseEvent2 -> {
                    showMessage(unitController.eliminateFeature());
                    unitInformation.getChildren().clear();
                    fillUnitInformation(tile,unit,graphicUnit,backgroundSize);
                });
            });
            Button heal =new Button("Heal Tile");
            heal.getStyleClass().add("secondary-btn");
            heal.setMaxWidth(100);
            workerActions.getChildren().add(heal);
            heal.setOnMouseClicked(mouseEvent1 -> {
                showMessage(unitController.healTile());
            });
            unitHBox.getChildren().add(workerActions);
        }
        if (tileInformation != null) {
            tileMap.getChildren().remove(tileInformation);
            tileInformation = null;
        }
        if (graphicUnit.getOpacity() == 1) {
            GameController.setSelectedUnit(unit);
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
    }

    public GraphicTile getGraphicByModel(Tile tile){
        for(int i=0;i<GameController.getMapWidth();i++){
            for(int j=0;j<GameController.getMapHeight();j++){
                if(tiles[i][j].getTile().equals(tile)){
                    return tiles[i][j];
                }
            }
        }
        return null;
    }

    public void showMessage(String message){
        timelineError.stop();
        error.setText(message);
        if(!tileMap.getChildren().contains(vBoxError)) tileMap.getChildren().add(vBoxError);
        timelineError.play();
    }

    public void fillCityPanel(City city){
        cityPanel=new VBox();
        cityPanel.setAlignment(Pos.CENTER);
        if(getGraphicByModel(city.getCapital()).getPoints().get(0)<600){
            cityPanel.setLayoutX(800);
        }
        cityPanel.setSpacing(10);
        cityPanel.setMinWidth(400);
        cityPanel.setMinHeight(600);
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        cityPanel.setBackground(new Background(backgroundImage));
        Text cityId = new Text("City "+city.getId()+" belong to "+cityController.getCityOwner(city).getNickname());
        cityId.getStyleClass().add("info");
        cityPanel.getChildren().add(cityId);
        HBox goldHBox=new HBox();
        goldHBox.setSpacing(20);
        Circle gold = new Circle(30);
        gold.setCenterY(15);
        ImagePattern goldImage = new ImagePattern(new Image(getClass().getResource("/images/info/Gold.png").toExternalForm()));
        gold.setFill(goldImage);
        goldHBox.getChildren().add(gold);
        Text goldAmount = new Text("Gold:  " + city.gold());
        goldAmount.setY(45);
        goldAmount.getStyleClass().add("info");
        goldHBox.getChildren().add(goldAmount);
        cityPanel.getChildren().add(goldHBox);
        HBox productionHBox =new HBox();
        productionHBox.setSpacing(20);
        Circle production = new Circle(30);
        production.setCenterY(15);
        ImagePattern productionImage = new ImagePattern(new Image(getClass().getResource("/images/info/Production.png").toExternalForm()));
        production.setFill(productionImage);
        productionHBox.getChildren().add(production);
        Text productionAmount = new Text("Production:  " + city.production());
        productionAmount.setY(45);
        productionAmount.getStyleClass().add("info");
        productionHBox.getChildren().add(productionAmount);
        cityPanel.getChildren().add(productionHBox);
        HBox FoodHBox =new HBox();
        FoodHBox.setSpacing(20);
        Circle food = new Circle(30);
        food.setCenterY(15);
        ImagePattern foodImage = new ImagePattern(new Image(getClass().getResource("/images/info/Food.png").toExternalForm()));
        food.setFill(foodImage);
        FoodHBox.getChildren().add(food);
        Text foodAmount = new Text("Food:  " + city.totalFood());
        foodAmount.setY(45);
        foodAmount.getStyleClass().add("info");
        FoodHBox.getChildren().add(foodAmount);
        cityPanel.getChildren().add(FoodHBox);
        Text health=new Text("Remaining Health: "+city.getHealth());
        health.getStyleClass().add("info");
        cityPanel.getChildren().add(health);
        Button purchaseTileButton =new Button("Purchase Tile");
        purchaseTileButton.setMaxWidth(150);
        purchaseTileButton.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(purchaseTileButton);
        Button putCitizen=new Button("Put Citizen To Tile");
        putCitizen.setMaxWidth(150);
        putCitizen.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(putCitizen);
        Button eliminateCitizen =new Button("Eliminate Citizen From Tile");
        eliminateCitizen.setMaxWidth(200);
        eliminateCitizen.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(eliminateCitizen);
        Button attack =new Button("Attack Unit");
        attack.setMaxWidth(200);
        attack.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(attack);
        purchaseTileButton.setOnMouseClicked(mouseEvent -> {
            putCitizenToTile=false;
            deleteCitizen=false;
            cityAttack=false;
            attack.setOpacity(1);
            putCitizen.setOpacity(1);
            attack.setOpacity(1);
            if(purchaseTile) purchaseTileButton.setOpacity(1);
            else purchaseTileButton.setOpacity(0.5);
            purchaseTile = !purchaseTile;
        });
        putCitizen.setOnMouseClicked(mouseEvent -> {
            deleteCitizen=false;
            purchaseTile=false;
            cityAttack=false;
            attack.setOpacity(1);
            purchaseTileButton.setOpacity(1);
            attack.setOpacity(1);
            if(putCitizenToTile) putCitizen.setOpacity(1);
            else putCitizen.setOpacity(0.5);
            putCitizenToTile = !putCitizenToTile;
        });
        eliminateCitizen.setOnMouseClicked(mouseEvent -> {
            putCitizenToTile=false;
            purchaseTile=false;
            cityAttack=false;
            attack.setOpacity(1);
            purchaseTileButton.setOpacity(1);
            putCitizen.setOpacity(1);
            if(deleteCitizen) eliminateCitizen.setOpacity(1);
            else eliminateCitizen.setOpacity(0.5);
            deleteCitizen=!deleteCitizen;
        });
        attack.setOnMouseClicked(mouseEvent -> {
            putCitizenToTile=false;
            purchaseTile=false;
            deleteCitizen=false;
            eliminateCitizen.setOpacity(1);
            purchaseTileButton.setOpacity(1);
            putCitizen.setOpacity(1);
            if(cityAttack) attack.setOpacity(1);
            else attack.setOpacity(0.5);
            cityAttack=!cityAttack;
        });
        Button unitProductionButton =new Button("Unit Production Panel");
        unitProductionButton.setMaxWidth(200);
        unitProductionButton.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(unitProductionButton);
        unitProductionButton.setOnMouseClicked(mouseEvent -> {
            purchaseTile=false;
            putCitizenToTile=false;
            deleteCitizen=false;
            cityAttack=false;
            cityPanel.getChildren().clear();
            Text currentProduction=new Text("Your Current Production: "+city.getConstructingUnit());
            currentProduction.getStyleClass().add("info");
            cityPanel.getChildren().add(currentProduction);
            Text text =new Text("You Can Constrict these units:");
            text.getStyleClass().add("info");
            cityPanel.getChildren().add(text);
            for (int i=0;i<unitController.getConstructableUnits().size();i++){
                Button unit=new Button(unitController.getConstructableUnits().get(i).getName());
                unit.getStyleClass().add("secondary-btn");
                unit.setMaxWidth(200);
                cityPanel.getChildren().add(unit);
                int finalI = i;
                unit.setOnMouseClicked(mouseEvent1 -> {
                    showMessage(cityController.constructUnit(unitController.getConstructableUnits().get(finalI).getName()));
                    tileMap.getChildren().remove(cityPanel);
                    fillCityPanel(city);
                    tileMap.getChildren().add(cityPanel);
                });
            }
            Button back =new Button("Back");
            back.getStyleClass().add("secondary-btn");
            back.setMaxWidth(200);
            cityPanel.getChildren().add(back);
            back.setOnMouseClicked(mouseEvent1 -> {
                tileMap.getChildren().remove(cityPanel);
                fillCityPanel(city);
                tileMap.getChildren().add(cityPanel);
            });
        });
        Button buildingProductionButton =new Button("Building Production Panel");
        buildingProductionButton.setMaxWidth(200);
        buildingProductionButton.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(buildingProductionButton);
        buildingProductionButton.setOnMouseClicked(mouseEvent -> {
            purchaseTile=false;
            putCitizenToTile=false;
            deleteCitizen=false;
            cityAttack=false;
            cityPanel.getChildren().clear();
            Text currentProduction=new Text("Your Current Production: "+city.getConstructingBuilding());
            currentProduction.getStyleClass().add("info");
            cityPanel.getChildren().add(currentProduction);
            if(!city.getBuildings().isEmpty()) {
                Text buildingsText=new Text("Your Have These Buildings:");
                buildingsText.getStyleClass().add("info");
                cityPanel.getChildren().add(buildingsText);
                HBox buildings = new HBox();
                buildings.setAlignment(Pos.CENTER);
                for (Building building : city.getBuildings()) {
                    Rectangle rectangle=new Rectangle();
                    rectangle.setWidth(80);
                    rectangle.setHeight(80);
                    ImagePattern buildingImage = new ImagePattern(new Image(getClass().getResource("/images/buildings/"+building.getName()+".png").toExternalForm()));
                    rectangle.setFill(buildingImage);
                    buildings.getChildren().add(rectangle);
                }
                cityPanel.getChildren().add(buildings);
            }
            Text text =new Text("You Can Constrict these buildings:");
            text.getStyleClass().add("info");
            cityPanel.getChildren().add(text);
            for (int i=0;i<cityController.constructableBuildingsForSelectedCity().size();i++){
                Button building =new Button(cityController.constructableBuildingsForSelectedCity().get(i).getName());
                building.getStyleClass().add("secondary-btn");
                building.setMaxWidth(200);
                cityPanel.getChildren().add(building);
                int finalI = i;
                building.setOnMouseClicked(mouseEvent1 -> {
                    showMessage(cityController.constructBuilding(cityController.constructableBuildingsForSelectedCity().get(finalI).getName()));
                    tileMap.getChildren().remove(cityPanel);
                    fillCityPanel(city);
                    tileMap.getChildren().add(cityPanel);
                });
            }
            Button back =new Button("Back");
            back.getStyleClass().add("secondary-btn");
            back.setMaxWidth(200);
            cityPanel.getChildren().add(back);
            back.setOnMouseClicked(mouseEvent1 -> {
                tileMap.getChildren().remove(cityPanel);
                fillCityPanel(city);
                tileMap.getChildren().add(cityPanel);
            });
        });
        Button close=new Button("Close Panel");
        close.setMaxWidth(150);
        close.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(close);
        close.setOnMouseClicked(mouseEvent -> {
            tileMap.getChildren().remove(cityPanel);
            cityPanel=null;
            for (Tile tile : city.getTiles()) {
                getGraphicByModel(tile).setOpacity(1);
                if(tile.getFeature()!=null) getGraphicByModel(tile).getFeature().setOpacity(1);
            }
            GameController.setSelectedCity(null);
            purchaseTile=false;
            deleteCitizen=false;
            putCitizenToTile=false;
            cityAttack=false;
        });
    }

    public void cheat(String input,BackgroundSize backgroundSize){
        Matcher matcher;
        if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_GOLD)) != null) {
            GameController.getCurrentPlayer().setGold(GameController.getCurrentPlayer().getGold() + Integer.parseInt(matcher.group("amount")));
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_TURN)) != null){
            String message = "";
            for(int i=0;i<Integer.parseInt(matcher.group("amount"));i++){
                message = civilizationController.nextTurn();
            }
            showMessage(message);
            App.changeMenu("Game");
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_HAPPINESS)) != null)
            GameController.getCurrentPlayer().setHappiness(GameController.getCurrentPlayer().getHappiness()+Integer.parseInt(matcher.group("amount")));
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_UNIT_FULL_HEALTH)) != null){
            for (Unit unit : GameController.getCurrentPlayer().getUnits()) {
                unit.setHealth(10);
            }
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CITY_FULL_HEALTH)) != null){
            for (City city : GameController.getCurrentPlayer().getCities()) {
                city.setHealth(20);
            }
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_INCREASE_CITIZEN)) != null){
            for (City city : GameController.getCurrentPlayer().getCities()) {
                if(city.getId()==Integer.parseInt(matcher.group("id"))){
                    city.setCountOfCitizens(city.getCountOfCitizens()+Integer.parseInt(matcher.group("amount")));
                    break;
                }
            }
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_ROAD)) != null) {
            civilizationController.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))].setRoad(true);
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_INCREASE_MOVEMENT)) != null){
            for (Unit unit : GameController.getCurrentPlayer().getUnits()) {
                unit.setRemainingMoves(unit.getRemainingMoves()+Integer.parseInt(matcher.group("amount")));
            }
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CREATE_CITY)) != null) {
            cityController.createCity(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")));
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_FINISH_STUDY)) != null){
            GameController.getCurrentPlayer().addTechnology(GameController.getCurrentPlayer().getCurrentStudy());
            GameController.getCurrentPlayer().getWaitedTechnologies().remove(GameController.getCurrentPlayer().getCurrentStudy().getName());
            GameController.getCurrentPlayer().setCurrentStudy(null);
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_UNIT)) != null){
            for (City city : GameController.getCurrentPlayer().getCities()) {
                if(city.getId()==Integer.parseInt(matcher.group("id"))){
                    cityController.createUnit(matcher.group("name"),city);
                }
            }
        }
        else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_BUILDING)) != null){
            for (City city : GameController.getCurrentPlayer().getCities()) {
                if(city.getId()==Integer.parseInt(matcher.group("id"))){
                    cityController.createBuilding(matcher.group("name"),city);
                }
            }
        }
        bar.getChildren().clear();
        createTopBar(backgroundSize);
        reBuildTiles(backgroundSize);
    }

    public void fillTechnologyPanel(BackgroundSize backgroundSize){
        Pane scroll=new Pane();
        technologyPanel=new ScrollPane();
        technologyPanel.setMinHeight(400);
        technologyPanel.setPrefWidth(1280);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/loginBackground.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                backgroundSize);
        Platform.runLater(()->scroll.setBackground(new Background(backgroundImage)));
        HashMap<Technology,Rectangle> technologies=new HashMap<>();
        int j;
        for(int i=0;i<13;i++) {
            j=0;
            for (Technology technology : TechnologyDatabase.getTechnologies()) {
                if(technology.getTreePlace()==i) {
                    Rectangle graphicTechnology = new Rectangle();
                    graphicTechnology.setHeight(40);
                    graphicTechnology.setWidth(200);
                    ImagePattern technologyImage=new ImagePattern(new Image(getClass().getResource("/images/technologyTree/"+technology.getName()+".png").toExternalForm()));
                    graphicTechnology.setFill(technologyImage);
                    technologies.put(technology,graphicTechnology);
                    graphicTechnology.setLayoutX(i*225);
                    graphicTechnology.setLayoutY(j*55);
                    scroll.getChildren().add(graphicTechnology);
                    j++;
                    graphicTechnology.setOnMouseClicked(mouseEvent -> {
                        if(GameController.getCurrentPlayer().readyTechnologies().contains(technology)) {
                            tileMap.getChildren().remove(technologyPanel);
                            technologyPanel = null;
                            civilizationController.studyTechnology(technology);
                            showMessage("studying technology!");
                        }else {
                            if(GameController.getCurrentPlayer().getCurrentStudy()!=null && GameController.getCurrentPlayer().getCurrentStudy().equals(technology)){
                                showMessage("you are already studying this technology!");
                            }else
                            showMessage("you have to study it's prerequisite technologies first!");
                        }
                    });
                }
            }
        }
        Text currentStudy=new Text();
        if(GameController.getCurrentPlayer().getCurrentStudy()!=null){
            int turnsLeft;
            if (GameController.getCurrentPlayer().getWaitedTechnologies().get(GameController.getCurrentPlayer().getCurrentStudy().getName()) % GameController.getCurrentPlayer().totalCup() == 0) {
                turnsLeft =  GameController.getCurrentPlayer().getWaitedTechnologies().get(GameController.getCurrentPlayer().getCurrentStudy().getName()) / GameController.getCurrentPlayer().totalCup();
            } else {
                turnsLeft = GameController.getCurrentPlayer().getWaitedTechnologies().get(GameController.getCurrentPlayer().getCurrentStudy().getName()) / GameController.getCurrentPlayer().totalCup() + 1;
            }
            currentStudy.setText("you are currently studying " + GameController.getCurrentPlayer().getCurrentStudy().getName() + " and there is " + turnsLeft + " turns left to unlock");
        }else {
            currentStudy.setText("no current study!");
        }
        currentStudy.getStyleClass().add("title");
        currentStudy.setFill(Color.rgb(30,200,80));
        currentStudy.setX(0);
        currentStudy.setY(370);
        scroll.getChildren().add(currentStudy);
        technologyPanel.setContent(scroll);
        for (Technology technology : TechnologyDatabase.getTechnologies()) {
            for (String prerequisiteTechnology : technology.getPrerequisiteTechnologies()) {
                Line line=new Line();
                Platform.runLater(()->line.setStartX(225*TechnologyDatabase.getTechnologyByName(prerequisiteTechnology).getTreePlace()+200));
                Platform.runLater(()->line.setStartY(technologies.get(TechnologyDatabase.getTechnologyByName(prerequisiteTechnology)).getLayoutY()));
                Platform.runLater(()->line.setEndX(technology.getTreePlace()*225));
                Platform.runLater(()->line.setEndY(technologies.get(technology).getLayoutY()));
                Platform.runLater(()->line.setStroke(Color.GREEN));
                Platform.runLater(()->scroll.getChildren().add(line));
            }
        }
    }

    public static CivilizationController getCivilizationController() {
        return civilizationController;
    }

    public static void setCivilizationController(CivilizationController civilizationController) {
        Game.civilizationController = civilizationController;
    }
}
