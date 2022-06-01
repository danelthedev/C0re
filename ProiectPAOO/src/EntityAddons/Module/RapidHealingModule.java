package EntityAddons.Module;

import Entities.Player;
import EntityAddons.Shield;
import Utilitarians.Input;

import java.awt.event.KeyEvent;

public class RapidHealingModule implements Module{

    private String name = "Rapid Healing";

    private String upgrade = "";

    private int CD, maxCD = 1200;
    private float moduleCdModifier;

    private float rechargeRateBoostModfier, rechargeDelayBoostModifier;
    private float rechargeRateBoost = 1.5f, rechargeDelayBoost = 0.5f;

    private int activeTime;

    private Shield shield;


    public RapidHealingModule(Shield shield){
        this.shield = shield;
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
        if(upgrade.startsWith("CD Reduction"))
            moduleCdModifier = 1 - Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else moduleCdModifier = 1;

        //MODULE EFICIENCY
        if(upgrade.startsWith("Nanomachines")) {
            rechargeRateBoostModfier = 1 + (float)Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
            rechargeDelayBoostModifier = 1 - (float)Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        }
        else{
            rechargeRateBoostModfier = 1;
           rechargeDelayBoostModifier = 1;
        }

    }

    @Override
    public void update() {

        getModifiers();

        if((Input.keysState[KeyEvent.VK_SHIFT] || shield.getOwner().getClass() != Player.class) && CD == 0){
            shield.setModuleRechargeDelayModifier(rechargeDelayBoost * rechargeDelayBoostModifier);
            shield.setModuleRechargeRateModifier(rechargeRateBoost * rechargeRateBoostModfier);
            CD = (int) (maxCD * moduleCdModifier);
            activeTime = 300;
        }

        if(activeTime == 0 && CD > 0){
            shield.setModuleRechargeDelayModifier(1f);
            shield.setModuleRechargeRateModifier(1f);
        }

        if(CD > 0)
            -- CD;

        if(activeTime > 0)
            -- activeTime;
    }
}
