package view_graphic.component;

import controller.CivilizationController;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Tile;
import model.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphicTile extends Polygon {
    private Text location;
    private Polygon feature;
    private Tile tile;
    private AnchorPane tileMap;
    private HashMap<Unit,Rectangle> graphicUnits=new HashMap<>();
    private CivilizationController civilizationController;
    public GraphicTile(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x5, double y5, double x6, double y6, String location, Tile tile, AnchorPane tileMap,CivilizationController civilizationController){
        this.tile=tile;
        this.tileMap=tileMap;
        this.civilizationController=civilizationController;
        this.getPoints().addAll(x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6);
        this.location=new Text(this.getPoints().get(0),this.getPoints().get(1)+15,location);
        feature=new Polygon(x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6);
    }

    public Text getLocation () {
        return location;
    }

    public Polygon getFeature () {
        return feature;
    }

    public void move(double dx,double dy){
        this.getPoints().set(0,(this.getPoints().get(0)+dx));
        this.getPoints().set(1,(this.getPoints().get(1)+dy));
        this.getPoints().set(2,(this.getPoints().get(2)+dx));
        this.getPoints().set(3,(this.getPoints().get(3)+dy));
        this.getPoints().set(4,(this.getPoints().get(4)+dx));
        this.getPoints().set(5,(this.getPoints().get(5)+dy));
        this.getPoints().set(6,(this.getPoints().get(6)+dx));
        this.getPoints().set(7,(this.getPoints().get(7)+dy));
        this.getPoints().set(8,(this.getPoints().get(8)+dx));
        this.getPoints().set(9,(this.getPoints().get(9)+dy));
        this.getPoints().set(10,(this.getPoints().get(10)+dx));
        this.getPoints().set(11,(this.getPoints().get(11)+dy));
        this.getFeature().getPoints().set(0,(this.getFeature().getPoints().get(0)+dx));
        this.getFeature().getPoints().set(1,(this.getFeature().getPoints().get(1)+dy));
        this.getFeature().getPoints().set(2,(this.getFeature().getPoints().get(2)+dx));
        this.getFeature().getPoints().set(3,(this.getFeature().getPoints().get(3)+dy));
        this.getFeature().getPoints().set(4,(this.getFeature().getPoints().get(4)+dx));
        this.getFeature().getPoints().set(5,(this.getFeature().getPoints().get(5)+dy));
        this.getFeature().getPoints().set(6,(this.getFeature().getPoints().get(6)+dx));
        this.getFeature().getPoints().set(7,(this.getFeature().getPoints().get(7)+dy));
        this.getFeature().getPoints().set(8,(this.getFeature().getPoints().get(8)+dx));
        this.getFeature().getPoints().set(9,(this.getFeature().getPoints().get(9)+dy));
        this.getFeature().getPoints().set(10,(this.getFeature().getPoints().get(10)+dx));
        this.getFeature().getPoints().set(11,(this.getFeature().getPoints().get(11)+dy));
        this.getLocation().setX(this.getLocation().getX()+dx);
        this.getLocation().setY(this.getLocation().getY()+dy);
        for(Map.Entry<Unit,Rectangle> entry : this.graphicUnits.entrySet()){
            this.graphicUnits.get(entry.getKey()).setX(this.graphicUnits.get(entry.getKey()).getX()+dx);
            this.graphicUnits.get(entry.getKey()).setY(this.graphicUnits.get(entry.getKey()).getY()+dy);
        }
        if(this.getPoints().get(1)<0){
            tileMap.getChildren().remove(this);
            tileMap.getChildren().remove(this.location);
            tileMap.getChildren().remove(this.feature);
            for(Map.Entry<Unit,Rectangle> entry : this.graphicUnits.entrySet()){
                tileMap.getChildren().remove(this.graphicUnits.get(entry.getKey()));
            }
        }
        else if(!tileMap.getChildren().contains(this)){
            tileMap.getChildren().add(this);
            tileMap.getChildren().add(this.location);
            if(this.tile.getFeature()!=null && !this.tile.getVisibilityForUser(civilizationController.getTurn()).equals("fog of war")) {
                tileMap.getChildren().add(this.feature);
            }
            for(Map.Entry<Unit,Rectangle> entry : this.graphicUnits.entrySet()){
                tileMap.getChildren().add(this.graphicUnits.get(entry.getKey()));
            }
        }
    }

    public Tile getTile () {
        return tile;
    }

    public void addUnit(Unit unit){
        Rectangle rectangle=new Rectangle();
        rectangle.setHeight(100);
        rectangle.setWidth(100);
        rectangle.setX(this.getPoints().get(0)+graphicUnits.size()*40);
        rectangle.setY(this.getPoints().get(5));
        ImagePattern unitImage=new ImagePattern(new Image(getClass().getResource("/images/unit/"+unit.getName()+".png").toExternalForm()));
        rectangle.setFill(unitImage);
        graphicUnits.put(unit,rectangle);
        tileMap.getChildren().add(rectangle);
    }

    public void deleteUnit(Unit unit){
        tileMap.getChildren().remove(graphicUnits.get(unit));
        graphicUnits.remove(unit);
    }

    public HashMap<Unit, Rectangle> getGraphicUnits () {
        return graphicUnits;
    }
}
