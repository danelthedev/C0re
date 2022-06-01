package Game;

import Interface.Buttons.*;
import Graphics.Background;
import Graphics.Renderer;
import Utilitarians.UpdateMaster;

import javax.swing.*;
import java.awt.*;

public class MenuState implements GameState{

    Renderer renderer = Renderer.getInstance();
    public static String submenu = "";

    //main menu buttons
    static ButtonTemplate quitButton, titleButton, playButton, optionsButton, leaderboardButton, controlsButton, kbControlsButton, mouseControlsButton;

    //options buttons
    static ButtonTemplate backButton, soundBar, soundButton;

    public MenuState(){
        Background.getInstance().setBackground("./Assets/backgrounds/background1.png");
        titleButton = TitleButton.getInstance();
        playButton = PlayButton.getInstance();
        optionsButton = OptionsButton.getInstance();
        quitButton = QuitButton.getInstance();
        leaderboardButton = LeaderboardButton.getInstance();
        controlsButton = ControlsButton.getInstance();
        mouseControlsButton = MouseControlsButton.getInstance();
        kbControlsButton = KBControlsButton.getInstance();
    }

    @Override
    public void update() {
        //update objects (buttons)
        UpdateMaster.updateMenuStateObjects();
    }

    @Override
    public void render() {

        //render buttons and stuff
        switch (submenu) {
            case "options":
                renderer.renderOptionsButtons();
                break;
            case "":
                renderer.renderMainMenuButtons();
                break;
            case "factionChoose":
                renderer.renderFactionChoose();
                break;
            case "leaderboard":
                renderer.renderLeaderboardSubmenu();
                break;
        }

    }

    @Override
    public void prev(Game game) {

    }

    @Override
    public void next(Game game) {
        Background.getInstance().setBackground("./Assets/backgrounds/background2.png");
        game.setState(new PlayState());
    }

    public static void enterOptions(){
        submenu = "options";
    }

    public static void enterLeaderboard(){
        submenu = "leaderboard";
    }

    public static void enterFactionChoose(){
        submenu = "factionChoose";
    }

    public static void returnToMenu(){
        submenu = "";
    }

}
