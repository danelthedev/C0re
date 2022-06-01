package EntityAddons.Weapon;

import Entities.ComplexEntity;

public interface Weapon {

    void shoot();

    void setMaxCD(int maxCD);
    int getMaxCD();

    void setCD();

    int getCD();
    void decCD();

    ComplexEntity getOwner();

    void setModuleDmgModifier(float modifier);
    void setModuleCDModifier(float modifier);

    void activateUpgrade(String upgrade);
    String getUpgrade();

    float getHullDmg();
    float getShieldDmg();

    void setHullDmg(int hullDmg);
    void setShieldDmg(int shieldDmg);
}
