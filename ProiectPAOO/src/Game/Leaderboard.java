package Game;


import java.util.ArrayList;
import java.util.HashMap;

public class Leaderboard {

    private HashMap<String, String> leaderboardColumns;
    private ArrayList<String> columnNames;
    private int entriesCount;

    public ArrayList<String> top10;

    private static Leaderboard instance = null;
    public static Leaderboard getInstance() {
        if(instance == null){
            synchronized (Leaderboard.class){
                if(instance == null)
                    instance = new Leaderboard();
            }
        }

        return instance;
    }

    private Leaderboard(){
        top10 = new ArrayList<>();

        columnNames = new ArrayList<>();
        columnNames.add("NAME");
        columnNames.add("SCORE");

        leaderboardColumns = new HashMap<>();
        leaderboardColumns.put("NAME", "CHAR(4)");
        leaderboardColumns.put("SCORE", "INT");

        Game.db.openDB("gameDB");
        Game.db.createTable("Leaderboard", leaderboardColumns);
        Game.db.closeDB();


        Game.db.openDB("gameDB");
        entriesCount = Game.db.getMaxID("Leaderboard");
        Game.db.closeDB();
    }

    public void addLeaderboardEntry(String name, int score){
        Game.db.openDB("gameDB");

        ++entriesCount;
        Game.db.insertValuesISI("Leaderboard", columnNames, entriesCount, name, score);

        Game.db.closeDB();
    }

    public ArrayList<String> getTop10(){
        Game.db.openDB("gameDB");
        top10 = Game.db.getTop10Entries("Leaderboard", "SCORE");
        Game.db.closeDB();
        return top10;
    }

}
