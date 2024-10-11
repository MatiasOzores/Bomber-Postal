package main.levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import main.entities.Player;
import main.ui.Window;

public class Level1 extends JPanel implements KeyListener {
    private Player player; // Referencia al jugador

    public Level1(Player player) {
        this.player = player; // Inicializar el jugador
        setFocusable(true);
        addKeyListener(this); // Registrar el KeyListener de Level1
    }

    // Dibuja un fondo rojo y el jugador
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);  // Fondo rojo
        g.fillRect(0, 0, getWidth(), getHeight());  // Rellenar todo el fondo
        
        player.draw(g); // Dibujar el jugador
    }

    // Método para actualizar la lógica del jugador
    public void update() {
        player.update(); // Actualizar la lógica del jugador
        repaint(); // Llamar a repaint para dibujar la nueva posición
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.setKey(e.getKeyCode(), true); // Marcar la tecla como presionada
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.setKey(e.getKeyCode(), false); // Marcar la tecla como no presionada
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No es necesario implementar
    }
}
