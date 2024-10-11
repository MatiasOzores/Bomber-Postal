package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import main.ui.Window;

public class Player {
    // -------------------------- Declaracion de variables --------------------------
    
	// Atributos generales del jugador
	private int x, y;
    private int sizeWidth, sizeHeight; 
    private int speed;
    private Color color;

    // Vida
    public int vida = 3;

    // Bombas
    private List<Bomb> bombs = new ArrayList<>();
    private long lastBombTime = 0; 
    private final long BOMB_DELAY = 5000; 
    
    // Balas
    private List<Bullet> bullets = new ArrayList<>();
    private long lastShootTime = 0; 
    private final long SHOOT_DELAY = 390; 
    private int lastDirection = -1;
    
    // Estructuras
    private List<Structure> structures;

    // ------------------------------------------------------------------------------

    public Player(int speed, Color color, List<Structure> structures) {
        this.speed = speed;
        this.color = color;
        this.structures = structures;
        updateDimensions();
        this.x = 128;
        this.y = 128;
    }

    public void updateDimensions() {
    	this.sizeWidth = (int) (Window.anchoResponsive * 0.05); 
        this.sizeHeight = (int) (Window.anchoResponsive * 0.05);
    }

    public void update(boolean left, boolean right, boolean space, boolean up, boolean a, boolean d, boolean w, boolean s, boolean down, boolean shoot) {
        speed = (int) (Window.anchoResponsive * 0.0033);
        sizeWidth = Window.bloqueTam - (int) (Window.anchoResponsive * 0.01);
        sizeHeight = Window.bloqueTam - (int) (Window.anchoResponsive * 0.01);
        
        long currentTime = System.currentTimeMillis();
        
        // ----------------------------------------------------- Movimiento
        boolean moved = false;
        
        int newX = x;
        int newY = y;

        if (left || a) {
            newX -= speed;
            if (!checkCollision(newX, y)) {
                x = newX;
                lastDirection = 2; // Izquierda
                moved = true;
            }
        }
        if (right || d) {
            newX += speed;
            if (!checkCollision(newX, y)) {
                x = newX;
                lastDirection = 0; // Derecha
                moved = true;
            }
        }
        if (up || w) {
            newY -= speed;
            if (!checkCollision(x, newY)) {
                y = newY;
                lastDirection = 1; // Arriba
                moved = true;
            }
        }
        if (down || s) {
            newY += speed;
            if (!checkCollision(x, newY)) {
                y = newY;
                lastDirection = 3; // Abajo
                moved = true;
            }
        }

        x = Math.max(Window.limiteJugadorIzquierda, Math.min(x,Window.limiteJugadorDerecha));
        y = Math.max(Window.limiteJugadorArriba, Math.min(y,Window.limiteJugadorAbajo));

        if (space && (currentTime - lastBombTime) >= BOMB_DELAY) {
            bombs.add(new Bomb(x + sizeWidth / 2 - (int) (Window.anchoResponsive * 0.019), y + sizeHeight / 2 - (int) (Window.anchoResponsive * 0.019)));
            lastBombTime = currentTime;
        }

        // Actualizar bombas
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).update();
            if (bombs.get(i).hasExploded()) {
                bombs.remove(i);
                i--;
            }
        }

        // ----------------------------------------------------- Balas
        if (shoot && (currentTime - lastShootTime) >= SHOOT_DELAY) {
            if (lastDirection != -1) {
                bullets.add(new Bullet(x + sizeWidth / 2 - 5, y + sizeHeight / 2 - 5, lastDirection));
                lastShootTime = currentTime;
            }
        }

        // Actualizar balas y verificar colisiones
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(structures);

            // Verificar colisiones con las estructuras
            for (Structure structure : structures) {
                if (bullet.collidesWith(structure)) {
                    bullet.deactivate(); // Desactivar la bala si colisiona
                    break; // Salir del bucle si la bala colisiona con cualquier estructura
                }
            }

            // Eliminar balas inactivas
            if (!bullet.isActive()) {
                bullets.remove(i);
                i--; // Decrementar i para mantener la posición correcta
            }
        }
    }


    
    private boolean checkCollision(int newX, int newY) {
        for (Structure structure : structures) {
            if (newX < structure.getX() + structure.getWidth() &&
                newX + sizeWidth > structure.getX() &&
                newY < structure.getY() + structure.getHeight() &&
                newY + sizeHeight > structure.getY()) {
                return true; // Hay colisión
            }
        }
        return false; // No hay colisión
    }

    public void draw(Graphics g) {
    	
    	// Jugador
        g.setColor(color);
        g.fillRect(x, y, sizeWidth, sizeHeight); 

        // Bombas
        for (Bomb bomb : bombs) {
            bomb.draw(g);
        }
        
        // Balas
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        // Dibujar estructuras
        for (Structure structure : structures) {
            structure.draw(g);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

