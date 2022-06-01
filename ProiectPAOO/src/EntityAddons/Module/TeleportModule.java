package EntityAddons.Module;

import Entities.ComplexEntity;
import Entities.Player;
import Utilitarians.Input;
import Utilitarians.Utilities;
import Utilitarians.Vector2;

import java.awt.event.KeyEvent;

public class TeleportModule implements Module {

    private String name = "Teleport";

    private int CD, maxCD = 300;
    private float moduleCdModifier = 1;

    private ComplexEntity owner;

    private int range = 450;
    private float rangeModifier = 1;

    private String upgrade = "";

    public TeleportModule(ComplexEntity owner) {
        this.owner = owner;
    }

    @Override
    public int getCD() {
        return CD;
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
    public String getName(){
        return name;
    }

    private void getModifiers(){
        //MODULE COOLDOWN
        if(upgrade.startsWith("CD Reduction"))
            moduleCdModifier = 1 - Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else moduleCdModifier = 1;

        //MODULE EFFICIENCY
        if(upgrade.startsWith("Range"))
            rangeModifier = 1 + Integer.parseInt(upgrade.replaceAll("[\\D]", "")) / 10f;
        else rangeModifier = 1;
    }

    @Override
    public void update() {

        getModifiers();

        if ((Input.keysState[KeyEvent.VK_SHIFT] || owner.getClass() != Player.class) && CD == 0) {
            CD = (int) (maxCD * moduleCdModifier);


            Vector2 temp = new Vector2();
            temp.x = owner.getTransform().position.x + rangeModifier * range * Math.cos(Utilities.deg2rad(owner.getTransform().rotation));
            temp.y = owner.getTransform().position.y + rangeModifier * range * Math.sin(Utilities.deg2rad(owner.getTransform().rotation));
            owner.getTransform().position = temp;


        }

        if (CD > 0)
            --CD;

    }
}
