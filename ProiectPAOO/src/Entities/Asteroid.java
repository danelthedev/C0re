package Entities;

import Game.Game;
import Graphics.Background;
import Graphics.Renderer;
import Utilitarians.Collider;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Utilities;
import Utilitarians.Vector2;

public class Asteroid extends Entity {

    public boolean hasCollided;

    public Asteroid(int spawnEdge){

        this.maxHp = 20;
        curHp = maxHp;

        maxSpeed = 3;
        //set scale
        transform.y_scale = Utilities.clamp((float)Math.random() * 3, 0.5f, 3);
        transform.x_scale = transform.y_scale;

        acceleration = 0.0f;

        //collider dimensions
        collider.transform.width = sprite.transform.width;
        collider.transform.height = sprite.transform.height;


        //set position and direction depending on the edge

        switch (spawnEdge) {//top
            case 0:
                transform.position.x = Math.random() * Background.getInstance().getWidth();
                transform.position.y = -collider.transform.height;
                direction = new Vector2(0, maxSpeed);
                break;
//left
            case 1:
                transform.position.x = -collider.transform.width;
                transform.position.y = Math.random() * Background.getInstance().getHeight();
                direction = new Vector2(maxSpeed, 0);
                break;
//down
            case 2:
                transform.position.x = Math.random() * Background.getInstance().getWidth();
                transform.position.y = Background.getInstance().getHeight() + collider.transform.height;
                direction = new Vector2(0, -maxSpeed);
                break;
//right
            case 3:
                transform.position.x = Background.getInstance().getWidth() + collider.transform.width;
                transform.position.y = Math.random() * Background.getInstance().getHeight();
                direction = new Vector2(-maxSpeed, 0);
                break;
        }


        sprite = new Sprite("./Assets/obstacles/asteroid.png", transform);
        transform.height = sprite.getHeight();
        transform.width = sprite.getWidth();
        Renderer.getInstance().addToLayer("Asteroids", sprite);
    }

    public void update(){
        System.out.println("Asteroid test");
        //decelerate when struck
        direction.x = direction.x - ( Math.signum(direction.x) * acceleration);
        direction.y = direction.y - ( Math.signum(direction.y) * acceleration);
        if(Math.abs(direction.x) < 0.1)
            direction.x = 0;
        if(Math.abs(direction.y) < 0.1)
            direction.y = 0;

        //actual moving
        transform.position.x += direction.x;
        transform.position.y += direction.y;

        //update sprite position
        sprite.transform.copyTransform(this.transform);
        //update collider position
        collider.transform.copyPosition(this.transform);
        updateColliderSize();

        if(curHp <= 0)
            delete();

    }

    public void hit(float dmg){
        curHp -= dmg;
    }

    public void delete(){
        Game.asteroids.remove(this);
        Renderer.getInstance().renderLayers.get("Asteroids").remove(sprite);
        System.gc();
    }

    public void removeDefDir(){
        hasCollided = true;
        acceleration = 0.025f;
    }

    public void updateColliderSize(){
        collider.transform.width = (int) (sprite.getWidth() * sprite.transform.x_scale);
        collider.transform.height = (int) (sprite.getHeight() * sprite.transform.x_scale);
    }

}
