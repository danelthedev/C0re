package Entities;

import EntityAddons.Module.Module;
import EntityAddons.Shield;
import EntityAddons.Trail;
import EntityAddons.Weapon.Weapon;
import EntityAddons.Weapon.WeaponFactory;
import Game.Game;
import Graphics.Background;
import Graphics.Renderer;
import Utilitarians.*;
import Graphics.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends ComplexEntity {

    private static String hullPath = "./Assets/ships/ship";
    private WeaponFactory weaponFactory = new WeaponFactory();

    public boolean isBoss;
    public ArrayList<Weapon> bossWeapons;


    public Enemy(String weaponType, String faction, boolean isBoss){
        super();
        setSpawnLocation();


        this.isBoss = isBoss;

        baseMaxHp = 40;
        maxHp = baseMaxHp;
        curHp = maxHp;

        baseMaxSpeed = 2;
        maxSpeed = baseMaxSpeed;

        this.faction = faction;
        this.weaponType = weaponType;

        //create weapon
        if(!isBoss) {
            weapon = weaponFactory.getWeapon(this);
            weapon.setHullDmg((int) (weapon.getHullDmg() * 0.5));
            weapon.setShieldDmg((int) (weapon.getShieldDmg() * 0.5));
            weapon.setMaxCD(weapon.getMaxCD() * 2);
        }
        else {
            bossWeapons = new ArrayList<>();
            for(int i = 0; i < 3; ++ i){
                Weapon temp;
                temp = weaponFactory.getWeapon(this);
                temp.setHullDmg((int) (temp.getHullDmg() * 0.5));
                temp.setShieldDmg((int) (temp.getShieldDmg() * 0.5));
                temp.setMaxCD(temp.getMaxCD() * 2);
                bossWeapons.add(temp);
            }
        }

        acceleration = 0.25f;


        //create hull and shield

        Random rand = new Random();
        int hullType = rand.nextInt(3) + 1;

        sprite = new Sprite(hullPath + faction + hullType + ".png", transform);

        shield = new Shield(25f, 0.3f, 6, this);

        transform.height = sprite.getHeight();
        transform.width = sprite.getWidth();
        Renderer.getInstance().addToLayer("Enemies", sprite);

    }


    private void setSpawnLocation(){
        Random rand = new Random();

        switch (rand.nextInt(4)) {//top
            case 0:
                transform.position.x = Math.random() * Background.getInstance().getWidth();
                transform.position.y = -transform.height * 2;
                break;
//left
            case 1:
                transform.position.x = -transform.width * 2;
                transform.position.y = Math.random() * Background.getInstance().getHeight();
                break;
//down
            case 2:
                transform.position.x = Math.random() * Background.getInstance().getWidth();
                transform.position.y = Background.getInstance().getHeight() + transform.height * 2;
                break;
//right
            case 3:
                transform.position.x = Background.getInstance().getWidth() + transform.width * 2;
                transform.position.y = Math.random() * Background.getInstance().getHeight();
                break;
        }
    }

    public void update(){

        //actual moving
        if(Game.isPlayerAlive) {
            lookAtPlayer();
            move();
        }

        transform.position.x += direction.x;
        transform.position.y += direction.y;

        //update sprite position
        sprite.transform.copyTransform(this.transform);
        //update collider position
        collider.transform.copyTransform(this.transform);
        //update shield
        shield.update();
        //update module
        if(module != null)
            module.update();

        //update weapon

        if(!isBoss) {
            if (weapon.getCD() <= 0) {
                weapon.shoot();
                audioPlayer.play("./Assets/Sound/menuAccept.wav");
            } else weapon.decCD();
        }
        else{
            for(Weapon w: bossWeapons)
                if (w.getCD() <= 0) {
                    w.shoot();
                    audioPlayer.play("./Assets/Sound/menuAccept.wav");
                } else w.decCD();
        }


        audioPlayer.update();

        if(curHp <= 0)
            delete();

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
        System.out.println("enemy deleted");
        Game.credits += 50;

        //delete shield sprite from render
        Renderer.getInstance().renderLayers.get("Shields").remove(shield.getSprite());

        Game.enemies.remove(this);
        Renderer.getInstance().renderLayers.get("Enemies").remove(sprite);
        System.gc();
    }

    public void lookAtPlayer(){
        transform.rotation = this.getCenter().getAngle(Game.player.getCenter());
        transform.rotation = Transform.convertAngle(transform.rotation);
    }


    public void move(){
        if(trailCd > 0)
            trailCd --;

        double h = Math.cos(Math.toRadians(transform.rotation));
        double v = Math.sin(Math.toRadians(transform.rotation));
        if(transform.position.distanceTo(Game.player.transform.position) > 600){
            direction.x = Utilities.clamp((float) (direction.x + (h * acceleration)), (float) (-maxSpeed * h), (float) (maxSpeed * h));
            direction.y = Utilities.clamp((float) (direction.y + (v * acceleration)), (float) (maxSpeed * v), (float) (-maxSpeed * v));
            lastH = h;
            lastV = v;


            //create trail
            if(trailCd <= 0) {

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
            direction.x = Utilities.clamp((float) (direction.x - Math.signum(direction.x) * (Math.abs(lastH) * acceleration / 10)), -maxSpeed, maxSpeed);
            direction.y = Utilities.clamp((float) (direction.y - Math.signum(direction.y) * (Math.abs(lastV) * acceleration / 10)), -maxSpeed, maxSpeed);
        }


        transform.position.x += direction.x;
        transform.position.y += direction.y;
    }

}
