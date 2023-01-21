package agh.ics.oop;

import java.lang.Math;

public class Vector3D {
    public final int x;
    public final int y;
    public final int z;

    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.join("","(", String.valueOf(this.x), ", ",
                String.valueOf(this.y), ", ", String.valueOf(this.z), ")");
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(x+other.x, y+other.y, z+ other.z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x-other.x, y-other.y, z- other.z);
    }

    public int distance (Vector3D other) {
        return Math.max(Math.abs(x-other.x), Math.max(Math.abs(y-other.y), Math.abs(z- other.z)));
    }

    public boolean doesNeighbour(Vector3D other) {
        return (!this.equals(other)) && Math.abs(this.x-other.x) <= 1 && Math.abs(this.y-other.y) <= 1 && Math.abs(this.z - other.z) <= 1;
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector3D))
            return false;
        Vector3D that = (Vector3D) other;
        return (x==that.x)&&(y==that.y)&&(z==that.z);
    }
    @Override
    public int hashCode() {
        return ((this.x * 17) % 19  + (this.x*this.y * 31) % 83);
    }
}
