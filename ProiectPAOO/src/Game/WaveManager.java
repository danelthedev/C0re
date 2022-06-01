package Game;

import Entities.Asteroid;
import Entities.Enemy;
import EntityAddons.Module.NitroModule;
import EntityAddons.Module.RapidHealingModule;
import EntityAddons.Module.TeleportModule;
import EntityAddons.Module.WeaponBoostModule;
import EntityAddons.Weapon.Weapon;
import Graphics.Camera;
import Interface.Shop;

import java.util.Random;

public class WaveManager {
    public int currentWave;

    private String[] factions = {"ADW", "Absterian", "Hegemony", "Kertham"};
    private String[] weaponTypes = {"ballistic", "plasma", "explosive"};

    private static WaveManager instance;

    private int remainingEnemies, enemyCounter, waveDelayer;
    private boolean startSpawning;

    private WaveManager(){
        waveDelayer = 0;
        currentWave = 0;
    }

    public static WaveManager getInstance(){
        if(instance == null){
            synchronized (Camera.class){
                if(instance == null)
                    instance = new WaveManager();
            }
        }
        return instance;
    }

    public void fullReset(){
        waveDelayer = 0;
        currentWave = 0;
        enemyCounter = 3;
        currentWave -= 1;
        //e nevoie de 2 parcurgeri ca sa fie stersi toti
        for(int i = 0; i < Game.enemies.size(); ++ i) {
            Game.enemies.get(i).delete();
            Game.credits -= 50;
        }
        for(int i = 0; i < Game.enemies.size(); ++ i) {
            Game.enemies.get(i).delete();
            Game.credits -= 50;
        }

        for(int i = 0; i < Game.enemyProj.size(); ++ i)
            Game.enemyProj.get(i).delete();
    }

    public void waveReset(){
        waveDelayer = 0;
        enemyCounter = 3;
        currentWave -= 1;


        //e nevoie de 2 parcurgeri ca sa fie stersi toti
        for(int i = 0; i < Game.enemies.size(); ++ i) {
            Game.enemies.get(i).delete();
            Game.credits -= 50;
        }
        for(int i = 0; i < Game.enemies.size(); ++ i) {
            Game.enemies.get(i).delete();
            Game.credits -= 50;
        }

        for(int i = 0; i < Game.enemyProj.size(); ++ i)
            Game.enemyProj.get(i).delete();
    }

    public void update(){

        remainingEnemies = Game.enemies.size();

        if(remainingEnemies == 0 && waveDelayer == 0){

            Random rand = new Random();
            if(rand.nextFloat() < currentWave * 1) {
                spawnAsteroids();
                System.out.println("Asteroids created");
            }

            startSpawning = true;
            currentWave++;

            if(currentWave % 10 != 0)
                enemyCounter = 3;
            else
                enemyCounter = 1;

            if(currentWave % 5 == 0 && !Shop.isActive)
                Shop.isActive = true;
            waveDelayer = 180;
        }

        if(waveDelayer > 0 && remainingEnemies == 0)
            -- waveDelayer;

        if(startSpawning && !Shop.isActive)
            spawnWave();

    }

    private void spawnAsteroids(){
        int r = (int) (Math.random() * 3);
        for(int i = 0; i < 5; ++ i)
            Game.asteroids.add(new Asteroid(r));
    }

    private void spawnWave(){
        if(currentWave < 10)
            spawnEasyWave();
        else if(currentWave == 10)
            spawnBoss(1);
        else if(currentWave < 20)
            spawnMediumWave();
        else if(currentWave == 20)
            spawnBoss(2);
        else if(currentWave < 30)
            spawnHardWave();
        else if(currentWave == 30)
            spawnBoss(3);
        else
            Game.player.setCurHp(-1);
    }

    private void spawnEasyWave(){
        if(enemyCounter == 0) {
            startSpawning = false;
        }
        else{
            //generate baseline enemy
            Random rand = new Random();
            String faction = factions[rand.nextInt(factions.length)];
            String weaponType = weaponTypes[rand.nextInt(weaponTypes.length)];
            Enemy e = new Enemy(weaponType, faction, false);

            //upgrade weapon
            if(!e.isBoss)
                if(rand.nextFloat() > 0.8) {
                     if(rand.nextFloat() > 0.5)
                        e.getWeapon().activateUpgrade("Mk I A");
                     else
                         e.getWeapon().activateUpgrade("Mk I B");
                }

            Game.enemies.add(e);
            enemyCounter --;
        }

    }

    private void spawnMediumWave(){
        if(enemyCounter == 0){
            startSpawning = false;
        }
        else{
            //generate baseline enemy
            Random rand = new Random();
            String faction = factions[rand.nextInt(factions.length)];
            String weaponType = weaponTypes[rand.nextInt(weaponTypes.length)];
            Enemy e = new Enemy(weaponType, faction, false);

            float dice;
            if(!e.isBoss) {
                //upgrade weapon
                dice = rand.nextFloat();
                //tier 1
                if (dice > 0.1 && dice < 0.8) {
                    if (rand.nextFloat() > 0.5)
                        e.getWeapon().activateUpgrade("Mk I A");
                    else
                        e.getWeapon().activateUpgrade("Mk I B");
                } else
                    //tier 2
                    if (dice > 0.8) {
                        if (rand.nextFloat() > 0.5)
                            e.getWeapon().activateUpgrade("Mk II A");
                        else
                            e.getWeapon().activateUpgrade("Mk II B");
                    }
            }

            //hp modifier
            e.setHpModifier(1.25f);

            //modules
            dice = rand.nextFloat();
            if(dice < 0.1)
                e.setModule(new NitroModule(e));
            else if(dice < 0.2)
                e.setModule(new RapidHealingModule(e.getShield()));
            else if(dice < 0.3)
                e.setModule(new TeleportModule(e));
            else if(dice < 0.4)
                e.setModule(new WeaponBoostModule(e.getWeapon()));

            Game.enemies.add(e);
            enemyCounter --;
        }

    }

    private void spawnHardWave(){
        if(enemyCounter == 0){
            startSpawning = false;
        }
        else{
            //generate baseline enemy
            Random rand = new Random();
            String faction = factions[rand.nextInt(factions.length)];
            String weaponType = weaponTypes[rand.nextInt(weaponTypes.length)];
            Enemy e = new Enemy(weaponType, faction, false);

            //upgrade weapon
            float dice;

            if(!e.isBoss) {
                dice = rand.nextFloat();
                //tier 1
                if (dice < 0.6) {
                    if (rand.nextFloat() > 0.5)
                        e.getWeapon().activateUpgrade("Mk I A");
                    else
                        e.getWeapon().activateUpgrade("Mk I B");
                }
                //tier 2
                else {
                    if (rand.nextFloat() > 0.5)
                        e.getWeapon().activateUpgrade("Mk II A");
                    else
                        e.getWeapon().activateUpgrade("Mk II B");
                }
            }

            //hp modifier
            e.setHpModifier(1.5f);

            //modules
            dice = rand.nextFloat();
            if(dice < 0.15)
                e.setModule(new NitroModule(e));
            else if(dice < 0.3)
                e.setModule(new RapidHealingModule(e.getShield()));
            else if(dice < 0.45)
                e.setModule(new TeleportModule(e));
            else if(dice < 0.6)
                e.setModule(new WeaponBoostModule(e.getWeapon()));

            Game.enemies.add(e);
            enemyCounter --;
        }

    }

    private void spawnBoss(int tier){
        //generate boss enemy
        Random rand = new Random();
        String faction = factions[rand.nextInt(factions.length)];
        String weaponType = weaponTypes[rand.nextInt(weaponTypes.length)];
        Enemy e = new Enemy(weaponType, faction, true);

        //upgrade weapon
        float dice;
        for(Weapon w: e.bossWeapons){
            dice = rand.nextFloat();
            //tier 1
            if (dice < 0.6) {
                if (rand.nextFloat() > 0.5)
                    w.activateUpgrade("Mk I A");
                else
                    w.activateUpgrade("Mk I B");
            }
            //tier 2
            else {
                if (rand.nextFloat() > 0.5)
                    w.activateUpgrade("Mk II A");
                else
                    w.activateUpgrade("Mk II B");
            }
        }

        //hp modifier
        e.setHpModifier(0.05f * tier);

        //modules
        dice = rand.nextFloat();
        if(dice < 0.15)
            e.setModule(new NitroModule(e));
        else if(dice < 0.3)
            e.setModule(new RapidHealingModule(e.getShield()));
        else if(dice < 0.45)
            e.setModule(new TeleportModule(e));
        else if(dice < 0.6)
            e.setModule(new WeaponBoostModule(e.bossWeapons.get(0)));

        Game.enemies.add(e);
        enemyCounter --;
        startSpawning = false;

    }

    public void setWaveDelayer(int delay){
        waveDelayer = delay;
    }
}
