package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import main.ui.Window;

public class Bullet {
    private int x, y, width,height;
    private int speed;
    private int direction; // 0: derecha, 1: arriba, 2: izquierda, 3: abajo
    private boolean active;

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = (int) (Window.anchoResponsive * 0.013); 
        this.active = true;
        
        if(direction == 0 || direction == 2) {
        	this.width = (int) (Window.anchoResponsive * 0.02);
        	this.height = (int) (Window.anchoResponsive * 0.01);
        }
        
        else if(direction == 1 || direction == 3){
        	this.width = (int) (Window.anchoResponsive * 0.01);
        	this.height = (int) (Window.anchoResponsive * 0.02);        	
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
                    deactivate(); // Desactivar la bala si hay colisión
                    break; // Salir del bucle una vez que se detecta una colisión
                }
            }
        }
    }


    public void draw(Graphics g) {
        if (active) {
            g.setColor(Color.RED); 
            g.fillRect(x, y, width, height); 
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