package Utilitarians;

import Game.Game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Utilities {
    public static float clamp(float val, float val1, float val2) {
        if(val1 > val2){
            float aux = val1;
            val1 = val2;
            val2 = aux;
        }

        return Math.max(val1, Math.min(val2, val));
    }

    public static double deg2rad(float degrees){
        return degrees * Math.PI / 180;
    }

    public static void setCursor(String cursorPath){
        try {
            Game.GameWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(new File(cursorPath)), new Point(0, 0), "newCursor"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean pointInRectangle(Vector2 point, Rectangle rectangle){
        return (point.x > rectangle.x && point.x < rectangle.x + rectangle.width && point.y > rectangle.y && point.y < rectangle.y + rectangle.height);
    }
}
