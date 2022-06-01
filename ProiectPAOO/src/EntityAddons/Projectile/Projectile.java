package EntityAddons.Projectile;

import Entities.ComplexEntity;
import Entities.Player;
import EntityAddons.Weapon.Weapon;
import Game.Game;
import Graphics.Background;
import Graphics.Renderer;
import Graphics.Sprite;
import Utilitarians.Collider;
import Utilitarians.Transform;
import Utilitarians.Utilities;
import Utilitarians.Vector2;

public class Projectile {

    private Vector2 spawnLocation;
    private float distanceTraveled = 0;
    private float maxDistanceTraveled = -1;

    private Transform transform;
    private Sprite sprite;
    private ComplexEntity owner;
    private Collider collider;
    private Vector2 direction;
    private float speed = 10;
    private float hullDmg, shieldDmg;
    private int activationDelay;

    private Weapon creator;
    private ComplexEntity randEnt;

    public Projectile(String spritePath, Vector2 pos, ComplexEntity owner, Weapon creator) {

        this.creator = creator;
        spawnLocation = new Vector2(pos);

        transform = new Transform();

        this.owner = owner;
        collider = new Collider();

        transform.position = pos;

        sprite = new Sprite(spritePath, transform);
        direction = new Vector2(1, 0);
        collider.transform.width = sprite.getWidth();
        collider.transform.height = sprite.getHeight();

        Renderer.getInstance().addToLayer("Projectiles", sprite);

        String audioFilePath = "./Assets/Sound/menuAccept.wav";

        if(owner.getClass() == Player.class) {
            if (Game.enemies.size() > 0) {
                int index = (int) (Math.random() * Game.enemies.size());
                randEnt = Game.enemies.get(index);
            } else {
                randEnt = null;
            }
        }
        else if(Game.player != null)
            randEnt = Game.player;


    }

    public void update(){

        if(activationDelay > 0){

            transform.position.x = owner.getCenter().x;
            transform.position.y = owner.getCenter().y;
            --activationDelay;
            spawnLocation.x = transform.position.x;
            spawnLocation.y = transform.position.y;

        }
        else {

            heatseeker();

            direction.x = Math.cos(Math.toRadians(transform.rotation));
            direction.y = Math.sin(Math.toRadians(transform.rotation));

            travel();
        }
    }

    private void heatseeker(){
        float seekingRotation = 0;

        if(creator.getUpgrade().equals("Mk II B"))
            seekingRotation = 1.5f;
        else if(creator.getUpgrade().equals("Mk I B"))
            seekingRotation = 0.5f;

        if (owner.getWeaponType().equals("explosive") && randEnt != null) {
            if(Transform.convertAngle(transform.rotation) > Transform.convertAngle(transform.position.getAngle(randEnt.getTransform().position)))
                transform.rotation -= seekingRotation;
            else
                transform.rotation += seekingRotation;
        }
    }

    private void travel(){
        transform.position.x += direction.x * speed;
        transform.position.y += direction.y * speed;

        sprite.transform.copyTransform(transform);
        collider.transform.copyPosition(transform);

        distanceTraveled = spawnLocation.distanceTo(transform.position);
        if(maxDistanceTraveled != -1) {
            sprite.transform.alpha = Utilities.clamp(1 - distanceTraveled/maxDistanceTraveled, 0, 1);

            if(distanceTraveled > maxDistanceTraveled)
                this.delete();
        }

        if (transform.position.x < 0 || transform.position.x > Background.getInstance().getWidth() || transform.position.y < 0 || transform.position.y > Background.getInstance().getHeight()) {
            this.delete();
        }

    }

    private void plasmaDelete(){

        if((creator.getUpgrade().equals("Mk I B") || creator.getUpgrade().equals("Mk II B")) && owner.getWeaponType().equals("plasma")){
            PlasmaAOE aoe;
            if(creator.getUpgrade().equals("Mk I B"))
                aoe = new PlasmaAOE("./Assets/Projectiles/AOE.png", new Vector2(transform.position), owner, 1);
            else
                aoe = new PlasmaAOE("./Assets/Projectiles/AOE.png", new Vector2(transform.position), owner, 2);

            if(owner.getClass() == Player.class){
                Game.PplasmaAOEs.add(aoe);
            }
            else
                Game.EplasmaAOEs.add(aoe);
        }

    }

    private void explosiveDelete(){

        if(owner.getWeaponType().equals("explosive")){
            ExplosionAOE aoe;

            if(creator.getUpgrade().equals("Mk I A"))
                aoe = new ExplosionAOE("./Assets/Projectiles/explosionAOE.png", new Vector2(transform.position), owner, 1);
            else
            if(creator.getUpgrade().equals("Mk II A"))
                aoe = new ExplosionAOE("./Assets/Projectiles/explosionAOE.png", new Vector2(transform.position), owner, 2);
            else
                aoe = new ExplosionAOE("./Assets/Projectiles/explosionAOE.png", new Vector2(transform.position), owner, 0);

            if(owner.getClass() == Player.class){
                Game.PExplosionAOEs.add(aoe);
            }
            else
                Game.EExplosionAOEs.add(aoe);
        }

    }

    public void delete(){

        plasmaDelete();
        explosiveDelete();

        Game.playerProj.remove(this);
        Game.enemyProj.remove(this);
        Renderer.getInstance().renderLayers.get("Projectiles").remove(sprite);
        System.gc();
    }

    public void setActivationDelay(int delay){
        this.activationDelay = delay;
    }

    public void setHullDmg(float hullDmg){
        this.hullDmg = hullDmg;
    }
    public float getHullDmg(){
        return hullDmg;
    }
    public void setShieldDmg(float shieldDmg){
        this.shieldDmg = shieldDmg;
    }
    public float getShieldDmg(){
        return shieldDmg;
    }

    public void setDmg(float hullDmg, float shieldDmg){
        setHullDmg(hullDmg);
        setShieldDmg(shieldDmg);
    }
    public void setSpeed(float speed) {this.speed = speed;}

    public Vector2 getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Vector2 spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(float distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public float getMaxDistanceTraveled() {
        return maxDistanceTraveled;
    }

    public void setMaxDistanceTraveled(float maxDistanceTraveled) {
        this.maxDistanceTraveled = maxDistanceTraveled;
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

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public int getActivationDelay() {
        return activationDelay;
    }

    public Weapon getCreator() {
        return creator;
    }

    public void setCreator(Weapon creator) {
        this.creator = creator;
    }

    public ComplexEntity getRandEnt() {
        return randEnt;
    }

    public void setRandEnt(ComplexEntity randEnt) {
        this.randEnt = randEnt;
    }
}
