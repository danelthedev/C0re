package Game;

import Entities.Enemy;
import Entities.Player;
import Graphics.Background;
import Graphics.Camera;
import Graphics.Renderer;
import Utilitarians.CollisionMaster;
import Utilitarians.UpdateMaster;
import Utilitarians.Vector2;

public class PlayState implements GameState{

    Renderer renderer = Renderer.getInstance();

    PlayState(){

        if(Game.player == null)
            Game.player = new Player();

        Game.isPlayerAlive = true;
        Camera.getInstance().setFollowedEntity(Game.player);
    }

    @Override
    public void update() {
        //update objects
        UpdateMaster.updatePlayStateObjects();
        //check collisions
        CollisionMaster.checkCollisions();
    }

    @Override
    public void render() {
        renderer.renderPlayStateLayers();
    }

    @Override
    public void prev(Game game) {
        for(int i = 0; i < Game.enemies.size(); ++ i)
            Game.enemies.get(i).delete();

        Camera.getInstance().setPosition(new Vector2(0, 0));

        Background.getInstance().setBackground("./Assets/backgrounds/background1.png");

        WaveManager.getInstance().fullReset();

        game.setState(new MenuState());
    }

    @Override
    public void next(Game game) {
        Camera.getInstance().setPosition(new Vector2(0, 0));

        game.setState(new PauseState());
    }

}
