package Utilitarians;

import java.awt.*;

public class Vector2 {

    public double x, y;

    public Vector2(){
        x = 0;
        y = 0;
    }

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v2){
        this.x = v2.x;
        this.y = v2.y;
    }

    public Vector2 getDirection(Vector2 target){
        Vector2 temp =  new Vector2(this.x - target.x, this.y - target.y);
        return temp;
    }

    public float getAngle(Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    public Vector2 add(Vector2 op2){
        Vector2 ret = new Vector2();
        ret.x = this.x + op2.x;
        ret.y = this.y + op2.y;
        return ret;
    }

    public Vector2 add(int x, int y){
        Vector2 ret = new Vector2();
        ret.x = this.x + x;
        ret.y = this.y + y;
        return ret;
    }

    public Vector2 sub(Vector2 op2){
        Vector2 ret = new Vector2();
        ret.x = this.x - op2.x;
        ret.y = this.y - op2.y;
        return ret;
    }

    public Vector2 sub(float x, float y){
        Vector2 ret = new Vector2();
        ret.x = this.x - x;
        ret.y = this.y - y;
        return ret;
    }

    public float abs(){
        return (float) Math.sqrt(x * x + y * y);
    }

    public float distanceTo(Vector2 op2){
        return (float) Math.sqrt((this.x - op2.x) * (this.x - op2.x) + (this.y - op2.y) * (this.y - op2.y));
    }

    public boolean pointInRectangle(Vector2 upperLeft, Vector2 lowerRight){
        return (this.x > upperLeft.x && this.x < lowerRight.x) && (this.y > upperLeft.y && this.y < lowerRight.y);
    }
}
