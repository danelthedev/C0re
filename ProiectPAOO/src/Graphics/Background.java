package Graphics;

import Exceptions.InvalidPathException;
import Game.Game;

import Utilitarians.Transform;

public class Background {

    static Background instance = null;
    private Sprite sprite = null;

    public static Background getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new Background();
            }
        }

        return instance;
    }

    private Background() {
        sprite = new Sprite("./Assets/backgrounds/background1.png", new Transform());

        Renderer.getInstance().addToLayer("Background", sprite);
    }

    private Background(String filepath) {
        sprite = new Sprite(filepath, new Transform());

        Renderer.getInstance().addToLayer("Background", sprite);
    }

    public int getWidth(){
        return sprite.getWidth();
    }

    public int getHeight(){
        return sprite.getHeight();
    }

    public void setBackground(String filepath){
        try {
            sprite.setSprite(filepath);
        }catch (InvalidPathException e){
            e.printStackTrace();
        }
    }
}