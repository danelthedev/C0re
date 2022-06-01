package EntityAddons.Module;

import Entities.Player;
import EntityAddons.Weapon.Weapon;
import Utilitarians.Input;

import java.awt.event.KeyEvent;

public class WeaponBoostModule implements Module{

    private String name = "Weapon boost";

    private String upgrade = "";

    private int CD, maxCD = 1200;

    private int activeTime;

    private Weapon weapon;

    private float cdBoost = 0.75f, cdBoostModifier = 1;
    private float dmgBoost = 1.25f, dmgBoostModifier = 1;

    public WeaponBoostModule(Weapon weapon){
        this.weapon = weapon;
    }

    @Override
    public int getCD() {
        return CD;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    @Override
    public String getUpgrade(){
        return upgrade;
    }

    private void getModifiers(){
        //MODULE COOLDOWN
        if(upgrade.startsWith("Firerate"))
            cdBoostModifier = 1 - Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else cdBoostModifier = 1;

        //MODULE EFICIENCY
        if(upgrade.startsWith("Ubercharge"))
            dmgBoostModifier = 1 + Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else dmgBoostModifier = 1;
    }

    @Override
    public void update() {

        getModifiers();

        if((Input.keysState[KeyEvent.VK_SHIFT] || weapon.getOwner().getClass() != Player.class) && CD == 0){
            weapon.setModuleCDModifier(cdBoostModifier * cdBoost);
            weapon.setModuleDmgModifier(dmgBoostModifier * dmgBoost);
            CD = maxCD;
            activeTime = 300;
        }
        if(activeTime == 0 && CD > 0){
            weapon.setModuleCDModifier(1);
            weapon.setModuleDmgModifier(1);
        }

        if(CD > 0)
            -- CD;

        if(activeTime > 0)
            -- activeTime;
    }
}
