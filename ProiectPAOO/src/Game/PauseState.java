package Game;

import Interface.Buttons.ButtonTemplate;
import Interface.Buttons.PauseButton;
import Interface.Buttons.QuitButton;
import Graphics.Background;
import Graphics.Camera;
import Graphics.Renderer;
import Utilitarians.UpdateMaster;
import Utilitarians.Vector2;

public class PauseState implements GameState{

    ButtonTemplate pause, quit;
    Renderer renderer = Renderer.getInstance();

    public PauseState(){
        pause = PauseButton.getInstance();
        quit = QuitButton.getInstance();
    }

    @Override
    public void update() {
        UpdateMaster.updatePauseStateObjects();
    }

    @Override
    public void render() {
        renderer.renderPauseStateButtons();
    }

    @Override
    public void prev(Game game) {
        game.setState(new PlayState());
    }

    @Override
    public void next(Game game) {
        WaveManager.getInstance().waveReset();
        Game.player.delete();
        Camera.getInstance().position = new Vector2(0, 0);
        Background.getInstance().setBackground("./Assets/backgrounds/background1.png");
        game.setState(new MenuState());
    }
}
