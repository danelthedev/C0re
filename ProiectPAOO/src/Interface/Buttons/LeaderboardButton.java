package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.Game;
import Game.MenuState;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class LeaderboardButton  implements ButtonTemplate {

    Transform transform = new Transform(new Vector2((float) Game.GameWindow.getWidth() - 400, Game.GameWindow.getHeight() - 100), 483, 51, 0.75f, 0.75f);
    public Sprite sprite = new Sprite("./Assets/menu buttons/leaderboard.png", transform);
    boolean hovered = false;

    static LeaderboardButton instance;

    public static LeaderboardButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new LeaderboardButton();
            }
        }

        return instance;
    }

    private LeaderboardButton(){
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
                    sprite.setSprite("./Assets/menu buttons/leaderboard.png");
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        if(hovered) {
            try {
                sprite.setSprite("./Assets/menu buttons/leaderboard.png");
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
                MenuState.enterLeaderboard();
            }
    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }

}
