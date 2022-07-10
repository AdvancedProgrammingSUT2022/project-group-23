package view_graphic.component;

import controller.CivilizationController;
import controller.GameController;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.City;
import model.Tile;
import model.Unit;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class GraphicTile extends Polygon {
    private Text location;
    private Polygon feature;
    private Tile tile;
    private AnchorPane tileMap;
    private HashMap<Unit, Rectangle> graphicUnits = new HashMap<>();
    private CivilizationController civilizationController;
    private VBox infos;

    private static Lighting lighting = new Lighting();

    static {
        lighting.setDiffuseConstant(0.8);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
    }

    public GraphicTile(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x5, double y5, double x6, double y6, Tile tile, AnchorPane tileMap, CivilizationController civilizationController) {
        this.tile = tile;
        this.tileMap = tileMap;
        this.civilizationController = civilizationController;
        this.getPoints().addAll(x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6);
        this.location = new Text(this.getPoints().get(0), this.getPoints().get(1) + 15, tile.getX() + "," + tile.getY());
        setTerrainBackgroundImage();
        this.setStroke(Color.rgb(15, 65, 135));
        this.setStrokeWidth(4);
        tileMap.getChildren().add(this);
        addFeature();
        checkCapital();
        tileMap.getChildren().add(location);


    }

    public void reBuildTile(){
        setTerrainBackgroundImage();
        this.setStroke(Color.rgb(15, 65, 135));
        this.setStrokeWidth(4);
        if (tile.getFeature() != null && !tile.getVisibilityForUser(GameController.getTurn()).equals("fog of war")) {
            ImagePattern imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/" + tile.getFeature().getName() + ".png").toExternalForm()));
            feature.setFill(imagePattern);
            if (tile.getVisibilityForUser(GameController.getTurn()).equals("revealed"))
                feature.setEffect(lighting);
            else feature.setEffect(null);
        }
        if(!tile.getVisibilityForUser(GameController.getTurn()).equals("fog of war"))
        checkCapital();
    }

    public Text getLocation() {
        return location;
    }

    public Polygon getFeature() {
        return feature;
    }

    public void move(double dx, double dy) {
        this.getPoints().set(0, (this.getPoints().get(0) + dx));
        this.getPoints().set(1, (this.getPoints().get(1) + dy));
        this.getPoints().set(2, (this.getPoints().get(2) + dx));
        this.getPoints().set(3, (this.getPoints().get(3) + dy));
        this.getPoints().set(4, (this.getPoints().get(4) + dx));
        this.getPoints().set(5, (this.getPoints().get(5) + dy));
        this.getPoints().set(6, (this.getPoints().get(6) + dx));
        this.getPoints().set(7, (this.getPoints().get(7) + dy));
        this.getPoints().set(8, (this.getPoints().get(8) + dx));
        this.getPoints().set(9, (this.getPoints().get(9) + dy));
        this.getPoints().set(10, (this.getPoints().get(10) + dx));
        this.getPoints().set(11, (this.getPoints().get(11) + dy));
        this.getFeature().getPoints().set(0, (this.getFeature().getPoints().get(0) + dx));
        this.getFeature().getPoints().set(1, (this.getFeature().getPoints().get(1) + dy));
        this.getFeature().getPoints().set(2, (this.getFeature().getPoints().get(2) + dx));
        this.getFeature().getPoints().set(3, (this.getFeature().getPoints().get(3) + dy));
        this.getFeature().getPoints().set(4, (this.getFeature().getPoints().get(4) + dx));
        this.getFeature().getPoints().set(5, (this.getFeature().getPoints().get(5) + dy));
        this.getFeature().getPoints().set(6, (this.getFeature().getPoints().get(6) + dx));
        this.getFeature().getPoints().set(7, (this.getFeature().getPoints().get(7) + dy));
        this.getFeature().getPoints().set(8, (this.getFeature().getPoints().get(8) + dx));
        this.getFeature().getPoints().set(9, (this.getFeature().getPoints().get(9) + dy));
        this.getFeature().getPoints().set(10, (this.getFeature().getPoints().get(10) + dx));
        this.getFeature().getPoints().set(11, (this.getFeature().getPoints().get(11) + dy));
        this.getLocation().setX(this.getLocation().getX() + dx);
        this.getLocation().setY(this.getLocation().getY() + dy);
        for (Map.Entry<Unit, Rectangle> entry : this.graphicUnits.entrySet()) {
            this.graphicUnits.get(entry.getKey()).setX(this.graphicUnits.get(entry.getKey()).getX() + dx);
            this.graphicUnits.get(entry.getKey()).setY(this.graphicUnits.get(entry.getKey()).getY() + dy);
        }
        if (this.getPoints().get(1) < 0) {
            tileMap.getChildren().remove(this);
            tileMap.getChildren().remove(this.location);
            tileMap.getChildren().remove(this.feature);
            for (Map.Entry<Unit, Rectangle> entry : this.graphicUnits.entrySet()) {
                tileMap.getChildren().remove(this.graphicUnits.get(entry.getKey()));
            }
        } else if (!tileMap.getChildren().contains(this)) {
            tileMap.getChildren().add(this);
            tileMap.getChildren().add(this.location);
            if (this.tile.getFeature() != null && !this.tile.getVisibilityForUser(GameController.getTurn()).equals("fog of war")) {
                tileMap.getChildren().add(this.feature);
            }
            for (Map.Entry<Unit, Rectangle> entry : this.graphicUnits.entrySet()) {
                tileMap.getChildren().add(this.graphicUnits.get(entry.getKey()));
            }
        }
    }

    public void setTerrainBackgroundImage() {
        ImagePattern imagePattern;
        if (tile.getVisibilityForUser(GameController.getTurn()).equals("fog of war"))
            imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/Fog.png").toExternalForm()));
        else if (tile.getVisibilityForUser(GameController.getTurn()).equals("revealed")) {
            imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/" + tile.getTerrain().getName() + ".png").toExternalForm()));
            this.setEffect(lighting);
        } else {
            imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/" + tile.getTerrain().getName() + ".png").toExternalForm()));
            this.setEffect(null);
        }
        this.setFill(imagePattern);
    }

    private void checkCapital() {
        ImagePattern imagePattern = null;
        for (User user : User.getUsers()) {
            for (City city : user.getCities()) {
                if(city.getCapital().equals(this.tile)){
                    imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/Capital.png").toExternalForm()));
                }
            }
        }
        if(imagePattern != null){
            feature.setFill(imagePattern);
            this.setFill(imagePattern);
        }
    }

    public void addFeature() {
        feature = new Polygon();
        feature.getPoints().addAll(this.getPoints());
        if (tile.getFeature() != null && !tile.getVisibilityForUser(GameController.getTurn()).equals("fog of war")) {
            ImagePattern imagePattern = new ImagePattern(new Image(getClass().getResource("/images/tile/" + tile.getFeature().getName() + ".png").toExternalForm()));
            feature.setFill(imagePattern);
            if (tile.getVisibilityForUser(GameController.getTurn()).equals("revealed"))
                feature.setEffect(lighting);
            tileMap.getChildren().add(feature);
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void addUnit(Unit unit) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(100);
        rectangle.setWidth(100);
        rectangle.setX(this.getPoints().get(0) + graphicUnits.size() * 40);
        rectangle.setY(this.getPoints().get(5));
        ImagePattern unitImage = new ImagePattern(new Image(getClass().getResource("/images/unit/" + unit.getName() + ".png").toExternalForm()));
        rectangle.setFill(unitImage);
        graphicUnits.put(unit, rectangle);
        tileMap.getChildren().add(rectangle);
    }

    public void deleteUnit(Unit unit) {
        tileMap.getChildren().remove(graphicUnits.get(unit));
        graphicUnits.remove(unit);
    }

    public HashMap<Unit, Rectangle> getGraphicUnits() {
        return graphicUnits;
    }

    public VBox getInfosBox(BackgroundSize backgroundSize) {
        if(infos==null) {
            infos=new VBox();
            infos.setMinHeight(60);
            infos.setMinWidth(200);
            BackgroundImage backgroundImage1 = new BackgroundImage(new Image(getClass().getResource("/images/backgrounds/tileInfo.png").toExternalForm()),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    backgroundSize);
            infos.setBackground(new Background(backgroundImage1));
            if (this.getTile().getResource() != null) {
                HBox resourceHBox = new HBox();
                Rectangle resource = new Rectangle();
                ImagePattern resourceImage = new ImagePattern(new Image(getClass().getResource("/images/Resources/" + this.getTile().getResource().getName() + ".png").toExternalForm()));
                resource.setHeight(100);
                resource.setWidth(100);
                resource.setFill(resourceImage);
                resourceHBox.getChildren().add(resource);
                Text resourceName = new Text("    Resource: " + this.getTile().getResource().getName());
                Text resourceValues = new Text("    Resource Values: Gold: " + this.getTile().getResource().getGold() + "  Food: " + this.getTile().getResource().getFood() + "   Production: " + this.getTile().getResource().getProduction());
                resourceName.setFill(Color.WHITE);
                resourceName.getStyleClass().add("tileInfo");
                resourceValues.setFill(Color.WHITE);
                resourceValues.getStyleClass().add("tileInfo");
                VBox resourceInfo = new VBox();
                resourceInfo.getChildren().add(resourceName);
                resourceInfo.getChildren().add(resourceValues);
                resourceHBox.getChildren().add(resourceInfo);
                infos.getChildren().add(resourceHBox);
            }
            Text tileInfo = new Text("Tile Values: Gold: " + this.getTile().getGold() + "  Production:  " + this.getTile().getProduction() + "  Food:  " + this.getTile().getFood());
            tileInfo.setFill(Color.WHITE);
            tileInfo.getStyleClass().add("tileInfo");
            infos.getChildren().add(tileInfo);
            Text terrain = new Text("Terrain Type: "+this.getTile().getTerrain().getName());
            terrain.setFill(Color.WHITE);
            terrain.getStyleClass().add("tileInfo");
            infos.getChildren().add(terrain);
            if(this.getTile().getFeature()!=null) {
                Text feature = new Text("Feature Type: " + this.getTile().getFeature().getName());
                feature.setFill(Color.WHITE);
                feature.getStyleClass().add("tileInfo");
                infos.getChildren().add(feature);
            }
            infos.setOpacity(0.8);
        }
        infos.setLayoutX(this.getPoints().get(10));
        infos.setLayoutY(this.getPoints().get(7));
        return infos;
    }
}
