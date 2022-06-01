package Interface.Buttons.SoundButtons;

import Interface.Buttons.ButtonTemplate;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class SoundButton implements ButtonTemplate {

    Transform transform = new Transform(new Vector2(50, 20), 293, 63);
    public Sprite sprite = new Sprite("./Assets/menu buttons/sound.png", transform);

    static SoundButton instance;

    public static SoundButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new SoundButton();
            }
        }

        return instance;
    }

    private SoundButton(){
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
