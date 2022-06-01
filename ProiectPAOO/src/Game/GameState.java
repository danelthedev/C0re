package Game;

public interface GameState {

    //The 3 states of the game are: MenuState | PlayState | PauseState
    void update();
    void render();
    void next(Game game);
    void prev(Game game);
}
