package main.ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.entities.Player;
import main.levels.Level1;

public class Window extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 942, HEIGHT = 582;
    public static final int MIN_WIDTH = 800, MIN_HEIGHT = 500;
    public static final int MAX_WIDTH = 1920, MAX_HEIGHT = 1080;

    private Thread thread;
    private boolean running;

    private JPanel container; 
    private CardLayout cardLayout; 

    private Menu_Screen menuScreen; 
    private Level1 level1;  // Instancia del nivel 1
    
    // Declarar jugador
    public Player player;

    // FPS control
    private final int TARGET_FPS = 60; // Frames por segundo
    private final long TARGETTIME = 1000000000 / TARGET_FPS; // Tiempo objetivo en nanosegundos
    private long delta = 0;

    public static int nivelSeleccionado = 1;

    public Window() {
        setTitle("Bomber Postal");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        menuScreen = new Menu_Screen(this);

        player = new Player(100, 100, 32, 48);
        level1 = new Level1(player);

        container.add(menuScreen, "Menu");
        container.add(level1, "Game");  // Añadir Level1 al contenedor

        add(container);
        setVisible(true);

        startMenu();  // Iniciar el menú al abrir la ventana
    }

    // Cambia a la pantalla del juego
    public void showGameScreen() {
        cardLayout.show(container, "Game");  // Mostrar la pantalla del juego
        level1.requestFocus();  // Enfocar el panel de Level1
        start();  // Iniciar el ciclo de juego
    }

    // Inicia el menú al abrir la ventana
    private void startMenu() {
        cardLayout.show(container, "Menu");
        menuScreen.start(); // Inicia el ciclo del menú
    }

    @Override
    public void run() {
        long now;
        long lastTime = System.nanoTime();
        
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / TARGETTIME;
            lastTime = now;

            if (delta >= 1) {
                update(); // Actualiza lógica del juego
                delta--;
            }
        }

        stop();
    }

    // Método para actualizar la lógica del juego
    private void update() {
        if (nivelSeleccionado == 1 && level1 != null) {
            level1.update(); // Llama al método de actualización de Level1
        }
    }

    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start(); // Inicia el hilo del juego
    }

    public void stop() {
        running = false;
        try {
            thread.join(); // Espera a que el hilo termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
