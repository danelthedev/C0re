package Interface;

import Graphics.Sprite;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class DeathBox {
    static Transform transform = new Transform(new Vector2(390, 170), 500, 600);
    public static Sprite sprite = new Sprite("./Assets/misc/deathBox.png", transform);
}
