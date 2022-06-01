package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.Game;
import Game.MenuState;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Database;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class PlayButton implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float)Game.GameWindow.getWidth()/2 - 175, 275), 350, 100);
    public Sprite sprite = new Sprite("./Assets/menu buttons/play.png", transform);
    boolean hovered = false;

    static PlayButton instance;

    public static PlayButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new PlayButton();
            }
        }

        return instance;
    }

    private PlayButton(){
    //    Renderer.getInstance().addToLayer("Interface.Buttons", sprite);
    }

    @Override
    public void update() {
        hovered = isHovered();
        isClicked();
    }

    @Override
    public boolean isHovered() {
        if (Input.mousePosition.pointInRectangle(transform.position, new Vector2(transform.position.x + transform.width, transform.position.y + transform.height))){
            if(!hovered) {
                try {
                    sprite.setSprite("./Assets/menu buttons/playHovered.png");
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        if(hovered) {
            try {
                sprite.setSprite("./Assets/menu buttons/play.png");
            } catch (InvalidPathException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void isClicked() {
        if(hovered)
            if(Input.LMB) {
                if(!Database.getInstance().checkForSaveData()) //daca nu ai un save state, selecteaza factiunea
                    MenuState.enterFactionChoose();
                else{ //daca ai un saveState, incarca-l in memorie
                    Database.getInstance().loadSaveData();
                }
            }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
