package Interface.Buttons;

import Game.Game;
import Game.MenuState;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class ADWButton  implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float) Game.GameWindow.getWidth()/2 - 530, 320), 455, 217);
    public Sprite sprite = new Sprite("./Assets/menu buttons/adwCard.png", transform);
    boolean hovered = false;
    public boolean unlockStatus = Game.factionsUnlocked[2] == 1;

    static ADWButton instance;

    public static ADWButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new ADWButton();
            }
        }

        return instance;
    }

    private ADWButton(){
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
                if(unlockStatus) {
                    MenuState.submenu = "";
                    Game.playerFaction = "ADW";
                    Game.gameState.next(Game.getInstance());
                }
                else if (Game.globalCredits >= 1000){
                    Game.globalCredits -= 1000;
                    unlockStatus = true;
                    Game.db.updateDB("MetaProgression", "ADWUnlocked", 1);
                    Game.db.updateDB("MetaProgression", "GlobalCredits", Game.globalCredits);
                }
            }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
