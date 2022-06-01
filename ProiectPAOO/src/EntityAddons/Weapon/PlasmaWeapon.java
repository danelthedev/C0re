package EntityAddons.Weapon;

import Entities.ComplexEntity;
import EntityAddons.Projectile.Projectile;
import EntityAddons.Projectile.ProjectileFactory;

public class PlasmaWeapon implements Weapon{

    private String upgrade = "";

    private float dmgModifier = 1, cdModifier = 1;

    private ComplexEntity owner;
    private int CD = 30;
    private int maxCD = 60;

    private int shieldBaseDmg = 15;
    private int hullBaseDmg = 5;

    private ProjectileFactory projectileFactory = new ProjectileFactory();

    PlasmaWeapon(ComplexEntity owner){
        this.owner = owner;
    }

    public String getUpgrade(){
        return upgrade;
    }

    public void basicShoot(){
        Projectile proj = projectileFactory.getProjectile(this);
        proj.setDmg(hullBaseDmg * dmgModifier, shieldBaseDmg * dmgModifier);
        proj.setSpeed(12);
        proj.setMaxDistanceTraveled(750);
        CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }

    public void Mk1AShoot(){
        for(int i = 0; i < 6; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * (360 / 6);
            proj.setDmg(hullBaseDmg * dmgModifier * 0.95f, shieldBaseDmg * dmgModifier * 0.95f);
            proj.setSpeed(8);
            proj.setMaxDistanceTraveled(600);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
        }
    }

    public void Mk2AShoot(){
        for(int i = 0; i < 12; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * (360 / 12);
            proj.setDmg(hullBaseDmg * dmgModifier * 0.95f, shieldBaseDmg * dmgModifier * 0.95f);
            proj.setSpeed(7);
            proj.setMaxDistanceTraveled(500);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
        }
    }

    public void Mk1BShoot(){
        Projectile proj = projectileFactory.getProjectile(this);
        proj.setDmg(hullBaseDmg * dmgModifier, shieldBaseDmg * dmgModifier);
        proj.setSpeed(12);
        proj.setMaxDistanceTraveled(800);
        CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }

    public void Mk2BShoot(){
        Projectile proj = projectileFactory.getProjectile(this);
        proj.setDmg(hullBaseDmg * dmgModifier, shieldBaseDmg * dmgModifier);
        proj.setSpeed(12);
        proj.setMaxDistanceTraveled(900);
        CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }

    @Override
    public void shoot() {

        if(upgrade.equals(""))
            basicShoot();

        if(upgrade.equals("Mk I A"))
            Mk1AShoot();

        if(upgrade.equals("Mk II A"))
            Mk2AShoot();

        if(upgrade.equals("Mk I B"))
            Mk1BShoot();

        if(upgrade.equals("Mk II B"))
            Mk2BShoot();
    }

    @Override
    public void setMaxCD(int maxCD){
        this.maxCD = maxCD;
    }

    @Override
    public void setCD(){
        this.CD = maxCD;
    }

    @Override
    public int getCD(){
        return CD;
    }

    @Override
    public int getMaxCD(){
        return maxCD;
    }

    @Override
    public void decCD() {
        --CD;
    }

    @Override
    public ComplexEntity getOwner() {
        return owner;
    }

    @Override
    public void setModuleDmgModifier(float modifier) {
        dmgModifier = modifier;
    }

    @Override
    public void setModuleCDModifier(float modifier) {
        cdModifier = modifier;
    }

    @Override
    public void activateUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    @Override
    public float getHullDmg() {
        return hullBaseDmg;
    }

    @Override
    public float getShieldDmg() {
        return shieldBaseDmg;
    }

    @Override
    public void setHullDmg(int hullDmg) {
        this.hullBaseDmg = hullDmg;
    }

    @Override
    public void setShieldDmg(int shieldDmg) {
        this.shieldBaseDmg = shieldDmg;
    }

}
