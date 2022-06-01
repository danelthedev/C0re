package EntityAddons.Weapon;

import Entities.ComplexEntity;
import EntityAddons.Projectile.Projectile;
import EntityAddons.Projectile.ProjectileFactory;

import java.util.Random;

public class BallisticWeapon implements Weapon{

    private String upgrade = "";

    private float dmgModifier = 1, cdModifier = 1;

    private ComplexEntity owner;
    private int CD = 30;
    private int maxCD = 60;

    private int shieldBaseDmg = 5;
    private int hullBaseDmg = 15;

    private ProjectileFactory projectileFactory = new ProjectileFactory();

    BallisticWeapon(ComplexEntity owner){
        this.owner = owner;
    }

    public String getUpgrade(){
        return upgrade;
    }

    public void basicShoot(){
        Projectile proj = projectileFactory.getProjectile(this);
        proj.setDmg(hullBaseDmg * dmgModifier, shieldBaseDmg * dmgModifier);
        proj.setSpeed(14);
        proj.setMaxDistanceTraveled(1300);
        CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }

    public void Mk1AShoot(){
        for(int i = -1; i < 2; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * 15;
            proj.setDmg(hullBaseDmg * dmgModifier * 0.85f, shieldBaseDmg * dmgModifier * 0.85f);
            proj.setSpeed(14);
            proj.setMaxDistanceTraveled(1100);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }
}

    public void Mk2AShoot(){
        for(int i = -3; i < 5; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * 7.5f - 4;
            proj.setDmg(hullBaseDmg * dmgModifier * 0.75f, shieldBaseDmg * dmgModifier * 0.75f);
            proj.setSpeed(14);
            proj.setMaxDistanceTraveled(1000);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
        }
    }

    public void Mk1BShoot(){
        for(int i = 0; i < 3; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.setDmg(hullBaseDmg * dmgModifier * 0.85f, shieldBaseDmg * dmgModifier * 0.85f);
            proj.setSpeed(14);
            proj.setMaxDistanceTraveled(1100);
            proj.setActivationDelay(i * 5);
            CD = (int) (maxCD * cdModifier * 1.75) + (int)(Math.random() * 60);
        }
    }

    public void Mk2BShoot(){
        for(int i = 0; i < 9; ++i) {
            Random rand = new Random();
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + rand.nextFloat() * 30 - 15;
            proj.setDmg(hullBaseDmg * dmgModifier * 0.55f, shieldBaseDmg * dmgModifier * 0.55f);
            proj.setSpeed(14);
            proj.setMaxDistanceTraveled(800);
            proj.setActivationDelay((int) (rand.nextFloat() * 7));
            CD = (int) (maxCD * cdModifier * 2.25) + (int)(Math.random() * 60);
        }
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
    public ComplexEntity getOwner() {
        return owner;
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
