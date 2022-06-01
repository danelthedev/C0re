package Game;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Entities.Asteroid;
import Entities.Player;
import EntityAddons.Projectile.ExplosionAOE;
import EntityAddons.Projectile.PlasmaAOE;
import EntityAddons.Projectile.Projectile;
import Entities.Enemy;
import EntityAddons.Trail;
import Graphics.*;
import Utilitarians.*;


public class Game {

    public enum ControlType{KeyboardControl, MouseControl};

    //database
    public static Database db;


    //state
    public static GameState gameState;
    int gameStateChangeCD = 0;

    //rendering
    public boolean isRunning;
    public static GameWin GameWindow;
    public static Canvas canvas;

    //playState objects
    public static Player player;
    public static boolean isPlayerAlive;
    public static String playerFaction;

    public static ArrayList<Asteroid> asteroids = new ArrayList<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();

    public static ArrayList<Projectile> playerProj = new ArrayList<>();
    public static ArrayList<Projectile> enemyProj = new ArrayList<>();
    public static ArrayList<PlasmaAOE> EplasmaAOEs = new ArrayList<>();
    public static ArrayList<PlasmaAOE> PplasmaAOEs = new ArrayList<>();
    public static ArrayList<ExplosionAOE> EExplosionAOEs = new ArrayList<>();
    public static ArrayList<ExplosionAOE> PExplosionAOEs = new ArrayList<>();
    public static ArrayList<Trail> trails = new ArrayList<>();

    //score
    public static int credits = 0;

    //global credits and factions unlocked
    public static int globalCredits = 0;
    public static int[] factionsUnlocked = new int[4];

    //input
    public static ControlType controlType;
    public static boolean[] keysState;
    public static boolean[] justPressed;

    //wave manager
    public WaveManager waveManager = WaveManager.getInstance();


    static Game instance = null;

    public static Game getInstance(){
        if(instance == null){
            synchronized (Game.class){
                if(instance == null)
                    instance = new Game();
            }
        }

        return instance;
    }

    private void initMetaProgression(){
        HashMap<String, String> metaProgressionColumns = new HashMap<>();
        metaProgressionColumns.put("GlobalCredits", "INT");
        metaProgressionColumns.put("HegemonyUnlocked", "INT");
        metaProgressionColumns.put("AbsterianUnlocked", "INT");
        metaProgressionColumns.put("ADWUnlocked", "INT");
        metaProgressionColumns.put("KerthamUnlocked", "INT");
        metaProgressionColumns.put("Volume", "REAL");

        db.openDB("gameDB");
        db.createTable("MetaProgression", metaProgressionColumns);
        db.closeDB();
    }

    private Game(){

        //create database
        db = Database.getInstance();
        initMetaProgression();

        //create savestate entry
        SaveState.getInstance();

        //read meta progression
        db.getMetaProgression();

        //create game window
        GameWindow = new GameWin();

        //cursor custom
        Utilities.setCursor("./Assets/misc/cursor.png");

        //setam vizibilitatea ferestrei
        GameWindow.setVisible(true);

        //initializarea canvasului
        canvas = new Canvas();
        GameWindow.add(canvas);
        canvas.setFocusable(false);
        canvas.createBufferStrategy(2);

        //set game state
        gameState = new MenuState();
        isRunning = true;
        controlType = ControlType.MouseControl;


        //create input
        Input input = new Input();
        GameWindow.addKeyListener(input);
        canvas.addMouseListener(input);

        //isPlayerAlive
        isPlayerAlive = true;
    }

    public void update(){

        //Get input (universal for all game states)
        keysState = Arrays.copyOf(Input.keysState, Input.keysState.length);
        justPressed = Arrays.copyOf(Input.justPressed, Input.justPressed.length);
        Input.updateMousePosition();

        //pause menu
        if (keysState[KeyEvent.VK_ESCAPE]) {
            if(gameState.getClass() == PlayState.class && gameStateChangeCD <= 0) {
                gameState.next(this);
                gameStateChangeCD = 60;
            }
            else if(gameState.getClass() == PauseState.class && gameStateChangeCD <= 0) {
                gameState.prev(this);
                gameStateChangeCD = 60;
            }
        }

        if(gameStateChangeCD > 0)
            --gameStateChangeCD;


        gameState.update();
    }

    public void render(){

        //clear JFrame
        canvas.getBufferStrategy().getDrawGraphics().clearRect(0,0, Game.GameWindow.getWidth(), Game.GameWindow.getHeight());

        gameState.render();

        //clear canvas at end of frame
        canvas.getBufferStrategy().show();
        canvas.getBufferStrategy().getDrawGraphics().dispose();
    }

    public void setState(GameState gameState){
        this.gameState = gameState;
    }

}
