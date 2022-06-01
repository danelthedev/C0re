package EntityAddons.Projectile;

import Entities.ComplexEntity;
import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Collider;
import Utilitarians.Transform;
import Utilitarians.Utilities;
import Utilitarians.Vector2;

public class PlasmaAOE {

    private Transform transform;
    private Sprite sprite;
    private ComplexEntity owner;
    private Collider collider;
    private float hullDmg = 0.05f, shieldDmg = 0.05f;
    private float alphaDec;

    public PlasmaAOE(String spritePath, Vector2 pos, ComplexEntity owner, int tier) {

        transform = new Transform();
        transform.position = pos;

        if(tier == 1){
            alphaDec = 0.02f;
            transform.x_scale = 1;
            transform.y_scale = 1;
        }
        else{
            alphaDec = 0.01f;
            transform.x_scale = 2.25f;
            transform.y_scale = 2.25f;
        }

        this.owner = owner;
        collider = new Collider();


        sprite = new Sprite(spritePath, transform);

        collider.transform.copyPosition(transform);
        collider.transform.width = (int) (sprite.getWidth() * sprite.transform.x_scale);
        collider.transform.height = (int) (sprite.getHeight() * sprite.transform.y_scale);

        Renderer.getInstance().addToLayer("Projectiles", sprite);
    }

    public void update() {
        sprite.transform.alpha = Utilities.clamp((float) (sprite.transform.alpha - 0.01), 0, 1);
        if(sprite.transform.alpha == 0)
            delete();
    }


    public void delete(){
        Game.PplasmaAOEs.remove(this);
        Game.EplasmaAOEs.remove(this);
        Renderer.getInstance().renderLayers.get("Projectiles").remove(sprite);
        System.gc();
    }


    public float getShieldDmg(){
        return shieldDmg;
    }
    public float getHullDmg(){
        return hullDmg;
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

    public ComplexEntity getOwner() {
        return owner;
    }
    public void setOwner(ComplexEntity owner) {
        this.owner = owner;
    }

    public Collider getCollider() {
        return collider;
    }
    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public void setHullDmg(float hullDmg) {
        this.hullDmg = hullDmg;
    }
    public void setShieldDmg(float shieldDmg) {
        this.shieldDmg = shieldDmg;
    }

    public float getAlphaDec() {
        return alphaDec;
    }
    public void setAlphaDec(float alphaDec) {
        this.alphaDec = alphaDec;
    }
}
