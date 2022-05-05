import java.util.Objects;

public class Tuple {

    float x;
    float y;
    boolean inCircle;

    public Tuple(float x, float y, boolean inCircle) {
        this.x = x;
        this.y = y;
        this.inCircle = inCircle;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isInCircle() {
        return inCircle;
    }

    public void setInCircle(boolean inCircle) {
        this.inCircle = inCircle;
    }
}
