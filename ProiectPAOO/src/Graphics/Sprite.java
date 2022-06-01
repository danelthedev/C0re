package Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Exceptions.InvalidPathException;
import Game.Game;
import Utilitarians.Transform;
import Utilitarians.Vector2;

public class Sprite {

    public static double resolutionModifier = 1;

    public BufferedImage sprite = null;
    public Transform transform = null;
    public Vector2 pos;


    public Sprite(String filepath, Transform transform){

        try {
            sprite = ImageIO.read(new File(filepath));
        }catch(IOException e){System.out.println("Cannot load image");}


        pos = new Vector2();

        this.transform = new Transform();
        this.transform.copyPosition(transform);
        this.transform.rotation = transform.rotation;
        this.transform.x_scale = transform.x_scale;
        this.transform.y_scale = transform.y_scale;
    }

    public Sprite(BufferedImage sprite, Transform transform){

        this.sprite = sprite;

        pos = new Vector2();

        this.transform = new Transform();
        this.transform.copyPosition(transform);
        this.transform.rotation = transform.rotation;
        this.transform.x_scale = transform.x_scale;
        this.transform.y_scale = transform.y_scale;
    }


    public void setSprite(String filepath) throws InvalidPathException {

        if(filepath == null)
            throw new InvalidPathException("Sprite path is null");

        sprite = null;
        System.gc();

        try {
            sprite = ImageIO.read(new File(filepath));
        }catch(IOException e){System.out.println("Cannot load image");}

    }

    public void setPos(int x, int y){
        this.pos.x = x;
        this.pos.y = y;
    }
    public void setPos(Vector2 pos){
        this.pos = pos;
    }

    public void render(){

        transform.width = (int) (getWidth() * transform.x_scale);
        transform.height = (int) (getHeight() * transform.y_scale);

        BufferStrategy temp = Game.canvas.getBufferStrategy();
        paint(temp.getDrawGraphics());
    }

    public void paint(Graphics g) {

       if(sprite != null) {

           Graphics2D g2d = (Graphics2D) g;
           AffineTransform at = new AffineTransform();
           at.translate((int) (transform.position.x - Camera.getInstance().position.x), (int) (transform.position.y - Camera.getInstance().position.y));
           at.rotate(Math.toRadians(transform.rotation), sprite.getWidth() / 2, sprite.getHeight() / 2);
           at.scale(transform.x_scale, transform.y_scale);
           g2d.setTransform(at);
           g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transform.alpha));

           g2d.drawImage(sprite, 0, 0, null);
           g2d.dispose();

       }

    }

    //used for rendering menus and buttons, NOT PLAYER DATA (for now)
    public void paintUI(Graphics g) {

        if(sprite != null) {

            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform at = new AffineTransform();
            at.translate(transform.position.x, transform.position.y);
            at.rotate(Math.toRadians(transform.rotation), sprite.getWidth() / 2, sprite.getHeight() / 2);
            g2d.setTransform(at);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transform.alpha));
            g2d.drawImage(sprite, 0, 0, null);
            g2d.dispose();
        }

    }

    public static void paintText(Graphics g, String text, int x, int y, Color c, float size){
        g.setColor(c);

        try {
            g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./Assets/fonts/ArizoneUnicaseRegular.ttf")).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        g.drawString(text, x, y);
    }

    public static void paintTextWithShadow(Graphics g, String text, int x, int y, Color c, float size){

        g.setColor(Color.BLACK);
        try {
            g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./Assets/fonts/ArizoneUnicaseRegular.ttf")).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        g.drawString(text, (int) (x + size / 10), (int) (y + size / 10));

        g.setColor(c);
        g.drawString(text, x, y);

    }


    public static void paintFilledRect(Graphics g, Color c, int x, int y, int w, int h){
        g.setColor(c);
        g.fillRect(x, y, w, h);
    }
    public static void paintFilledRectWA(Graphics g, Color c, int x, int y, int w, int h, float a){
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2d.setColor(c);
        g2d.fillRect(x, y, w, h);
    }
    public static void paintFilledRectWACentered(Graphics g, Color c, int x, int y, int w, int h, float a){
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2d.setColor(c);
        g2d.fillRect(x - w/2, y, w, h);
    }

    public static void paintOutlineRect(Graphics g, Color c, int x, int y, int w, int h){
        g.setColor(c);
        g.drawRect(x, y, w, h);
    }
    public static void paintOutlineRectWA(Graphics g, Color c, int x, int y, int w, int h, float a){
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2d.setColor(c);
        g2d.drawRect(x, y, w, h);
    }

    public static void paintOutlineGrosRect(Graphics g, Color c, int grosime, int x, int y, int w, int h){
        g.setColor(c);

        for(int i = 0; i < grosime; ++ i)
            g.drawRect(x-i, y-i, w+2*i, h+2*i);

    }
    public static void paintOutlineGrosRectWA(Graphics g, Color c, int grosime, int x, int y, int w, int h, float a){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));

        for(int i = 0; i < grosime; ++ i)
            g2d.drawRect(x-i, y-i, w+2*i, h+2*i);

    }
    public static void paintOutlineGrosRectWACentered(Graphics g, Color c, int grosime, int x, int y, int w, int h, float a){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));

        for(int i = 0; i < grosime; ++ i)
            g2d.drawRect((x - w/2)-i, y-i, w+2*i, h+2*i);

    }


    public int getWidth(){
        return sprite.getWidth();
    }
    public int getHeight(){
        return sprite.getHeight();
    }

}
