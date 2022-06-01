package Utilitarians;

import Entities.Enemy;
import EntityAddons.Projectile.ExplosionAOE;
import EntityAddons.Projectile.PlasmaAOE;
import EntityAddons.Projectile.Projectile;
import Game.Game;
import Entities.Asteroid;

public class CollisionMaster {

    static float speedDmgThreshold = 100;
    static float impactDmg = 10;

    public static void checkCollisions(){
        if(Game.isPlayerAlive) {
            playerCollisions();
            enemyCollisions();
        }
    }

    private static void playerCollisions(){

        //iterate asteroids
        for(Asteroid a : Game.asteroids)
            if(Game.player.getCollider().isColliding(a.getCollider()) && !a.hasCollided) {

                //calculam gravitatea impactului
                //sistem de coliziuni: revizia 1 (equal mass)

                //calculam impulsul in sens opus
                Vector2 impulsP = new Vector2((Game.player.getDirection().x - a.getDirection().x) / 2, (Game.player.getDirection().y - a.getDirection().y) / 2);
                Vector2 impulsA = new Vector2((a.getDirection().x - Game.player.getDirection().x) / 2, (a.getDirection().y- Game.player.getDirection().y) / 2);

                //scadem impulsul din vectorul de directie
                Game.player.setDirection(Game.player.getDirection().sub(impulsP));
                a.setDirection(a.getDirection().sub(impulsA));

                Game.player.getShield().hit(10);
                Game.player.getShield().activateDelay();

                a.removeDefDir();

                //ne asiguram ca hitboxurile nu mai sunt suprapuse (metoda bruta)

                while(Game.player.getCollider().isColliding(a.getCollider())) {
                    if(a.getCollider().transform.position.x < Game.player.getCollider().transform.position.x)
                        a.getCollider().transform.position.x --;
                    else
                    if(a.getCollider().transform.position.x > Game.player.getCollider().transform.position.x)
                        a.getCollider().transform.position.x ++;

                    if(a.getCollider().transform.position.y < Game.player.getCollider().transform.position.y)
                        a.getCollider().transform.position.y --;
                    else
                    if(a.getCollider().transform.position.y > Game.player.getCollider().transform.position.y)
                        a.getCollider().transform.position.y ++;
                }

                Game.player.getTransform().copyPosition(Game.player.getCollider().transform);
                a.getTransform().copyPosition(a.getCollider().transform);
            }else a.hasCollided = false;

        //iterate enemies
        for(Enemy e : Game.enemies)
            if(Game.player.getCollider().isColliding(e.getCollider())) {

                //calculam gravitatea impactului
                //sistem de coliziuni: revizia 1 (equal mass)

                //calculam impulsul in sens opus in functie de viteza la momentul coliziunii
                float playerSpd = Game.player.getDirection().abs();
                float enemySpd = e.getDirection().abs();


                Vector2 impulsP = new Vector2((Game.player.getDirection().x - e.getDirection().x) / 2, (Game.player.getDirection().y - e.getDirection().y) / 2);
                Vector2 impulsE = new Vector2((e.getDirection().x - Game.player.getDirection().x) / 2, (e.getDirection().y- Game.player.getDirection().y) / 2);

                //scadem impulsul din vectorul de directie
                Game.player.setDirection(Game.player.getDirection().sub(impulsP));
                e.setDirection(e.getDirection().sub(impulsE));

                Game.player.hit( Utilities.clamp(enemySpd - playerSpd, 0, 100) * (enemySpd / speedDmgThreshold) * impactDmg);
                e.hit(Utilities.clamp(playerSpd - enemySpd, 0, 100) * (playerSpd / speedDmgThreshold) * impactDmg);

                //ne asiguram ca hitboxurile nu mai sunt suprapuse (metoda bruta)

                while(Game.player.getCollider().isColliding(e.getCollider())) {
                    if(e.getCollider().transform.position.x < Game.player.getCollider().transform.position.x)
                        e.getCollider().transform.position.x --;
                    else
                    if(e.getCollider().transform.position.x > Game.player.getCollider().transform.position.x)
                        e.getCollider().transform.position.x ++;

                    if(e.getCollider().transform.position.y < Game.player.getCollider().transform.position.y)
                        e.getCollider().transform.position.y --;
                    else
                    if(e.getCollider().transform.position.y > Game.player.getCollider().transform.position.y)
                        e.getCollider().transform.position.y ++;
                }

                Game.player.getTransform().copyPosition(Game.player.getCollider().transform);
                e.getTransform().copyPosition(e.getCollider().transform);
            }

        //iterate enemy projectiles
        for (Projectile p : Game.enemyProj)
            if (Game.player.getCollider().isColliding(p.getCollider())) {
                Game.player.hit(p.getHullDmg(), p.getShieldDmg());
                p.delete();
                break;
            }

        //iterate player explosion AOEs
        for (ExplosionAOE e : Game.EExplosionAOEs)
            if (Game.player.getCollider().isColliding(e.getCollider()) && !e.getTargetsHit().contains(Game.player)) {
                Game.player.hit(e.getHullDmg(), e.getShieldDmg());
                e.getTargetsHit().add(Game.player);
                break;
            }

        //iterate enemy plasma AOEs
        for (PlasmaAOE p : Game.EplasmaAOEs)
            if (Game.player.getCollider().isColliding(p.getCollider())) {
                Game.player.hit(p.getHullDmg(), p.getShieldDmg());
                break;
            }
    }

    private static void enemyCollisions(){

        //iterate through enemies
        for(Enemy e: Game.enemies) {
            //iterate player projectiles
            for (Projectile p : Game.playerProj)
                if (p.getCollider().isColliding(e.getCollider())) {
                    e.hit(p.getHullDmg(), p.getShieldDmg());
                    p.delete();
                    break;
                }

            //iterate player explosion AOEs
            for (ExplosionAOE p : Game.PExplosionAOEs)
                if (e.getCollider().isColliding(p.getCollider()) && !p.getTargetsHit().contains(e)) {
                    e.hit(p.getHullDmg(), p.getShieldDmg());
                    p.getTargetsHit().add(e);
                    break;
                }

            //iterate player plasma AOEs
            for (PlasmaAOE p : Game.PplasmaAOEs)
                if (e.getCollider().isColliding(p.getCollider())) {
                    e.hit(p.getHullDmg(), p.getShieldDmg());
                    break;
                }


            //iterate other enemies
            for(Enemy e2: Game.enemies)
                if(!e2.equals(e) && e.getCollider().isColliding(e2.getCollider())){


                    //calculam gravitatea impactului
                    //sistem de coliziuni: revizia 1 (equal mass)

                    //calculam impulsul in sens opus
                    Vector2 impulsP = new Vector2(e2.getDirection().x - e.getDirection().x, e2.getDirection().y - e.getDirection().y);
                    Vector2 impulsA = new Vector2(e.getDirection().x - e2.getDirection().x, e.getDirection().y - e2.getDirection().y);

                    //scadem impulsul din vectorul de directie
                    e2.setDirection(e2.getDirection().sub(impulsP));
                    e.setDirection(e.getDirection().sub(impulsA));

                    //ne asiguram ca hitboxurile nu mai sunt suprapuse (metoda bruta)

                    while(e2.getCollider().isColliding(e.getCollider())) {
                        if(e.getCollider().transform.position.x < e2.getCollider().transform.position.x)
                            e.getCollider().transform.position.x --;
                        else
                        if(e.getCollider().transform.position.x > e2.getCollider().transform.position.x)
                            e.getCollider().transform.position.x ++;

                        if(e.getCollider().transform.position.y < e2.getCollider().transform.position.y)
                            e.getCollider().transform.position.y --;
                        else
                        if(e.getCollider().transform.position.y > e2.getCollider().transform.position.y)
                            e.getCollider().transform.position.y ++;
                    }

                    e2.getTransform().copyPosition(e2.getCollider().transform);
                    e.getTransform().copyPosition(e.getCollider().transform);
                }

        }

    }

}

