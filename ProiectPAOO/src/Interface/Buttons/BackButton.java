package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.Game;
import Game.MenuState;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class BackButton implements ButtonTemplate{
    Transform transform = new Transform(new Vector2(50, Game.GameWindow.getHeight() - 150), 236, 63);
    public Sprite sprite = new Sprite("./Assets/menu buttons/back.png", transform);
    boolean hovered = false;

    static BackButton instance;

    public static BackButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new BackButton();
            }
        }

        return instance;
    }


    private BackButton(){
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
            if(!hovered)
                try {
                sprite.setSprite("./Assets/menu buttons/backHovered.png");
                }catch (InvalidPathException e){
                    e.printStackTrace();
                }

            return true;
        }
        if(hovered) {
            try {
                sprite.setSprite("./Assets/menu buttons/back.png");
            }catch (InvalidPathException e){
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public void isClicked() {
        if(hovered)
            if(Input.LMB) {
                //back button for menu
                if(Game.gameState.getClass() == MenuState.class)
                    MenuState.returnToMenu();
                else
                //back button for pause
                    Game.gameState.prev(Game.getInstance());


            }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }

}
