package model;

import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class GraphicTile extends Polygon {
    private Text location;
    public GraphicTile(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4,double x5,double y5,double x6,double y6,String location){
        this.getPoints().addAll(x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6);
        this.location=new Text(this.getPoints().get(0),this.getPoints().get(1)+15,location);
    }

    public Text getLocation () {
        return location;
    }

}
