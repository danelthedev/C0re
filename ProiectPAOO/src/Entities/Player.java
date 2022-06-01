package Entities;

import Exceptions.InvalidPathException;
import EntityAddons.Module.*;
import EntityAddons.Module.Module;
import EntityAddons.Shield;
import EntityAddons.Trail;
import EntityAddons.Weapon.Weapon;
import EntityAddons.Weapon.WeaponFactory;
import Game.Game;
import Graphics.Background;
import Graphics.Camera;
import Graphics.Renderer;
import Interface.Shop;
import Utilitarians.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Player extends ComplexEntity {

    private String[] GPUs;
    private WeaponFactory weaponFactory = new WeaponFactory();

    public Player(){
        super();

        acceleration = 0.2f;

        faction = Game.playerFaction;
        transform.position = new Vector2(Background.getInstance().getWidth() / 2, Background.getInstance().getHeight() / 2);

        GPUs = new String[2];
        GPUs[0] = "";
        GPUs[1] = "";

        baseMaxHp = 100;
        maxHp = baseMaxHp;
        curHp = baseMaxHp;

        baseMaxSpeed = 7;
        maxSpeed = baseMaxSpeed;

        collider.transform.width = sprite.getWidth();
        collider.transform.height = sprite.getHeight();

        transform.height = sprite.getHeight();
        transform.width = sprite.getWidth();

        switch (Game.playerFaction) {
            case "ADW":
                weaponType = "plasma";
                weapon = weaponFactory.getWeapon(this);
                shield = new Shield(50f, 0.5f, 2, this);
                module = new TeleportModule(this);
                break;
            case "Absterian":
                weaponType = "explosive";
                weapon = weaponFactory.getWeapon(this);
                shield = new Shield(50f, 0.5f, 2, this);
                module = new NitroModule(this);
                break;
            case "Hegemony":
                weaponType = "ballistic";
                weapon = weaponFactory.getWeapon(this);
                shield = new Shield(50f, 0.5f, 2, this);
                module = new WeaponBoostModule(weapon);
                break;
            case "Kertham":
                weaponType = "ballistic";
                weapon = weaponFactory.getWeapon(this);
                shield = new Shield(50f, 0.5f, 2, this);
                module = new RapidHealingModule(shield);
                break;
        }
        weapon.setMaxCD(30);

        Random rand = new Random();

        String path ="./Assets/ships/ship" + Game.playerFaction + (rand.nextInt(3) + 1) + ".png";
        try {
            sprite.setSprite(path);
        }catch (InvalidPathException e){
            e.printStackTrace();
        }

        Renderer.getInstance().addToLayer("Player", sprite);

    }

    public void removeMod(){
        module = null;
    }

    public String getGPU(int index){
        return GPUs[index];
    }
    public void addGPU(String GPU, int index){
        GPUs[index] = GPU;
    }
    public void removeGPU(int index){
        GPUs[index] = "";
    }

    private void resetGPUmodifiers(){
        hpModifier = 1;
        speedModifier = 1;
    }

    private void interpretGPus(){
        for(String GPU: GPUs){
            if(GPU.contains("Stronger hull"))
                hpModifier = 1 + Integer.parseInt(GPU.replaceAll("[\\D]", "")) / 10f;
            if(GPU.contains("Bigger engine"))
                speedModifier = 1 + Integer.parseInt(GPU.replaceAll("[\\D]", "")) / 10f;
        }

        maxHp = baseMaxHp * hpModifier;
        maxSpeed = baseMaxSpeed * speedModifier * moduleSpeedModifier;
    }

    public void update() {

        //parse GPUs
        resetGPUmodifiers();
        interpretGPus();

        if(module != null)
            module.update();

        audioPlayer.update();

        //update sprite position
        sprite.transform.copyTransform(transform);
        //update shield
        shield.update();
        //update collider position
        collider.transform.copyPosition(this.transform);

        if(!Shop.isActive){
            //move ship
            movement();
            //get attack input
            attack();
        }

        if(curHp <= 0){
            Game.isPlayerAlive = false;
            this.delete();
        }
    }


    public void calcDirection(){

        //get rotation

        if(Game.controlType == Game.ControlType.KeyboardControl){
            float rotationSpeed = 2;
            if (Game.keysState[KeyEvent.VK_A])
                transform.rotation -= rotationSpeed;

            if (Game.keysState[KeyEvent.VK_D])
                transform.rotation += rotationSpeed;

            transform.rotation = Transform.convertAngle(transform.rotation);
        }
        else{
            transform.rotation = this.getCenter().getAngle(Input.mousePosition.add(Camera.getInstance().position));
            transform.rotation = Transform.convertAngle(transform.rotation);
        }


        double h = Math.cos(Math.toRadians(transform.rotation));
        double v = Math.sin(Math.toRadians(transform.rotation));

        //acceleration logic

        if(Game.keysState[KeyEvent.VK_W] && Game.controlType == Game.ControlType.KeyboardControl
            || Input.RMB && Game.controlType == Game.ControlType.MouseControl){
            direction.x = Utilities.clamp((float) (direction.x + (h * acceleration)), (float) (-maxSpeed * h), (float) (maxSpeed * h));
            direction.y = Utilities.clamp((float) (direction.y + (v * acceleration)), (float) (maxSpeed * v), (float) (-maxSpeed * v));
            lastH = h;
            lastV = v;

            //create trail
            if(trailCd <= 0){

                Transform t = new Transform();
                t.copyTransform(transform);
                t.position.x -= 64 * Math.cos(Math.toRadians(t.rotation));
                t.position.y -= 64 * Math.sin(Math.toRadians(t.rotation));

                Trail trail = new Trail(Trail.trails.get(getFaction()), t);

                Game.trails.add(trail);
                trailCd = 1;
            }

        }
        else{
            //make direction go towards 0
            direction.x = Utilities.clamp((float) (direction.x - Math.signum(direction.x) * (Math.abs(lastH) * acceleration / 2)), -maxSpeed, maxSpeed);
            direction.y = Utilities.clamp((float) (direction.y - Math.signum(direction.y) * (Math.abs(lastV) * acceleration / 2)), -maxSpeed, maxSpeed);

            //when close to 0, give exactly 0
            if(Math.abs(direction.x) < 0.1)
                direction.x = 0;
            if(Math.abs(direction.y) < 0.1)
                direction.y = 0;
        }

        if(trailCd > 0)
            --trailCd;

    }

    public void movement(){

        calcDirection();
        transform.position.x += direction.x;
        transform.position.y += direction.y;

        if(transform.position.x < 0)
            transform.position.x = Background.getInstance().getWidth() - 64;
        else if(transform.position.x > Background.getInstance().getWidth() - 64)
            transform.position.x = 0;

        if(transform.position.y < 0)
            transform.position.y = Background.getInstance().getHeight() - 64;
        else if(transform.position.y > Background.getInstance().getHeight() - 64)
            transform.position.y = 0;


    }

    public void attack() {

        if((Game.keysState[KeyEvent.VK_SPACE] && Game.controlType == Game.ControlType.KeyboardControl)
            || (Input.LMB && Game.controlType == Game.ControlType.MouseControl))
            if(weapon.getCD() <= 0){
                weapon.shoot();
                audioPlayer.play("./Assets/Sound/menuAccept.wav");
            }

        weapon.decCD();
    }

    public void hit(float dmg){
        if(shield.getCurHp() > 0) {
            float dif = dmg - shield.getCurHp();
            shield.hit(dmg);
            shield.activateDelay();

            if(dif > 0)
                curHp = Utilities.clamp(curHp - dif, 0, maxHp);

        }
        else
            curHp = Utilities.clamp(curHp - dmg, 0, maxHp);
    }

    public void hit(float hullDmg, float shieldDmg){
        if(shield.getCurHp() > 0) {
            float dif = shieldDmg - shield.getCurHp();
            shield.hit(shieldDmg);
            shield.activateDelay();

            if(dif > 0)
                curHp = Utilities.clamp(curHp - hullDmg, 0, maxHp);

        }
        else
            curHp = Utilities.clamp(curHp - hullDmg, 0, maxHp);
    }

    public void delete(){
        Renderer.getInstance().renderLayers.get("Shields").remove(shield.getSprite());
        Game.isPlayerAlive = false;
        Game.player = null;
        Renderer.getInstance().renderLayers.get("Player").remove(sprite);
        System.gc();
    }
}
