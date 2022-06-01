package EntityAddons.Weapon;

import Entities.ComplexEntity;

public class WeaponFactory {

    public WeaponFactory(){

    }

    public Weapon getWeapon(ComplexEntity owner){
        if(owner.getWeaponType().equals("plasma")){
            return new PlasmaWeapon(owner);
        }
        if(owner.getWeaponType().equals("ballistic")){
            return new BallisticWeapon(owner);
        }
        if(owner.getWeaponType().equals("explosive")){
            return new ExplosiveWeapon(owner);
        }
        return null;
    }

}
