package view_graphic;

import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import enums.Commands;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.*;
import view_graphic.component.GraphicTile;

import java.util.regex.Matcher;

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
    private VBox cityPanel;
    private boolean putCitizenToTile;
    private boolean deleteCitizen;
    private boolean purchaseTile;

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
        vBoxError.setLayoutX(600);
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
                Matcher matcher;
                String input=cheatDialog.getEditor().getText();
                if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_GOLD)) != null) {
                    civilizationController.getCurrentPlayer().setGold(civilizationController.getCurrentPlayer().getGold() + Integer.parseInt(matcher.group("amount")));
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_TURN)) != null){
                    String message = "";
                    for(int i=0;i<Integer.parseInt(matcher.group("amount"));i++){
                        message = civilizationController.nextTurn();
                    }
                    showMessage(message);
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_HAPPINESS)) != null)
                    civilizationController.getCurrentPlayer().setHappiness(civilizationController.getCurrentPlayer().getHappiness()+Integer.parseInt(matcher.group("amount")));
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_UNIT_FULL_HEALTH)) != null){
                    for (Unit unit : civilizationController.getCurrentPlayer().getUnits()) {
                        unit.setHealth(10);
                    }
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CITY_FULL_HEALTH)) != null){
                    for (City city : civilizationController.getCurrentPlayer().getCities()) {
                        city.setHealth(20);
                    }
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_INCREASE_CITIZEN)) != null){
                    for (City city : civilizationController.getCurrentPlayer().getCities()) {
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
                    for (Unit unit : civilizationController.getCurrentPlayer().getUnits()) {
                        unit.setRemainingMoves(unit.getRemainingMoves()+Integer.parseInt(matcher.group("amount")));
                    }
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CREATE_CITY)) != null) {
                    cityController.createCity(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")));
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_FINISH_STUDY)) != null){
                    civilizationController.getCurrentPlayer().addTechnology(civilizationController.getCurrentPlayer().getCurrentStudy());
                    civilizationController.getCurrentPlayer().getWaitedTechnologies().remove(civilizationController.getCurrentPlayer().getCurrentStudy().getName());
                    civilizationController.getCurrentPlayer().setCurrentStudy(null);
                }
                else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_UNIT)) != null){
                    for (City city : civilizationController.getCurrentPlayer().getCities()) {
                        if(city.getId()==Integer.parseInt(matcher.group("id"))){
                            cityController.createUnit(matcher.group("name"),city);
                        }
                    }
                }
                bar.getChildren().clear();
                createTopBar(backgroundSize);
                reBuildTiles();
            }else {
                move(keyEvent);
            }
        });
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
                            showMessage(output);
                            tiles[unitController.getSelectedUnit().getX()][unitController.getSelectedUnit().getY()].getGraphicUnits().get(unitController.getSelectedUnit()).setOpacity(1);
                            tileMap.getChildren().remove(unitInformation);
                            GameController.setSelectedUnit(null);
                        }
                    } else {
                        if(!putCitizenToTile) {
                            if (!deleteCitizen) {
                                if(!purchaseTile) {
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
                showMessage(output);
            }
        });
        bar.getChildren().add(nextTurnVBox);
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
                    for (City city : civilizationController.getCurrentPlayer().getCities()) {
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
                            bar.getChildren().clear();
                            createTopBar(backgroundSize);
                        }
                            showMessage(output);
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

    public GraphicTile getGraphicByModel(Tile tile){
        for(int i=0;i<civilizationController.getMapWidth();i++){
            for(int j=0;j<civilizationController.getMapHeight();j++){
                if(tiles[i][j].getTile().equals(tile)){
                    return tiles[i][j];
                }
            }
        }
        return null;
    }

    public void showMessage(String message){
        error.setText(message);
        tileMap.getChildren().add(vBoxError);
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
        purchaseTileButton.setOnMouseClicked(mouseEvent -> {
            putCitizenToTile=false;
            deleteCitizen=false;
            eliminateCitizen.setOpacity(1);
            putCitizen.setOpacity(1);
            if(purchaseTile) purchaseTileButton.setOpacity(1);
            else purchaseTileButton.setOpacity(0.5);
            purchaseTile = !purchaseTile;
        });
        putCitizen.setOnMouseClicked(mouseEvent -> {
            deleteCitizen=false;
            purchaseTile=false;
            purchaseTileButton.setOpacity(1);
            eliminateCitizen.setOpacity(1);
            if(putCitizenToTile) putCitizen.setOpacity(1);
            else putCitizen.setOpacity(0.5);
            putCitizenToTile = !putCitizenToTile;
        });
        eliminateCitizen.setOnMouseClicked(mouseEvent -> {
            putCitizenToTile=false;
            purchaseTile=false;
            purchaseTileButton.setOpacity(1);
            putCitizen.setOpacity(1);
            if(deleteCitizen) eliminateCitizen.setOpacity(1);
            else eliminateCitizen.setOpacity(0.5);
            deleteCitizen=!deleteCitizen;
        });
        Button productionButton =new Button("Production Panel");
        productionButton.setMaxWidth(200);
        productionButton.getStyleClass().add("secondary-btn");
        cityPanel.getChildren().add(productionButton);
        productionButton.setOnMouseClicked(mouseEvent -> {
            purchaseTile=false;
            putCitizenToTile=false;
            deleteCitizen=false;
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
        });
    }

}
