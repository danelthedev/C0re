package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;
import Game.MenuState;

public class OptionsButton implements ButtonTemplate {

    Transform transform = new Transform(new Vector2((float) Game.GameWindow.getWidth()/2 - 260, 430), 515, 110);
    public Sprite sprite = new Sprite("./Assets/menu buttons/options.png", transform);
    boolean hovered = false;

    static OptionsButton instance;

    public static OptionsButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new OptionsButton();
            }
        }

        return instance;
    }

    private OptionsButton(){
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
                    sprite.setSprite("./Assets/menu buttons/optionsHovered.png");
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        if(hovered) {
            try {
                sprite.setSprite("./Assets/menu buttons/options.png");
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
                MenuState.enterOptions();
            }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }

}
