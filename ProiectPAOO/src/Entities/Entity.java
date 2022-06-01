package Entities;

import Graphics.Camera;
import Graphics.Sprite;
import Utilitarians.Collider;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class Entity {
    //general stuff
    protected Transform transform;
    protected Sprite sprite;
    protected Collider collider;

    protected float maxHp, baseMaxHp;
    protected float curHp;

    protected float maxSpeed , baseMaxSpeed;
    protected float acceleration;
    protected Vector2 direction;

    Entity(){
        transform = new Transform();
        sprite = new Sprite("./Assets/ships/shipBasic.png", transform);
        //create collider and direction
        collider = new Collider();
        direction = new Vector2();
    }

    public boolean cursorOverEntity(){
        return (Input.mousePosition.x > transform.position.x - Camera.getInstance().position.x && Input.mousePosition.x < transform.position.x + transform.width * transform.x_scale - Camera.getInstance().position.x)
                && (Input.mousePosition.y > transform.position.y - Camera.getInstance().position.y && Input.mousePosition.y < transform.position.y + transform.height * transform.y_scale - Camera.getInstance().position.y);
    }
    public Vector2 getCenter(){
        Vector2 center;
        center = new Vector2();
        center.x = this.transform.position.x + (float)this.transform.width * this.transform.x_scale /2;
        center.y = this.transform.position.y + (float)this.transform.height * this.transform.y_scale / 2;
        return center;
    }


    public Transform getTransform() {
        return transform;
    }
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Sprite getSprite() {
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Collider getCollider() {
        return collider;
    }
    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public float getMaxHp() {
        return maxHp;
    }
    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    public float getBaseMaxHp() {
        return baseMaxHp;
    }
    public void setBaseMaxHp(float baseMaxHp) {
        this.baseMaxHp = baseMaxHp;
    }

    public float getCurHp() {
        return curHp;
    }
    public void setCurHp(float curHp) {
        this.curHp = curHp;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getBaseMaxSpeed() {
        return baseMaxSpeed;
    }
    public void setBaseMaxSpeed(float baseMaxSpeed) {
        this.baseMaxSpeed = baseMaxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getDirection() {
        return direction;
    }
    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }


}
