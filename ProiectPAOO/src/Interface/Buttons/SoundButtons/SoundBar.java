package Interface.Buttons.SoundButtons;

import Interface.Buttons.ButtonTemplate;
import Graphics.Renderer;
import Game.Game;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class SoundBar implements ButtonTemplate {

    int clickPos;
    public static float volume = 1;

    Transform transform = new Transform(new Vector2(50, 100), 415, 103);
    public Sprite sprite = new Sprite("./Assets/menu buttons/soundOutline.png", transform);
    boolean hovered = false;

    static SoundBar instance;

    public static SoundBar getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new SoundBar();
            }
        }

        return instance;
    }

    private SoundBar(){
        //  Renderer.getInstance().addToLayer("Interface.Buttons", sprite);
    }

    @Override
    public void update() {
        hovered = isHovered();
        isClicked();
    }

    @Override
    public boolean isHovered() {
        return Input.mousePosition.pointInRectangle(transform.position, new Vector2(transform.position.x + transform.width, transform.position.y + transform.height));
    }

    @Override
    public void isClicked() {
        if(hovered) {
            if (Input.LMB) {
                clickPos = (int) Input.mousePosition.x;
                volume = (float) ((clickPos - transform.position.x) / 415);
                Game.db.updateDB("MetaProgression","Volume", volume);
            }
        }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
