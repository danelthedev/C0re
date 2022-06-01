package EntityAddons.Module;

import Entities.ComplexEntity;
import Entities.Player;
import Utilitarians.Input;

import java.awt.event.KeyEvent;

public class NitroModule implements Module{

    private final String name = "Nitro";
    private String upgrade = "";
    private int CD;
    private final int maxCD = 1200;
    private float moduleCdModifier = 1;

    private int activeTime;
    private final ComplexEntity owner;

    float speedBoost = 2, speedBoostModifier = 1;

    public NitroModule(ComplexEntity owner){
        this.owner = owner;
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


    @Override
    public int getCD() {
        return CD;
    }
    private void getModifiers(){
        //MODULE COOLDOWN
        if(upgrade.startsWith("CD Reduction"))
            moduleCdModifier = 1 - Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else moduleCdModifier = 1;

        //MODULE EFICIENCY
        if(upgrade.startsWith("Turbo speed"))
            speedBoostModifier = 1 + Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else speedBoostModifier = 1;
    }
    @Override
    public void update() {

        getModifiers();

        if((Input.keysState[KeyEvent.VK_SHIFT] || owner.getClass() != Player.class) && CD == 0){
            owner.setModuleSpeedModifier(speedBoost * speedBoostModifier);
            CD = (int) (maxCD * moduleCdModifier);
            activeTime = 300;
        }
        if(activeTime == 0 && CD > 0){
            owner.setModuleSpeedModifier(1);
        }

        if(CD > 0)
            -- CD;

        if(activeTime > 0)
            -- activeTime;
    }

}
