package EntityAddons.Weapon;

import Entities.ComplexEntity;
import EntityAddons.Projectile.Projectile;
import EntityAddons.Projectile.ProjectileFactory;

public class ExplosiveWeapon implements Weapon {

    private String upgrade = "";

    private float cdModifier = 1, dmgModifier = 1;

    private ComplexEntity owner;
    private int CD = 30;
    private int maxCD = 60;

    private int shieldBaseDmg = 7;
    private int hullBaseDmg = 7;

    ProjectileFactory projectileFactory = new ProjectileFactory();

    ExplosiveWeapon(ComplexEntity owner){
        this.owner = owner;
    }

    public String getUpgrade(){
        return upgrade;
    }

    public void basicShoot(){
        Projectile proj = projectileFactory.getProjectile(this);
        proj.setDmg(hullBaseDmg * dmgModifier, shieldBaseDmg * dmgModifier);
        proj.setSpeed(10);
        proj.setMaxDistanceTraveled(750);
        CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
    }

    public void Mk1AShoot(){
        basicShoot();
    }

    public void Mk2AShoot(){
        basicShoot();
    }

    public void Mk1BShoot(){
        for(int i = -1; i < 1; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * 15 + 7.5f;
            proj.setDmg(hullBaseDmg * dmgModifier * 0.85f, shieldBaseDmg * dmgModifier * 0.85f);
            proj.setSpeed(8);
            proj.setMaxDistanceTraveled(700);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
        }
    }

    public void Mk2BShoot(){
        for(int i = -2; i < 2; ++i) {
            Projectile proj = projectileFactory.getProjectile(this);
            proj.getTransform().rotation = owner.getTransform().rotation + i * 15 + 7.5f;
            proj.setDmg(hullBaseDmg * dmgModifier * 0.85f, shieldBaseDmg * dmgModifier * 0.85f);
            proj.setSpeed(8);
            proj.setMaxDistanceTraveled(700);
            CD = (int) (maxCD * cdModifier) + (int)(Math.random() * 60);
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
    public int getCD(){
        return CD;
    }

    @Override
    public ComplexEntity getOwner() {
        return owner;
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
