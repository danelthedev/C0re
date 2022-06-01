package EntityAddons.Module;

public interface Module {

    int getCD();
    String getName();
    String getUpgrade();
    void setUpgrade(String upgrade);
    void update();
}
