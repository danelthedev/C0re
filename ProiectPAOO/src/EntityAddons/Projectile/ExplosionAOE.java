package EntityAddons.Projectile;

import Entities.ComplexEntity;
import Entities.Player;
import Game.Game;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Collider;
import Utilitarians.Transform;
import Utilitarians.Utilities;
import Utilitarians.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class ExplosionAOE {

    private Transform transform;
    private Sprite sprite;
    private ComplexEntity owner;
    private Collider collider;
    private float hullDmg = 4f, shieldDmg = 4f;

    private int maxSubexplosions, subexplosions;
    private ArrayList<ComplexEntity> targetsHit = new ArrayList<>();

    public ExplosionAOE(String spritePath, Vector2 pos, ComplexEntity owner, int tier) {

        transform = new Transform();
        transform.position = pos;

        maxSubexplosions = (int) Utilities.clamp(tier * 2, 0, 4);
        subexplosions = maxSubexplosions;

        this.owner = owner;
        collider = new Collider();


        sprite = new Sprite(spritePath, transform);

        collider.transform.copyPosition(transform);
        collider.transform.width = (int) (sprite.getWidth() * sprite.transform.x_scale);
        collider.transform.height = (int) (sprite.getHeight() * sprite.transform.y_scale);

        Renderer.getInstance().addToLayer("Projectiles", sprite);
    }

    public void update() {
        sprite.transform.alpha = Utilities.clamp((float) (sprite.transform.alpha - 0.1), 0, 1);

        if(sprite.transform.alpha < (float)subexplosions / maxSubexplosions - 0.2 && subexplosions > 0){
            Random rand = new Random();
            Vector2 newPos = new Vector2(transform.position);
            newPos.x += rand.nextInt(60) - 30;
            newPos.y += rand.nextInt(60) - 30;

            subexplosions --;
            ExplosionAOE aoe = new ExplosionAOE("./Assets/projectiles/explosionAOE.png", newPos, owner, 0);

            if(owner.getClass() == Player.class)
                Game.PExplosionAOEs.add(aoe);
            else
                Game.EExplosionAOEs.add(aoe);

        }

        if(sprite.transform.alpha == 0)
            delete();
    }
    public void delete(){

        Game.EExplosionAOEs.remove(this);
        Game.PExplosionAOEs.remove(this);
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

    public int getMaxSubexplosions() {
        return maxSubexplosions;
    }
    public void setMaxSubexplosions(int maxSubexplosions) {
        this.maxSubexplosions = maxSubexplosions;
    }

    public int getSubexplosions() {
        return subexplosions;
    }
    public void setSubexplosions(int subexplosions) {
        this.subexplosions = subexplosions;
    }

    public ArrayList<ComplexEntity> getTargetsHit() {
        return targetsHit;
    }
    public void setTargetsHit(ArrayList<ComplexEntity> targetsHit) {
        this.targetsHit = targetsHit;
    }

}
