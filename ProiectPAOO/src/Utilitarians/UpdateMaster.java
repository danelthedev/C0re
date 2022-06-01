package Utilitarians;

import Interface.Buttons.*;
import Interface.Buttons.SoundButtons.SoundBar;
import Game.Game;
import Game.WaveManager;
import Game.MenuState;
import Graphics.Camera;
import Interface.Shop;

import java.awt.*;


public class UpdateMaster {

    public static void updatePlayStateObjects(){

        if(Game.isPlayerAlive) {
            WaveManager.getInstance().update();
            updatePlayer();
            updateEnemies();
            if(!Shop.isActive)
                updateAsteroids();
            updateProjectiles();
            updateTrails();
            updateCamera();
            updateShop();
        }
        else QuitButton.getInstance().update();
    }

    public static void updateMenuStateObjects(){
        switch (MenuState.submenu){
            case "":
                updateMainMenuButtons();
                break;
            case "options":
                updateOptionsButtons();
                break;
            case "factionChoose":
                updateFactionChooseButtons();
                break;
            case "leaderboard":
                updateLeaderboardButtons();
        }

    }

    //pause objects
    public static void updatePauseStateObjects(){
        QuitButton.getInstance().update();
    }

    //playstate objects
    private static void updatePlayer(){
        Game.player.update();
    }
    private static void updateEnemies(){
        for(int i = 0; i < Game.enemies.size(); ++ i) {
            Game.enemies.get(i).update();
        }
    }
    private static void updateAsteroids(){
        for(int i = 0; i < Game.asteroids.size(); ++ i)
            Game.asteroids.get(i).update();
    }

    private static void updateProjectiles(){
        for(int i = 0; i < Game.playerProj.size(); ++ i)
            Game.playerProj.get(i).update();
        for(int i = 0; i < Game.enemyProj.size(); ++ i)
            Game.enemyProj.get(i).update();

        for(int i = 0; i < Game.PplasmaAOEs.size(); ++ i)
            Game.PplasmaAOEs.get(i).update();
        for(int i = 0; i < Game.EplasmaAOEs.size(); ++ i)
            Game.EplasmaAOEs.get(i).update();

        for(int i = 0; i < Game.EExplosionAOEs.size(); ++ i)
            Game.EExplosionAOEs.get(i).update();
        for(int i = 0; i < Game.PExplosionAOEs.size(); ++ i)
            Game.PExplosionAOEs.get(i).update();

    }
    private static void updateTrails(){
        for(int i = 0; i < Game.trails.size(); ++ i)
            Game.trails.get(i).update();
    }
    private static void updateCamera(){
        Camera.getInstance().update();
    }
    private static void updateShop(){
        Shop.getInstance().update();
    }

    //main menu objects
    private static void updateMainMenuButtons(){
        PlayButton.getInstance().update();
        OptionsButton.getInstance().update();
        QuitButton.getInstance().update();
        LeaderboardButton.getInstance().update();
    }

    //options objects
    private static void updateOptionsButtons(){
        SoundBar.getInstance().update();
        BackButton.getInstance().update();
        ControlsButton.getInstance().update();
        KBControlsButton.getInstance().update();
        MouseControlsButton.getInstance().update();
    }

    //faction choose objects
    private static void updateFactionChooseButtons(){

        AbsterianButton.getInstance().update();
        HegemonyButton.getInstance().update();
        ADWButton.getInstance().update();
        KerthamButton.getInstance().update();
        BackButton.getInstance().update();
    }

    //update leaderboard objects
    private static void updateLeaderboardButtons(){
        BackButton.getInstance().update();
    }
}
