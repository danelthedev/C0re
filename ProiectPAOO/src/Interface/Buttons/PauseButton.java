package Interface.Buttons;

import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class PauseButton  implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float) Game.GameWindow.getWidth()/2 - 265, 50), 734, 186, 0.75f, 0.75f);
    public Sprite sprite = new Sprite("./Assets/menu buttons/paused.png", transform);

    static PauseButton instance;

    public static PauseButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new PauseButton();
            }
        }

        return instance;
    }

    private PauseButton(){
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
