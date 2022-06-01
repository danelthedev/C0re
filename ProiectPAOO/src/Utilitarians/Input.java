package Utilitarians;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Game.Game;
import Utilitarians.Vector2;

import static Utilitarians.Utilities.clamp;

public class Input implements KeyListener, MouseListener{

    //keyboard info
    public static boolean[] keysState;
    public static boolean[] justPressed;
    //mouse info
    public static Vector2 mousePosition;
    public static boolean LMB, RMB, MMB;

    //buffer string
    public static String buffer = "";

    public Input(){
        keysState = new boolean[256];
        justPressed = new boolean[256];
        mousePosition = new Vector2();

    }

    @Override
    public void keyTyped(KeyEvent e) {    }

    @Override
    public void keyPressed(KeyEvent e) {

        justPressed[e.getKeyCode()] = !keysState[e.getKeyCode()];

        keysState[e.getKeyCode()] = true;

        if(!Game.isPlayerAlive){
            if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE && buffer.length() < 4)
                buffer += (Character.toString(e.getKeyChar()));
            else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && buffer.length() > 0)
                buffer = buffer.substring(0, buffer.length() - 1);
            System.out.println(buffer);
        }
        else
            buffer = "";
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysState[e.getKeyCode()] = false;
    }

    //MOUSE CONTROLS

    public static void updateMousePosition(){
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        SwingUtilities.convertPointFromScreen(b, Game.GameWindow);
        mousePosition.x = (int) clamp((int) b.getX(), 0, Game.GameWindow.getWidth());
        mousePosition.y = (int) clamp((int) b.getY(), 0, Game.GameWindow.getHeight());


        /*
        if(Game.GameWindow.getMousePosition() != null) {
            mousePosition.x = Game.GameWindow.getMousePosition().x;
            mousePosition.y = Game.GameWindow.getMousePosition().y;
        }
         */


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getButton() == MouseEvent.BUTTON1)
            LMB = true;
        if(e.getButton() == MouseEvent.BUTTON2)
            MMB = true;
        if(e.getButton() == MouseEvent.BUTTON3)
            RMB = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(e.getButton() == MouseEvent.BUTTON1)
            LMB = false;
        if(e.getButton() == MouseEvent.BUTTON2)
            MMB = false;
        if(e.getButton() == MouseEvent.BUTTON3)
            RMB = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}

