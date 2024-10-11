package main.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class Menu_Screen extends JPanel implements KeyListener, Runnable {

    private static final long serialVersionUID = 1L;
    private boolean startGame = false;
    private boolean running;
    private boolean showPressEnter = true;
    private int blinkCounter = 0;
    private final int BLINK_INTERVAL = 40; 

    private String tittle = "Bomber Postal";
    private Window window; 

    public Menu_Screen(Window window) {
        this.window = window;
        setFocusable(true);
        addKeyListener(this);
    }

    public void update() {
        blinkCounter++;
        if (blinkCounter >= BLINK_INTERVAL) {
            showPressEnter = !showPressEnter; 
            blinkCounter = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        int fontSizeTittle = getWidth() / 9; 
        Font tittleFont = new Font("BREAK IT", Font.PLAIN, fontSizeTittle);
        g.setFont(tittleFont);
        int tittleWidth = g.getFontMetrics().stringWidth(tittle);
        g.setColor(Color.red);
        g.drawString(tittle, (getWidth() / 2) - (tittleWidth / 2), getHeight() / 3);

        if (showPressEnter) { 
            int fontSizeEnter = getWidth() / 30; 
            g.setFont(new Font("BREAK IT", Font.PLAIN, fontSizeEnter));
            String pressEnter = "Presiona Enter";
            int pressEnterWidth = g.getFontMetrics().stringWidth(pressEnter);
            g.setColor(Color.white);
            g.drawString(pressEnter, (getWidth() / 2) - (pressEnterWidth / 2), getHeight() / 2 + 50);
        }
    }

    @Override
    public void run() {
        while (running) {
            repaint(); 
            update();  
            if (startGame) {
                running = false;
                window.showGameScreen(); 
            }
            try {
                Thread.sleep(16); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Window.nivelSeleccionado = 1; 
            stop();  
            window.showGameScreen();
        }
    }




    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
