package EntityAddons;

import Entities.ComplexEntity;
import Entities.Entity;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Utilities;


public class Shield {

    private float moduleRechargeRateModifier, moduleRechargeDelayModifier;

    private String spritePath = "./Assets/shields/shield";

    private ComplexEntity owner;

    private Transform transform;
    private Sprite sprite;

    private float maxHp;
    private float curHp;

    private float rechargeRate;
    private float rechargeDelay;

    private float curDelay;

    public Shield(float maxHp, float rechargeRate, float rechargeDelay, ComplexEntity owner){

        this.moduleRechargeRateModifier = 1f;
        this.moduleRechargeDelayModifier = 1f;
        this.owner = owner;
        this.transform = new Transform();
        this.sprite = new Sprite(spritePath + owner.getFaction() + ".png", transform);
        this.maxHp = maxHp;
        this.rechargeRate = rechargeRate;
        this.rechargeDelay = rechargeDelay * 60; //amplific cu 60 ca sa obtin valoarea din secunde in frameuri

        Renderer.getInstance().addToLayer("Shields", sprite);
    }

    public void update(){


        transform.copyPosition(owner.getTransform());
        transform.rotation = owner.getTransform().rotation;

        sprite.transform.copyTransform(this.transform);
        sprite.transform.alpha = curHp / maxHp;

        if(curDelay > 0)
            curDelay --;
        else
            curHp = Utilities.clamp(curHp + rechargeRate * moduleRechargeRateModifier, 0, maxHp);
    }

    public void activateDelay(){
        curDelay = rechargeDelay * moduleRechargeDelayModifier;
    }

    public void hit(float dmg){
        curHp = Utilities.clamp(curHp - dmg, 0, maxHp);
    }

    public float getMaxHp(){
        return maxHp;
    }

    public float getCurHp(){
        return curHp;
    }

    public float getRechargeDelay() {
        return rechargeDelay;
    }

    public float getRechargeRate() {
        return rechargeRate;
    }

    public void setRechargeDelay(float rechargeDelay) {
        this.rechargeDelay = rechargeDelay;
    }

    public void setRechargeRate(float rechargeRate) {
        this.rechargeRate = rechargeRate;
    }

    public float getModuleRechargeRateModifier() {
        return moduleRechargeRateModifier;
    }
    public void setModuleRechargeRateModifier(float moduleRechargeRateModifier) {
        this.moduleRechargeRateModifier = moduleRechargeRateModifier;
    }
    public float getModuleRechargeDelayModifier() {
        return moduleRechargeDelayModifier;
    }
    public void setModuleRechargeDelayModifier(float moduleRechargeDelayModifier) {
        this.moduleRechargeDelayModifier = moduleRechargeDelayModifier;
    }

    public String getSpritePath() {
        return spritePath;
    }
    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public ComplexEntity getOwner(){return owner;}
    public void setOwner(ComplexEntity owner) {
        this.owner = owner;
    }

    public Transform getTransform() {
        return transform;
    }
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Sprite getSprite() {
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }
    public void setCurHp(float curHp) {
        this.curHp = curHp;
    }

    public float getCurDelay() {
        return curDelay;
    }
    public void setCurDelay(float curDelay) {
        this.curDelay = curDelay;
    }

}
