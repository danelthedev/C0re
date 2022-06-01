package Interface.Buttons;

import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class ControlsButton implements ButtonTemplate{

    Transform transform = new Transform(new Vector2(50, 200), 1256, 129, 0.75f, 0.75f);
    public Sprite sprite = new Sprite("./Assets/menu buttons/controls.png", transform);

    static ControlsButton instance;

    public static ControlsButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new ControlsButton();
            }
        }

        return instance;
    }

    private ControlsButton(){
        //    Renderer.getInstance().addToLayer("Interface.Buttons", sprite);
    }

    @Override
    public void update() {
        isHovered();
        isClicked();

    }

    @Override
    public boolean isHovered() {
        return false;
    }

    @Override
    public void isClicked() {

    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
