package EntityAddons.Projectile;

import Entities.ComplexEntity;
import EntityAddons.Weapon.Weapon;
import Game.Game;
import Utilitarians.Vector2;

public class ProjectileFactory {

    public ProjectileFactory(){

    }

    public Projectile getProjectile(Weapon creator){

        ComplexEntity owner = creator.getOwner();
        //spawn projectile in center of owner
        Vector2 t = new Vector2();
        t.x = owner.getCenter().x;
        t.y = owner.getCenter().y;

        //create the projectile
        Projectile p = new Projectile("./Assets/projectiles/" + owner.getWeaponType() + owner.getFaction() + ".png", t, owner, creator);

        //rotate it in right direction
        p.getTransform().rotation = owner.getTransform().rotation;

        //add it to the pool of projectiles
        if (owner != Game.player)
            Game.enemyProj.add(p);
        else{
            Game.playerProj.add(p);
        }

        return p;
    }

}
