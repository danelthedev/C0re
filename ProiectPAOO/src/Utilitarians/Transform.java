package Utilitarians;

public class Transform {

    public Vector2 position;
    public int width, height;
    public float x_scale, y_scale;
    public float rotation;
    public float alpha;

    public Transform(){
        position = new Vector2(0, 0);
        x_scale = 1;
        y_scale = 1;
        rotation = 0;
        width = 0;
        height = 0;
        alpha = 1;
    }

    public Transform(Vector2 position, int width, int height){
        this.position = new Vector2(position);
        x_scale = 1;
        y_scale = 1;
        rotation = 0;
        this.width = width;
        this.height = height;
        alpha = 1;
    }

    public Transform(Vector2 position, int width, int height, float x_scale, float y_scale){
        this.position = new Vector2(position);
        this.x_scale = x_scale;
        this.y_scale = y_scale;
        rotation = 0;
        this.width = width;
        this.height = height;
        alpha = 1;
    }


    public void copyTransform(Transform transform){
        this.position.x = transform.position.x;
        this.position.y = transform.position.y;
        this.x_scale = transform.x_scale;
        this.y_scale = transform.y_scale;
        this.rotation = transform.rotation;
        this.width = transform.width;
        this.height = transform.height;
        this.alpha = transform.alpha;
    }

    public void copyPosition(Transform transform){
        this.position.x = transform.position.x;
        this.position.y = transform.position.y;
    }

    public static float convertAngle(Float rotation){
        rotation = rotation % 360;
        rotation = (rotation + 360) % 360;
        if (rotation > 180)
            rotation -= 360;

        return rotation;
    }
}
