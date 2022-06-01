package EntityAddons;

import Graphics.Renderer;
import Graphics.Sprite;
import Game.Game;
import Utilitarians.Transform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Trail {

    Sprite sprite;
    Transform transform;

    static String trailPath = "./Assets/trails/trail";

    public static Map<String, BufferedImage> trails = new HashMap<>();

    static {

        try {
            BufferedImage temp = ImageIO.read(new File(trailPath + "Basic.png"));
            trails.put("Basic", temp);
            temp = ImageIO.read(new File(trailPath + "Hegemony.png"));
            trails.put("Hegemony", temp);
            temp = ImageIO.read(new File(trailPath + "ADW.png"));
            trails.put("ADW", temp);
            temp = ImageIO.read(new File(trailPath + "Kertham.png"));
            trails.put("Kertham", temp);
            temp = ImageIO.read(new File(trailPath + "Absterian.png"));
            trails.put("Absterian", temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Trail(BufferedImage sprite, Transform transform){
        this.transform = new Transform();
        this.transform.copyTransform(transform);
        this.sprite = new Sprite(sprite, transform);

        Renderer.getInstance().addToLayer("Trails", this.sprite);

    }

    public void update(){

        if(sprite.transform.alpha <= 0.1){
            delete();
        }
        else
            sprite.transform.alpha -= 0.08;

    }

    public void delete(){
        Game.trails.remove(this);
        Renderer.getInstance().renderLayers.get("Trails").remove(sprite);
    }

}
