package main.entities;

import java.awt.Color;
import java.awt.Graphics;

import main.ui.Window;

public class Bomb {
    private int x, y;
    private int size;
    private long creationTime;
    private final long explosionDelay = 3000;
    private boolean exploded = false;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = (int)(Window.anchoResponsive*0.04);
        this.creationTime = System.currentTimeMillis();
    }

    public void update() {
        if (!exploded && (System.currentTimeMillis() - creationTime) >= explosionDelay) {
            exploded = true;
        }
    }

    public void draw(Graphics g) {
    	

    	
        if (exploded) {
            g.setColor(Color.RED); 
            g.fillRect(x, y, size, size);
        } else {
            g.setColor(Color.BLACK); 
            g.fillRect(x, y, size, size); 
        }
    }

    public boolean hasExploded() {
        return exploded;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
