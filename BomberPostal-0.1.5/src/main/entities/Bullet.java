package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import main.ui.Window;

public class Bullet {
    private int x, y, width, height;
    private int speed;
    private int direction; // 0: derecha, 1: arriba, 2: izquierda, 3: abajo
    private boolean active;

    // Imágenes para cada dirección
    private Image imgRight, imgUp, imgLeft, imgDown;

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = (int) (Window.anchoResponsive * 0.013); 
        this.active = true;

        if(direction == 0 || direction == 2) {
            this.width = (int) (Window.anchoResponsive * 0.02);
            this.height = (int) (Window.anchoResponsive * 0.01);
        } else if(direction == 1 || direction == 3) {
            this.width = (int) (Window.anchoResponsive * 0.01);
            this.height = (int) (Window.anchoResponsive * 0.02);        	
        }

        // Cargar las imágenes para cada dirección
        try {
            imgRight = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bala/Bala-Derecha.png"));
            imgUp = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bala/Bala-Arriba.png"));
            imgLeft = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bala/Bala-Izquierda.png"));
            imgDown = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bala/Bala-Abajo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(List<Structure> structures) {
        if (active) {
            switch (direction) {
                case 0: // Derecha
                    x += speed;
                    break;
                case 1: // Arriba
                    y -= speed;
                    break;
                case 2: // Izquierda
                    x -= speed;
                    break;
                case 3: // Abajo
                    y += speed;
                    break;
            }

            // Comprobar límites de la ventana
            if (x < 0 || x > (Window.anchoResponsive - 16) || y < 0 || y > (Window.alturaResponsive - 39)) {
                active = false;
            }

            // Verificar colisiones con estructuras
            for (Structure structure : structures) {
                if (collidesWith(structure)) {
                    deactivate();
                    break; 
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (active) {
            // Dibujar la imagen dependiendo de la dirección
            switch (direction) {
                case 0:
                    g.drawImage(imgRight, x, y, width, height, null);
                    break;
                case 1:
                    g.drawImage(imgUp, x, y, width, height, null);
                    break;
                case 2:
                    g.drawImage(imgLeft, x, y, width, height, null);
                    break;
                case 3:
                    g.drawImage(imgDown, x, y, width, height, null);
                    break;
            }
        }
    }

    public boolean collidesWith(Structure structure) {
        return x < structure.getX() + structure.getWidth() &&
               x + width > structure.getX() &&
               y < structure.getY() + structure.getHeight() &&
               y + height > structure.getY();
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
