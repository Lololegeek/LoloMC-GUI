package dev.lolomc.gui.model;

public final class Rect {
    public float x;
    public float y;
    public float width;
    public float height;

    public Rect() { this(0, 0, 0, 0); }

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float px, float py) {
        return px >= x && py >= y && px <= x + width && py <= y + height;
    }
}

