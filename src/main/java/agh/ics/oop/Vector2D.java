package agh.ics.oop;

public class Vector2D {
    private final int x;
    private final int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.join("","(", String.valueOf(this.x), ", ",
                String.valueOf(this.y), ")");
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x+other.x, y+other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x-other.x, y-other.y);
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2D))
            return false;
        Vector2D that = (Vector2D) other;
        return (x==that.x)&&(y==that.y);
    }
    @Override
    public int hashCode() {
        return ((this.x * 17) % 19  + (this.x*this.y * 31) % 83);
    }
}
