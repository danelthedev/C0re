package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class KBControlsButton   implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float) 600, 210), 78, 78);
    public Sprite sprite = new Sprite("./Assets/menu buttons/checkboxFalse.png", transform);
    boolean hovered = false;

    static KBControlsButton instance;

    public static KBControlsButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new KBControlsButton();
            }
        }

        return instance;
    }

    private KBControlsButton(){
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
            return true;
        }

        return false;

    }

    @Override
    public void isClicked() {
        if(hovered)
            if(Input.LMB) {
                if(Game.controlType != Game.ControlType.KeyboardControl) {
                    try {
                        sprite.setSprite("./Assets/menu buttons/checkboxTrue.png");
                        MouseControlsButton.getInstance().sprite.setSprite("./Assets/menu buttons/checkboxFalse.png");
                        Game.controlType = Game.ControlType.KeyboardControl;
                    } catch (InvalidPathException e) {
                        e.printStackTrace();
                    }
                }

            }

    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
