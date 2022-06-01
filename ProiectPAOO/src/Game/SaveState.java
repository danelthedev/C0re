package Game;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveState {

    int currentWave, credits;
    float curHp;
    String playerFaction, GPU1, GPU2, module, moduleUpgrade, weaponUpgrade;

    HashMap<String, String> saveItems;
    ArrayList<String> columnNames;

    private static SaveState instance = null;
    public static SaveState getInstance() {
        if(instance == null){
            synchronized (SaveState.class){
                if(instance == null)
                    instance = new SaveState();
            }
        }

        return instance;
    }

    SaveState(){

        //creearea savestateului
        saveItems = new HashMap<>();
        saveItems.put("CurrentWave", "INT");
        saveItems.put("Credits", "INT");
        saveItems.put("CurHP", "Real");
        saveItems.put("PlayerFaction", "String");
        saveItems.put("GPU1", "String");
        saveItems.put("GPU2", "String");
        saveItems.put("Module", "String");
        saveItems.put("ModuleUpgrade", "String");
        saveItems.put("WeaponUpgrade", "String");

        columnNames = new ArrayList<>();
        columnNames.add("CurrentWave");
        columnNames.add("Credits");
        columnNames.add("CurHP");
        columnNames.add("PlayerFaction");
        columnNames.add("GPU1");
        columnNames.add("GPU2");
        columnNames.add("Module");
        columnNames.add("ModuleUpgrade");
        columnNames.add("WeaponUpgrade");


        Game.db.openDB("gameDB");
        Game.db.createTable("SaveState",saveItems);
        Game.db.closeDB();
    }

    public void save(){
        //inserarea valorilor in savestate

        Game.db.insertSaveStateValues(WaveManager.getInstance().currentWave, Game.credits, Game.player.getCurHp(), Game.playerFaction,
                                        Game.player.getGPU(0), Game.player.getGPU(1), Game.player.getModule().getName(),
                                        Game.player.getModule().getUpgrade(), Game.player.getWeapon().getUpgrade());

    }

    public void deleteSaveState(){
        Game.db.deleteSaveState();
    }

}
