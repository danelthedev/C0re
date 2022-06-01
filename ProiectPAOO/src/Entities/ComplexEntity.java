package Entities;

import EntityAddons.Module.Module;
import EntityAddons.Shield;
import EntityAddons.Weapon.Weapon;
import Graphics.Camera;
import Utilitarians.*;
import Graphics.Sprite;

public class ComplexEntity extends Entity{

    //player and enemy stuff
    protected Shield shield;
    protected Weapon weapon;
    protected Module module;
    protected float moduleSpeedModifier=1, speedModifier=1, hpModifier=1;
    protected String faction;
    protected String weaponType;
    protected double lastH, lastV;
    protected int trailCd = 0;
    protected AudioPlayer audioPlayer;

    public ComplexEntity(){
        super();
        audioPlayer = new AudioPlayer();
        module = null;
    }

    public Shield getShield() {
        return shield;
    }
    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public Weapon getWeapon() {
        return weapon;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Module getModule() {
        return module;
    }
    public void setModule(Module module) {
        this.module = module;
    }

    public float getModuleSpeedModifier() {
        return moduleSpeedModifier;
    }
    public void setModuleSpeedModifier(float moduleSpeedModifier) {
        this.moduleSpeedModifier = moduleSpeedModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }
    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    public float getHpModifier() {
        return hpModifier;
    }
    public void setHpModifier(float hpModifier) {
        this.hpModifier = hpModifier;
    }

    public String getFaction() {
        return faction;
    }
    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getWeaponType() {
        return weaponType;
    }
    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public double getLastH() {
        return lastH;
    }
    public void setLastH(double lastH) {
        this.lastH = lastH;
    }

    public double getLastV() {
        return lastV;
    }
    public void setLastV(double lastV) {
        this.lastV = lastV;
    }

    public int getTrailCd() {
        return trailCd;
    }
    public void setTrailCd(int trailCd) {
        this.trailCd = trailCd;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
}
