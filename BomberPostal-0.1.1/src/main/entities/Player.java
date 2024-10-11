package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player {
    private int x, y; 
    private int width, height; 
    private Color color;

    // Arreglo para almacenar el estado de las teclas
    private boolean[] keys = new boolean[256]; // Suponiendo que se usen 256 teclas

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.BLUE; 
    }

    // Método para dibujar el jugador
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height); 
    }

    // Método para actualizar la posición del jugador
    public void update() {
        // Lógica de movimiento basada en el estado de las teclas
        if (keys[KeyEvent.VK_W] && y > 0) {  // Tecla W
            y -= 5; // Mover hacia arriba
        }
        if (keys[KeyEvent.VK_S] && y < 582 - height) {  // Tecla S
            y += 5; // Mover hacia abajo
        }
        if (keys[KeyEvent.VK_A] && x > 0) {  // Tecla A
            x -= 5; // Mover hacia la izquierda
        }
        if (keys[KeyEvent.VK_D] && x < 942 - width) {  // Tecla D
            x += 5; // Mover hacia la derecha
        }
    }

    public void setKey(int keyCode, boolean pressed) {
        keys[keyCode] = pressed; // Actualiza el estado de la tecla
    }
}
