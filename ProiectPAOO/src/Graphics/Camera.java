package Graphics;

import Entities.ComplexEntity;
import Game.Game;
import Utilitarians.Vector2;

import static Utilitarians.Utilities.clamp;

public class Camera {

    private static Camera instance = null;
    public Vector2 position;
    public ComplexEntity followedEntity;

    public static Camera getInstance(){
        if(instance == null){
            synchronized (Camera.class){
                if(instance == null)
                    instance = new Camera();
            }
        }

        return instance;
    }

    private Camera(){
        position = new Vector2();
        position.x = 0;
        position.y = 0;
        followedEntity = null;
    }

    public void update(){

        if(followedEntity != null){
            Vector2 pos = followedEntity.getTransform().position;

            position.x = (int) clamp((float) (pos.x - Game.GameWindow.getWidth()/2 + 64), 0, (Background.getInstance().getWidth() - Game.GameWindow.getWidth()));
            position.y = (int) clamp((float) (pos.y - Game.GameWindow.getHeight()/2 + 64), 0, (Background.getInstance().getHeight() - Game.GameWindow.getHeight()));
        }

    }

    public void setPosition(Vector2 pos){
        position.x = pos.x;
        position.y = pos.y;
    }

    public void setFollowedEntity(ComplexEntity entity){
        this.followedEntity = entity;
    }

}
