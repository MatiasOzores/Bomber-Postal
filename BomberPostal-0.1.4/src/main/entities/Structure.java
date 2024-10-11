package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Structure {
    private int x, y;
    private int width, height;
    private Color color; // Color opcional si no se proporciona imagen
    private Image image; // Imagen opcional

    // Constructor que acepta color
    public Structure(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.image = null; // No hay imagen en este caso
    }

    // Constructor que acepta imagen
    public Structure(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.color = null; // No hay color en este caso
    }

    public void draw(Graphics g) {
        if (image != null) {
            // Dibujar imagen si está presente
            g.drawImage(image, x, y, width, height, null);
        } else if (color != null) {
            // Dibujar el rectángulo con color si no hay imagen
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
