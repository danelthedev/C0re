package Interface.Buttons;

import Exceptions.InvalidPathException;
import Game.*;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Database;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class QuitButton implements ButtonTemplate{

    Transform transform = new Transform(new Vector2((float)Game.GameWindow.getWidth()/2 - 150, Game.GameWindow.getHeight()-200), 299, 100);
    public Sprite sprite = new Sprite("./Assets/menu buttons/quit.png", transform);
    boolean hovered = false;

    static QuitButton instance;

    public static QuitButton getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new QuitButton();
            }
        }

        return instance;
    }

    private QuitButton(){
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
                    sprite.setSprite("./Assets/menu buttons/quitHovered.png");
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        if(hovered) {
            try {
                sprite.setSprite("./Assets/menu buttons/quit.png");
            } catch (InvalidPathException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void isClicked() {
        if(hovered && Input.LMB)
            if(Game.gameState.getClass() == MenuState.class) //daca esti in meniu
                System.exit(0);
            else if(Game.gameState.getClass() == PauseState.class) { //daca ai dat pauza
                WaveManager.getInstance().waveReset();
                SaveState.getInstance().save();
                Game.gameState.next(Game.getInstance());
                Game.credits = 0;

            }
            else if(Game.gameState.getClass() == PlayState.class) { //daca ai murit
                SaveState.getInstance().deleteSaveState();

            Leaderboard.getInstance().addLeaderboardEntry(Input.buffer, Game.credits);

            Game.gameState.prev(Game.getInstance());
            Game.globalCredits += Game.credits / 2;
            Database.getInstance().updateDB("MetaProgression", "GlobalCredits", Game.globalCredits);
            Game.credits = 0;
        }

    }

    @Override
    public void delete() {
        Renderer.getInstance().renderLayers.get("Buttons").remove(sprite);
        System.gc();
    }
}
