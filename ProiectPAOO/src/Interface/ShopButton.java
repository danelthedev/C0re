package Interface;

import Utilitarians.Vector2;

public class ShopButton{

    public String value = "";
    public String description = "";

    public int price = 0;

    boolean highlighted;

    Vector2 pos;
    int size;

    ShopButton(Vector2 pos){
        this.pos = pos;
        size = 64;
    }
}