package Graphics;

import Interface.Buttons.*;
import Interface.Buttons.SoundButtons.SoundBar;
import Interface.Buttons.SoundButtons.SoundButton;
import Entities.Asteroid;
import Entities.Enemy;
import Game.Game;
import Game.Leaderboard;
import Game.WaveManager;
import Interface.DeathBox;
import Interface.Shop;
import Utilitarians.Input;
import Utilitarians.Vector2;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;

public class Renderer {

    public HashMap<String, ArrayList<Sprite>> renderLayers;

    static Renderer instance = null;

    public static Renderer getInstance(){
        if(instance == null){
            synchronized (Renderer.class){
                if(instance == null)
                    instance = new Renderer();
            }
        }

        return instance;
    }

    private Renderer(){
        renderLayers = new HashMap<>();
        createLayer("Background");
        createLayer("Player");
        createLayer("Enemies");
        createLayer("Shields");
        createLayer("Projectiles");
        createLayer("Trails");
        createLayer("Asteroids");
    }

    void createLayer(String layer){
        renderLayers.put(layer, new ArrayList<>());
    }

    void renderLayer(String layer){
        ArrayList<Sprite> temp = renderLayers.get(layer);
        for(Sprite sprite : temp) {
            sprite.render();
        }
    }

    void renderLayerWithBoundBox(String layer){
        ArrayList<Sprite> temp = renderLayers.get(layer);
        for(Sprite sprite : temp) {
            sprite.render();
            BufferStrategy t = Game.canvas.getBufferStrategy();
            Sprite.paintOutlineGrosRect(t.getDrawGraphics(), Color.YELLOW, 4, (int) (sprite.transform.position.x - Camera.getInstance().position.x), (int) (sprite.transform.position.y - Camera.getInstance().position.y), sprite.transform.width, sprite.transform.height);
        }
    }

    public void addToLayer(String layer, Sprite sprite){
        ArrayList<Sprite> tempLayer = renderLayers.get(layer);
        tempLayer.add(sprite);
    }

    public void renderShieldHp(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        float curHp = Game.player.getShield().getCurHp();
        float maxHp = Game.player.getShield().getMaxHp();

        Sprite.paintFilledRectWA(temp, Color.decode("#86f7f0"), 30, 49, (int)(300 * (curHp / maxHp)), 15, 0.5f);
        Sprite.paintOutlineGrosRectWA(temp, Color.BLACK, 4,30, 49, (int)(300 * (curHp / maxHp)), 15, 0.5f);
    }

    public void renderHullHp(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        float curHp = Game.player.getCurHp();
        float maxHp = Game.player.getMaxHp();

        Sprite.paintFilledRectWA(temp, Color.decode("#B8BC38"), 30, 30, (int)(300 * (curHp / maxHp)), 15, 0.5f);
        Sprite.paintOutlineGrosRectWA(temp, Color.BLACK, 4,30, 30, (int)(300 * (curHp / maxHp)), 15, 0.5f);
    }

    public void renderHighlightedHp(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        float curHp = -1;
        float maxHp = -1;

        for(Asteroid a : Game.asteroids)
            if(a.cursorOverEntity()){
                curHp = a.getCurHp();
                maxHp = a.getMaxHp();
            }

        for(Enemy e: Game.enemies)
            if(e.cursorOverEntity()){
                curHp = e.getCurHp();
                maxHp = e.getMaxHp();
            }

        if(maxHp != -1){
            Sprite.paintFilledRectWACentered(temp, Color.decode("#d1d1d1"), Game.GameWindow.getWidth()/2, 30, (int)(300 * (curHp / maxHp)), 10, 0.5f);
            Sprite.paintOutlineGrosRectWACentered(temp, Color.BLACK, 2,Game.GameWindow.getWidth()/2, 30, (int)(300 * (curHp / maxHp)), 10, 0.75f);
        }

    }

    public void renderCurWave(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        Sprite.paintTextWithShadow(temp,"Wave " + WaveManager.getInstance().currentWave, Game.GameWindow.getWidth()/2 - 140, Game.GameWindow.getHeight() - 50, Color.WHITE, 40);
    }

    public void renderCredits(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        Sprite.paintTextWithShadow(temp,"Credits: " + Game.credits, 20, Game.GameWindow.getHeight() - 60, Color.WHITE, 15);
    }

    public void renderStats(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        //weapon info
        Sprite.paintTextWithShadow(temp,"Weapon: " + Game.player.getWeaponType() + " " + Game.player.getWeapon().getUpgrade(), 20, 50, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Hull damage: " + Game.player.getWeapon().getHullDmg(), 20, 80, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Shield damage: " + Game.player.getWeapon().getShieldDmg(), 20, 110, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Fire rate: " +  String.format("%.02f", (float)60 / Game.player.getWeapon().getMaxCD()), 20, 140, Color.WHITE, 15f);

        //health info
        Sprite.paintTextWithShadow(temp,"Hull HP: " + Game.player.getMaxHp(), 20, 200, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Shield HP: " + Game.player.getShield().getMaxHp(), 20, 230, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Shield recharge delay: " + Game.player.getShield().getRechargeDelay(), 20, 260, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Shield recharge rate: " +  Game.player.getShield().getRechargeRate(), 20, 290, Color.WHITE,15f);

        //speed info
        Sprite.paintTextWithShadow(temp,"Max speed: " + Game.player.getMaxSpeed(), 20, 350, Color.WHITE,15f);

        //module info
        Sprite.paintTextWithShadow(temp,"Module: " + Game.player.getModule().getName(), 20, 410, Color.WHITE,15f);
        Sprite.paintTextWithShadow(temp,"Module upgrade: " + Game.player.getModule().getUpgrade(), 20, 440, Color.WHITE,15f);

    }

    public void renderUI(){
        renderShieldHp();
        renderHullHp();
        renderModuleCD();
        renderHighlightedHp();
        renderCurWave();
        renderCredits();
    }

    public void renderModuleCD(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        if(Game.player.getModule() != null)
        {
            if (Game.player.getModule().getCD() > 0)
                Sprite.paintTextWithShadow(temp, "Module CD: " + Game.player.getModule().getCD() / 60, 30, 100, Color.WHITE, 10f);
            else
                Sprite.paintTextWithShadow(temp, "Module Ready!", 30, 100, Color.WHITE, 10f);
        }
        else    Sprite.paintTextWithShadow(temp, "No module!", 30, 100, Color.WHITE, 10f);

    }

    public void renderPlayStateLayers(){
        //get to each rendering layer and render its contents
        renderLayer("Background");

        if(Game.isPlayerAlive) {
            renderLayer("Trails");
            renderLayer("Projectiles");
            renderLayer("Shields");
            renderLayer("Enemies");
            renderLayer("Asteroids");
            renderLayer("Player");


            if(Shop.isActive) {
                Shop.getInstance().sprite.render();
                renderCredits();
                if(Shop.showHighlight) {
                    Shop.getInstance().shopHighlighter.render();
                    renderShopInfo();
                }
            }
            else
                renderUI();

        }
        else{
            Camera.getInstance().setPosition(new Vector2(0, 0));
            renderDeathScreen();
        }


    }

    public void renderShopInfo(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        Sprite.paintTextWithShadow(temp, Shop.highlightedButton.value, Game.GameWindow.getWidth()/2 - 140, Game.GameWindow.getHeight() - 215, Color.WHITE, 20);
        Sprite.paintTextWithShadow(temp, Shop.highlightedButton.description, 183, Game.GameWindow.getHeight() - 180, Color.WHITE, 15);
        if(Shop.highlightedButton.price != 0)
            Sprite.paintTextWithShadow(temp, "Price: " + Shop.highlightedButton.price, 183, Game.GameWindow.getHeight() - 145, Color.WHITE, 15);
    }

    public void renderSoundBar(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        Sprite.paintFilledRectWA(temp, Color.decode("#ffef42"),65, 115, (int) (385 * SoundBar.volume), 42, 1);
    }

    public void renderMainMenuButtons(){
        renderLayer("Background");

        TitleButton.getInstance().sprite.render();
        PlayButton.getInstance().sprite.render();
        OptionsButton.getInstance().sprite.render();
        QuitButton.getInstance().sprite.render();
        LeaderboardButton.getInstance().sprite.render();
    }

    public void renderDeathScreen(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();

        DeathBox.sprite.render();

        if(WaveManager.getInstance().currentWave <= 30)
            Sprite.paintTextWithShadow(temp, "You Died", Game.GameWindow.getWidth() / 2 - 175, 250, Color.WHITE, 40);
        else
            Sprite.paintTextWithShadow(temp, "You Won!", Game.GameWindow.getWidth() / 2 - 175, 250, Color.WHITE, 40);

        Sprite.paintTextWithShadow(temp, "Final Score:", Game.GameWindow.getWidth() / 2 - 270, 335, Color.WHITE, 40);
        Sprite.paintTextWithShadow(temp, Integer.toString(Game.credits), Game.GameWindow.getWidth() / 2 - 50, 390, Color.WHITE, 40);
        Sprite.paintTextWithShadow(temp, "Input Name:", Game.GameWindow.getWidth() / 2 - 240, 450, Color.WHITE, 40);
        Sprite.paintTextWithShadow(temp, Input.buffer, Game.GameWindow.getWidth() / 2 - 100, 510, Color.GRAY, 40);


        QuitButton.getInstance().sprite.render();
    }

    public void renderTop10(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        int contor = 0;

        Sprite.paintTextWithShadow(temp, "Leaderboard", Game.GameWindow.getWidth() / 2 - 280, 60, Color.WHITE, 40);
        for(String entry: Leaderboard.getInstance().getTop10()) {
            ++contor;
            Sprite.paintTextWithShadow(temp, entry, Game.GameWindow.getWidth() / 2 - 250, 100 + contor * 60, Color.WHITE, 30);
        }
    }

    public void renderOptionsButtons(){
        renderLayer("Background");
        SoundButton.getInstance().sprite.render();
        SoundBar.getInstance().sprite.render();
        renderSoundBar();
        ControlsButton.getInstance().sprite.render();
        KBControlsButton.getInstance().sprite.render();
        MouseControlsButton.getInstance().sprite.render();

        BackButton.getInstance().sprite.render();

    }

    public void renderLeaderboardSubmenu(){
        renderLayer("Background");
        renderTop10();

        BackButton.getInstance().sprite.render();
    }

    public void renderFactionChoose(){
        Graphics temp = Game.canvas.getBufferStrategy().getDrawGraphics();
        renderLayer("Background");
        Sprite.paintTextWithShadow(temp, "Choose your faction", Game.GameWindow.getWidth() / 2 - 210, 40, Color.WHITE, 20);
        Sprite.paintTextWithShadow(temp, "Credits: " + Game.globalCredits, Game.GameWindow.getWidth() - 350, Game.GameWindow.getHeight() - 50, Color.WHITE, 15);


        AbsterianButton.getInstance().sprite.render();
        if(AbsterianButton.getInstance().isHovered()) {

            if(!AbsterianButton.getInstance().unlockStatus)
                Sprite.paintTextWithShadow(temp, "Unlock cost: 1000", Game.GameWindow.getWidth()/2 + 100, 300, Color.WHITE, 20);
            else
                Sprite.paintTextWithShadow(temp, "Unlocked", Game.GameWindow.getWidth()/2 + 100, 300, Color.WHITE, 20);

            Sprite.paintTextWithShadow(temp, "Absterian ships excel at long range and hit and run tactics", 400, Game.GameWindow.getHeight() - 110, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Weapon: Explosive", 400, Game.GameWindow.getHeight() - 80, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Module: Nitro", 400, Game.GameWindow.getHeight() - 50, Color.WHITE, 10);
        }

        HegemonyButton.getInstance().sprite.render();
        if(HegemonyButton.getInstance().isHovered()) {

            if(!HegemonyButton.getInstance().unlockStatus)
                Sprite.paintTextWithShadow(temp, "Unlock cost: 1000", Game.GameWindow.getWidth()/2 - 400, 300, Color.WHITE, 20);
            else
                Sprite.paintTextWithShadow(temp, "Unlocked", Game.GameWindow.getWidth()/2 - 400, 300, Color.WHITE, 20);

            Sprite.paintTextWithShadow(temp, "Hegemony ships have strong firepower and resistant hulls", 400, Game.GameWindow.getHeight() - 110, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Weapon: Ballistic", 400, Game.GameWindow.getHeight() - 80, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Module: Weapon boost", 400, Game.GameWindow.getHeight() - 50, Color.WHITE, 10);
        }

        ADWButton.getInstance().sprite.render();
        if(ADWButton.getInstance().isHovered()) {

            if(!ADWButton.getInstance().unlockStatus)
                Sprite.paintTextWithShadow(temp, "Unlock cost: 1000", Game.GameWindow.getWidth()/2 - 510, 570, Color.WHITE, 20);
            else
                Sprite.paintTextWithShadow(temp, "Unlocked", Game.GameWindow.getWidth()/2 - 510, 570, Color.WHITE, 20);

            Sprite.paintTextWithShadow(temp, "ADW ships are effective against shields and can teleport short distances", 400, Game.GameWindow.getHeight() - 110, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Weapon: Plasma", 400, Game.GameWindow.getHeight() - 80, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Module: Teleport", 400, Game.GameWindow.getHeight() - 50, Color.WHITE, 10);
        }


        KerthamButton.getInstance().sprite.render();
        if(KerthamButton.getInstance().isHovered()) {

            if(!KerthamButton.getInstance().unlockStatus)
                Sprite.paintTextWithShadow(temp, "Unlock cost: 1000", Game.GameWindow.getWidth()/2 + 100, 570, Color.WHITE, 20);
            else
                Sprite.paintTextWithShadow(temp, "Unlocked", Game.GameWindow.getWidth()/2 + 100, 570, Color.WHITE, 20);

            Sprite.paintTextWithShadow(temp, "Kertham ships have resistant shields and have strong firepower", 400, Game.GameWindow.getHeight() - 110, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Weapon: Ballistic", 400, Game.GameWindow.getHeight() - 80, Color.WHITE, 10);
            Sprite.paintTextWithShadow(temp, "Module: Rapid Healing", 400, Game.GameWindow.getHeight() - 50, Color.WHITE, 10);
        }


        BackButton.getInstance().sprite.render();
    }

    public void renderPauseStateButtons(){
        renderLayer("Background");

        PauseButton.getInstance().sprite.render();
        QuitButton.getInstance().sprite.render();

        renderStats();
    }

}
