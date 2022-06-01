package Utilitarians;

import Entities.Player;
import EntityAddons.Module.NitroModule;
import EntityAddons.Module.RapidHealingModule;
import EntityAddons.Module.TeleportModule;
import EntityAddons.Module.WeaponBoostModule;
import Game.Game;
import Game.WaveManager;
import Game.Leaderboard;
import Interface.Buttons.SoundButtons.SoundBar;
import Interface.Buttons.SoundButtons.SoundButton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    Connection c;
    Statement stmt;


    private Database(){

        try {
            Class.forName("org.sqlite.JDBC");
            c = null;
            stmt = null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Database instance = null;
    public static Database getInstance() {
        if(instance == null){
            synchronized (Database.class){
                if(instance == null)
                    instance = new Database();
            }
        }

        return instance;
    }

    public void openDB(String name){
        try {
            this.c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
            this.c.setAutoCommit(false);
            stmt = c.createStatement();
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    public void closeDB(){
        try {
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, HashMap<String, String> fields){
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (ID INT PRIMARY KEY NOT NULL, ");// <-- creaza tabela

        for (HashMap.Entry<String, String> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();
            sql.append(fieldName).append(" ");
            sql.append(fieldType).append(", ");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");

        try {
            stmt.executeUpdate(String.valueOf(sql));
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertValuesISI(String tableName, ArrayList<String> columnNames, int id, String field1, int field2){
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (ID, ");

        for(String columnName: columnNames)
            sql.append(columnName).append(", ");
        sql.deleteCharAt(sql.lastIndexOf(","));

        sql.append(") VALUES (").append(id).append(", '").append(field1).append("', ").append(field2).append(" );");

        try {
            stmt.executeUpdate(String.valueOf(sql));
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkForSaveData(){
        Game.db.openDB("gameDB");
        String sql = "select count(*) from SaveState";
        ResultSet rs = null;
        try {
            //Executing the query
            rs = stmt.executeQuery(sql);
            //Retrieving the result
            rs.next();
            int count = rs.getInt(1);
            Game.db.closeDB();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Game.db.closeDB();
        return false;
    }

    public void deleteSaveState(){
        Game.db.openDB("gameDB");

        try {
            String sql = "DELETE from SaveState";
            stmt.executeUpdate(sql);
            c.commit();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Game.db.closeDB();
    }

    public void insertSaveStateValues(int currentWave, int credits, float curHp, String playerFaction, String GPU1, String GPU2, String module, String moduleUpgrade, String weaponUpgrade){

        deleteSaveState();

        Game.db.openDB("gameDB");
        StringBuilder sql = new StringBuilder("INSERT INTO SaveState (ID, PlayerFaction, Credits, CurrentWave, GPU1, WeaponUpgrade, GPU2, Module, ModuleUpgrade, CurHP) VALUES (");

        String temp = 0 + ", '" + playerFaction + "', " + credits + ", " + currentWave + ", '" + GPU1 + "', '" + weaponUpgrade + "', '" + GPU2 + "', '" + module + "', '" + moduleUpgrade + "', " + curHp + ");";
        sql.append(temp);

        try {
            stmt.executeUpdate(String.valueOf(sql));
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Game.db.closeDB();
    }

    public void loadSaveData(){
        Game.db.openDB("gameDB");
        try {

            ResultSet rs = stmt.executeQuery( "SELECT * FROM SaveState;" );
            while(rs.next()){
                Game.playerFaction = rs.getString("PlayerFaction");
                Game.credits = rs.getInt("Credits");
                WaveManager.getInstance().currentWave = rs.getInt("CurrentWave");

                Game.player = new Player();
                Game.player.setCurHp(rs.getFloat("CurHp"));

                Game.player.getWeapon().activateUpgrade(rs.getString("WeaponUpgrade"));

                switch (rs.getString("Module")) {
                    case "Weapon boost":
                        Game.player.setModule(new WeaponBoostModule(Game.player.getWeapon()));
                        break;
                    case "Rapid healing":
                        Game.player.setModule(new RapidHealingModule(Game.player.getShield()));
                        break;
                    case "Teleport":
                        Game.player.setModule(new TeleportModule(Game.player));
                        break;
                    case "Nitro":
                        Game.player.setModule(new NitroModule(Game.player));
                        break;
                }
                Game.player.getModule().setUpgrade(rs.getString("ModuleUpgrade"));

                Game.player.addGPU(rs.getString("GPU1"), 0);
                Game.player.addGPU(rs.getString("GPU2"), 1);

                Game.gameState.next(Game.getInstance());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Game.db.closeDB();
    }

    public int getMaxID(String tableName){
        StringBuilder sql = new StringBuilder("SELECT MAX(ID) FROM " + tableName);
        ResultSet rs;
        try {
            rs = stmt.executeQuery(String.valueOf(sql));
            c.commit();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<String> getTop10Entries(String tableName, String columnName){
        StringBuilder sql = new StringBuilder( "SELECT * FROM " + tableName + " ORDER BY " + columnName + " DESC;"); // <-- cu comanda asta obtii toate campurile din tabela

        ArrayList<String> entries = new ArrayList<>();
        int currentCount = 0;

        try{
            ResultSet rs = stmt.executeQuery(String.valueOf(sql));
            //in rs avem toate entry-urile ordonate

            while ( rs.next() && currentCount < 10) { // <-- asa parcurgi campurile unul cate unul
                ++ currentCount;

                String name = rs.getString("NAME");
                int score = rs.getInt("SCORE");

                String temp = currentCount + ". " + name + ": " + score;
                entries.add(temp);
            }
            return entries;

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void getMetaProgression(){
        Game.db.openDB("gameDB");

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM MetaProgression ");
        int isFirstTime = 0;

        //check for first time
        try {
            ResultSet rs = stmt.executeQuery(String.valueOf(sql));
            while(rs.next()) {
                isFirstTime = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //daca nu e prima data cand deschid jocul, incarc din DB progresul
        if(isFirstTime > 0){
            sql = new StringBuilder("SELECT * FROM MetaProgression "); // <-- cu comanda asta obtii toate campurile din tabela

            try {
                ResultSet rs = stmt.executeQuery(String.valueOf(sql));
                rs.next();

                Game.globalCredits = rs.getInt("GlobalCredits");
                Game.factionsUnlocked[0] = rs.getInt("HegemonyUnlocked");
                Game.factionsUnlocked[1] = rs.getInt("AbsterianUnlocked");
                Game.factionsUnlocked[2] = rs.getInt("ADWUnlocked");
                Game.factionsUnlocked[3] = rs.getInt("KerthamUnlocked");
                SoundBar.volume = rs.getFloat("Volume");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //daca este prima data, introducem un entry in DB cu valorile default
        else{
            try {
                sql = new StringBuilder("INSERT INTO MetaProgression (ID, GlobalCredits, HegemonyUnlocked, AbsterianUnlocked, ADWUnlocked, KerthamUnlocked, Volume) VALUES (0, 0, 1, 0, 0, 0, 1)");
                Game.globalCredits = 0;
                Game.factionsUnlocked[0] = 1;
                Game.factionsUnlocked[1] = 0;
                Game.factionsUnlocked[2] = 0;
                Game.factionsUnlocked[3] = 0;
                SoundBar.volume = 1;
                stmt.executeUpdate(String.valueOf(sql));
                c.commit();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Game.db.closeDB();
    }

    public void updateDB(String tableName, String field, float value){
        Game.db.openDB("gameDB");

        String sql = "UPDATE " + tableName + " set " + field + " = " + value + ";";
        try {
            stmt.executeUpdate(sql);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Game.db.closeDB();
    }

}
