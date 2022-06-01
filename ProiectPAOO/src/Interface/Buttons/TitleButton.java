package Interface.Buttons;

import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class TitleButton implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float)Game.GameWindow.getWidth()/2 - 230, 75), 609, 163, 0.75f, 0.75f);
    public Sprite sprite = new Sprite("./Assets/menu buttons/C0re.png", transform);

    static TitleButton instance;

    public static TitleButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new TitleButton();
            }
        }

        return instance;
    }

    private TitleButton(){
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
