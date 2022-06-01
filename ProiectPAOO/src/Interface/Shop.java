package Interface;

import EntityAddons.Module.NitroModule;
import EntityAddons.Module.RapidHealingModule;
import EntityAddons.Module.TeleportModule;
import EntityAddons.Module.WeaponBoostModule;
import Game.Game;
import Graphics.Camera;
import Graphics.Renderer;
import Graphics.Sprite;
import Interface.Buttons.BackButton;
import Utilitarians.Input;
import Utilitarians.Transform;
import Utilitarians.Utilities;
import Utilitarians.Vector2;
import Game.WaveManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Shop {

    String[] mods = {"Nitro", "Teleport", "Weapon boost", "Rapid Healing"};
    String[] GPUs = {"Bigger engine", "Stronger hull"};

    Transform transform = new Transform(new Vector2(0, 0), 1366, 768);
    public Sprite sprite = new Sprite("./Assets/misc/shopInterface.png", transform);

    Transform temp = new Transform(new Vector2(0, 0), 82, 82);
    public Sprite shopHighlighter = new Sprite("./Assets/misc/shopHighlighter.png", temp);

    public static boolean isActive;
    public static boolean showHighlight;


    public static ShopButton highlightedButton;
    Map<String, ShopButton> shopButtons = new HashMap<>();

    private static Shop instance;
    public static Shop getInstance(){
        if(instance == null){
            synchronized (Shop.class){
                if(instance == null)
                    instance = new Shop();
            }
        }

        return instance;
    }

    private Shop(){
        isActive = false;
        //base weapon
        shopButtons.put("base", new ShopButton(new Vector2(294, 313)));
        shopButtons.get("base").value = Game.player.getWeaponType();
        switch (Game.player.getWeaponType()) {
            case "explosive":
                shopButtons.get("base").description = "Launches rockets that explode on impact";
                break;
            case "plasma":
                shopButtons.get("base").description = "Shoots plasma orbs that are effective against shields";
                break;
            case "ballistic":
                shopButtons.get("base").description = "A ballistic cannon that is effective against hulls";
                break;
            default:
                shopButtons.get("base").description = "I'm error";
                break;
        }

        shopButtons.put("mk1a", new ShopButton(new Vector2(230, 190)));
        shopButtons.get("mk1a").price = 300;
        switch (Game.player.getWeaponType()) {
            case "explosive":
                shopButtons.get("mk1a").description = "Causes sub-explosions after the main explosion";
                break;
            case "plasma":
                shopButtons.get("mk1a").description = "Shoots 6 projectiles around the ship";
                break;
            case "ballistic":
                shopButtons.get("mk1a").description = "Shoots 3 projectiles in a cone";
                break;
            default:
                shopButtons.get("mk1a").description = "I'm error";
                break;
        }

        shopButtons.put("mk2a", new ShopButton(new Vector2(230, 48)));
        shopButtons.get("mk2a").price = 600;
        switch (Game.player.getWeaponType()) {
            case "explosive":
                shopButtons.get("mk2a").description = "Causes even more sub-explosions";
                break;
            case "plasma":
                shopButtons.get("mk2a").description = "Shoots 12 projectiles around the ship";
                break;
            case "ballistic":
                shopButtons.get("mk2a").description = "Shoots 6 projectiles in a cone";
                break;
            default:
                shopButtons.get("mk2a").description = "I'm error";
                break;
        }

        shopButtons.put("mk1b", new ShopButton(new Vector2(358, 190)));
        shopButtons.get("mk1b").price = 300;
        switch (Game.player.getWeaponType()) {
            case "explosive":
                shopButtons.get("mk1b").description = "Shoots 2 rockets that follow enemies";
                break;
            case "plasma":
                shopButtons.get("mk1b").description = "Projectiles create damaging areas after they are destroyed";
                break;
            case "ballistic":
                shopButtons.get("mk1b").description = "Shoots 3 projectiles in a burst";
                break;
            default:
                shopButtons.get("mk1b").description = "I'm error";
                break;
        }

        shopButtons.put("mk2b", new ShopButton(new Vector2(358, 48)));
        shopButtons.get("mk2b").price = 600;
        switch (Game.player.getWeaponType()) {
            case "explosive":
                shopButtons.get("mk2b").description = "Shoots 4 rockets with better following";
                break;
            case "plasma":
                shopButtons.get("mk2b").description = "Damaging areas are bigger and last longer";
                break;
            case "ballistic":
                shopButtons.get("mk2b").description = "Shoots many projectiles spread out";
                break;
            default:
                shopButtons.get("mk2b").description = "I'm error";
                break;
        }

        shopButtons.put("ownMod", new ShopButton(new Vector2(595, 48)));
        shopButtons.get("ownMod").value = Game.player.getModule().getName();
        switch (Game.player.getModule().getName()) {
            case "Nitro":
                shopButtons.get("ownMod").description = "Temporarily gain a speed boost";
                break;
            case "Rapid Healing":
                shopButtons.get("ownMod").description = "Shield recharges faster";
                break;
            case "Teleport":
                shopButtons.get("ownMod").description = "Teleports the ship in the direction its facing";
                break;
            case "Weapon boost":
                shopButtons.get("ownMod").description = "Increases fire rate and damage of weapon";
                break;
            default:
                shopButtons.get("ownMod").description = "I'm error";
                break;
        }

        shopButtons.put("ownModUpgrade", new ShopButton(new Vector2(595, 151)));
        shopButtons.get("ownModUpgrade").value = Game.player.getModule().getUpgrade();

        shopButtons.put("mod", new ShopButton(new Vector2(778, 50)));
        shopButtons.put("modUpgrade1", new ShopButton(new Vector2(904, 50)));
        shopButtons.put("modUpgrade2", new ShopButton(new Vector2(1038, 50)));

        shopButtons.put("ownGpu1", new ShopButton(new Vector2(840, 365)));
        shopButtons.get("ownGpu1").value = Game.player.getGPU(0);

        shopButtons.put("ownGpu2", new ShopButton(new Vector2(974, 366)));
        shopButtons.get("ownGpu2").value = Game.player.getGPU(1);


        shopButtons.put("gpu1", new ShopButton(new Vector2(778, 236)));
        shopButtons.put("gpu2", new ShopButton(new Vector2(908, 236)));
        shopButtons.put("gpu3", new ShopButton(new Vector2(1038, 236)));


        changeStock();
    }

    public void update(){
        //activate

        if(Input.keysState[KeyEvent.VK_CONTROL]) {
            isActive = false;
            changeStock();
        }

        if(isActive){
            WaveManager.getInstance().setWaveDelayer(180);
            //get highlighter position
            shopHighlighter.transform.position = Camera.getInstance().position.add(Input.mousePosition);

            //update shop position
            transform.position = new Vector2(Camera.getInstance().position);
            sprite.transform.copyPosition(transform);

            highlightedButton = checkHighlightedButton();


            checkRemoveUpgrade();
            checkPurchaseUpgrade();
        }

    }

    private ShopButton checkHighlightedButton(){

        Vector2 pos = new Vector2((int) (transform.position.x + Input.mousePosition.x - 8), (int) (transform.position.y + Input.mousePosition.y - 31));

        showHighlight = false;

        for(String key: shopButtons.keySet()){
            ShopButton button = shopButtons.get(key);
            Vector2 buttonRealPos = button.pos.add((int)transform.position.x, (int)transform.position.y);
            if(pos.pointInRectangle(buttonRealPos, buttonRealPos.add(64, 64))) {
                showHighlight = true;
                shopHighlighter.transform.position = buttonRealPos;
                return button;
            }
        }
        return null;

    }

    private void changeStock(){
        Random rand = new Random();

        //change module
        shopButtons.get("mod").value = mods[rand.nextInt(mods.length)];
        shopButtons.get("mod").price = 500;

        //change module upgrades
        int dice;
        switch (shopButtons.get("ownMod").value) {
            case "Nitro":
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade1").value = "CD Reduction " + dice;
                shopButtons.get("modUpgrade1").price = dice * 30;
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade2").value = "Turbo speed " + (rand.nextInt(5) + dice);
                shopButtons.get("modUpgrade2").price = dice * 30;
                break;
            case "Teleport":
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade1").value = "CD Reduction " + dice;
                shopButtons.get("modUpgrade1").price = dice * 30;
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade2").value = "Range " + dice;
                shopButtons.get("modUpgrade2").price = dice * 30;
                break;
            case "Rapid healing":
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade1").value = "CD Reduction " + dice;
                shopButtons.get("modUpgrade1").price = dice * 30;
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade2").value = "Nanomachines " + dice;
                shopButtons.get("modUpgrade2").price = dice * 30;
                break;
            case "Weapon boost":
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade1").value = "Firerate " + dice;
                shopButtons.get("modUpgrade1").price = dice * 30;
                dice = rand.nextInt(5) + 1;
                shopButtons.get("modUpgrade2").value = "Ubercharge " + dice;
                shopButtons.get("modUpgrade2").price = dice * 30;
                break;
        }

        //change GPUs
        dice = rand.nextInt(5) + 1;
        shopButtons.get("gpu1").value = GPUs[rand.nextInt(GPUs.length)] + " " + dice;
        shopButtons.get("gpu1").price = dice * 30;
        dice = rand.nextInt(5) + 1;
        shopButtons.get("gpu2").value = GPUs[rand.nextInt(GPUs.length)] + " " + dice;
        shopButtons.get("gpu2").price = dice * 30;
        dice = rand.nextInt(5) + 1;
        shopButtons.get("gpu3").value = GPUs[rand.nextInt(GPUs.length)] + " " + dice;
        shopButtons.get("gpu3").price = dice * 30;
    }

    private void checkRemoveUpgrade(){

        //remove gpus
        if(highlightedButton == shopButtons.get("ownGpu1") && Input.RMB) {
            shopButtons.get("ownGpu1").value = "";
            Game.player.removeGPU(0);
        }
        if(highlightedButton == shopButtons.get("ownGpu2") && Input.RMB) {
            shopButtons.get("ownGpu2").value = "";
            Game.player.removeGPU(1);
        }

        //remove mod
        if(highlightedButton == shopButtons.get("ownMod") && Input.RMB) {
            shopButtons.get("ownMod").value = "";
            shopButtons.get("ownMod").description = "";
            shopButtons.get("ownModUpgrade").value = "";

            Game.player.removeMod();
        }

        //remove mod upgrade
        if(highlightedButton == shopButtons.get("ownModUpgrade") && Input.RMB) {
            shopButtons.get("ownModUpgrade").value = "";
            if(Game.player.getModule() != null)
                Game.player.getModule().setUpgrade("");
        }


    }

    private void checkPurchaseUpgrade(){

        //purchase gpus
        if(highlightedButton == shopButtons.get("gpu1") && Input.LMB && Game.credits >= shopButtons.get("gpu1").price) {
            //daca primul slot e liber
            if(Game.player.getGPU(0).equals("") && !Game.player.getGPU(1).equals(shopButtons.get("gpu1").value)) {
                shopButtons.get("ownGpu1").value = shopButtons.get("gpu1").value;
                Game.player.addGPU(shopButtons.get("ownGpu1").value, 0);
                Game.credits -= shopButtons.get("gpu1").price;
            }
            //daca al doilea slot e liber
            else if(Game.player.getGPU(1).equals("") && !Game.player.getGPU(0).equals(shopButtons.get("gpu1").value)) {
                shopButtons.get("ownGpu2").value = shopButtons.get("gpu1").value;
                Game.player.addGPU(shopButtons.get("ownGpu2").value, 1);
                Game.credits -= shopButtons.get("gpu1").price;
            }
        }
        if(highlightedButton == shopButtons.get("gpu2") && Input.LMB && Game.credits >= shopButtons.get("gpu2").price) {
            //daca primul slot e liber
            if (Game.player.getGPU(0).equals("") && !Game.player.getGPU(1).equals(shopButtons.get("gpu2").value)) {
                shopButtons.get("ownGpu1").value = shopButtons.get("gpu2").value;
                Game.player.addGPU(shopButtons.get("ownGpu1").value, 0);
                Game.credits -= shopButtons.get("gpu2").price;
            }
            //daca al doilea slot e liber
            else if (Game.player.getGPU(1).equals("") && !Game.player.getGPU(0).equals(shopButtons.get("gpu2").value)) {
                shopButtons.get("ownGpu2").value = shopButtons.get("gpu2").value;
                Game.player.addGPU(shopButtons.get("ownGpu2").value, 1);
                Game.credits -= shopButtons.get("gpu2").price;
            }
        }
        if(highlightedButton == shopButtons.get("gpu3") && Input.LMB && Game.credits >= shopButtons.get("gpu3").price) {
            //daca primul slot e liber
            if (Game.player.getGPU(0).equals("") && !Game.player.getGPU(1).equals(shopButtons.get("gpu3").value)) {
                shopButtons.get("ownGpu1").value = shopButtons.get("gpu3").value;
                Game.player.addGPU(shopButtons.get("ownGpu1").value, 0);
                Game.credits -= shopButtons.get("gpu3").price;
            }
            //daca al doilea slot e liber
            else if (Game.player.getGPU(1).equals("") && !Game.player.getGPU(0).equals(shopButtons.get("gpu3").value)) {
                shopButtons.get("ownGpu2").value = shopButtons.get("gpu3").value;
                Game.player.addGPU(shopButtons.get("ownGpu2").value, 1);
                Game.credits -= shopButtons.get("gpu3").price;
            }
        }

        //purchase mod
        if(highlightedButton == shopButtons.get("mod") && Input.LMB && Game.player.getModule() == null && Game.credits >= shopButtons.get("mod").price) {
            shopButtons.get("ownMod").value = shopButtons.get("mod").value;
            shopButtons.get("ownMod").description = shopButtons.get("mod").description;

            Game.credits -= shopButtons.get("mod").price;

            switch (shopButtons.get("ownMod").value) {
                case "Teleport":
                    Game.player.setModule(new TeleportModule(Game.player));
                    break;
                case "Rapid healing":
                    Game.player.setModule(new RapidHealingModule(Game.player.getShield()));
                    break;
                case "Weapon boost":
                    Game.player.setModule(new WeaponBoostModule(Game.player.getWeapon()));
                    break;
                case "Nitro":
                    Game.player.setModule(new NitroModule(Game.player));
                    break;
            }


            //change module upgrades
            Random rand = new Random();
            switch (shopButtons.get("ownMod").value) {
                case "Nitro":
                    shopButtons.get("modUpgrade1").value = "CD Reduction " + (rand.nextInt(5) + 1);
                    shopButtons.get("modUpgrade2").value = "Turbo speed " + (rand.nextInt(5) + 1);
                    break;
                case "Teleport":
                    shopButtons.get("modUpgrade1").value = "CD Reduction " + (rand.nextInt(5) + 1);
                    shopButtons.get("modUpgrade2").value = "Range " + (rand.nextInt(5) + 1);
                    break;
                case "Rapid healing":
                    shopButtons.get("modUpgrade1").value = "CD Reduction " + (rand.nextInt(5) + 1);
                    shopButtons.get("modUpgrade2").value = "Nanomachines " + (rand.nextInt(5) + 1);
                    break;
                case "Weapon boost":
                    shopButtons.get("modUpgrade1").value = "Firerate " + (rand.nextInt(5) + 1);
                    shopButtons.get("modUpgrade2").value = "Ubercharge " + (rand.nextInt(5) + 1);
                    break;
            }
        }

        //purchase mod upgrade
        if(Game.player.getModule() != null) {
            if (highlightedButton == shopButtons.get("modUpgrade1") && Input.LMB && Game.player.getModule().getUpgrade().equals("") && Game.credits >= shopButtons.get("modUpgrade1").price) {
                shopButtons.get("ownModUpgrade").value = shopButtons.get("modUpgrade1").value;
                Game.player.getModule().setUpgrade(shopButtons.get("modUpgrade1").value);
                Game.credits -= shopButtons.get("modUpgrade1").price;
            }
            if (highlightedButton == shopButtons.get("modUpgrade2") && Input.LMB && Game.player.getModule().getUpgrade().equals("") && Game.credits >= shopButtons.get("modUpgrade2").price) {
                shopButtons.get("ownModUpgrade").value = shopButtons.get("modUpgrade2").value;
                Game.player.getModule().setUpgrade(shopButtons.get("modUpgrade2").value);
                Game.credits -= shopButtons.get("modUpgrade2").price;
            }
        }

        //purchase weapon upgrade
        if(highlightedButton == shopButtons.get("mk1a") && Input.LMB && Game.player.getWeapon().getUpgrade().equals("") && Game.credits >= shopButtons.get("mk1a").price){
            Game.player.getWeapon().activateUpgrade("Mk I A");
            Game.credits -= shopButtons.get("mk1a").price;
        }
        if(highlightedButton == shopButtons.get("mk2a") && Input.LMB && Game.player.getWeapon().getUpgrade().equals("Mk I A") && Game.credits >= shopButtons.get("mk2a").price){
            Game.player.getWeapon().activateUpgrade("Mk II A");
            Game.credits -= shopButtons.get("mk2a").price;
        }
        if(highlightedButton == shopButtons.get("mk1b") && Input.LMB && Game.player.getWeapon().getUpgrade().equals("") && Game.credits >= shopButtons.get("mk1b").price){
            Game.player.getWeapon().activateUpgrade("Mk I B");
            Game.credits -= shopButtons.get("mk1b").price;
        }
        if(highlightedButton == shopButtons.get("mk2b") && Input.LMB && Game.player.getWeapon().getUpgrade().equals("Mk I B") && Game.credits >= shopButtons.get("mk2b").price){
            Game.player.getWeapon().activateUpgrade("Mk II B");
            Game.credits -= shopButtons.get("mk2b").price;
        }

    }

}
