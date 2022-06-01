package Utilitarians;

public class Collider {

    public Transform transform;

    public Collider() {
        transform = new Transform();
    }

    public boolean isColliding(Collider collider){
        return (this.transform.position.x < collider.transform.position.x + collider.transform.width &&
                this.transform.position.x + this.transform.width > collider.transform.position.x &&
                this.transform.position.y < collider.transform.position.y + collider.transform.height &&
                this.transform.position.y + this.transform.height > collider.transform.position.y);
    }

}
