package main.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import main.entities.Player;
import main.entities.Structure;

public class Window extends JFrame implements Runnable, KeyListener {

	// -------------------------- Declaracion de variables --------------------------
	
	// aux
	private int invalido = 0;
	
	// general
	private static final long serialVersionUID = 1L;
    public static int WIDTH = 1280;
	public static int HEIGHT = 720;
    public static int alturaResponsive;
    public static int anchoResponsive;
	private Canvas canvas;
	private Thread thread;
	private boolean running;
	
	// para dibujos/graficos
	private BufferStrategy bs;
	private Graphics g;
	
	// para los frames
	private final int FPS = 60;
	private double TARGETTIME = 1000000000/FPS;
	private double delta = 0;
	
	// para entidades
    private Player player;
    public int speedPlayer = 5;
    
    // relacionado a estructuras/bloques
    List<Structure> structures = new ArrayList<>();
    private int espaciado;
    
    // para eventos de teclado
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean space = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean cPressed = false;
    
    // para controlar la resolucion
    public int resolucion = 2;
    public static int bloqueTam;  // Tamaño de los bloques de la cuadrícula
    
    // Tabla lateral derecha
    public static int puntuacion = 0;
    
    // Cronómetro
    private long startTime;
    private long elapsedTime; // Tiempo jugado en milisegundos
    
    // Controlar niveles y frames
    public int nivelSeleccionado = 1;
    public int frameSeleccionado = 1;
    
    Font font = new Font("Dirty Headline", Font.BOLD, 30);
    
	// ------------------------------------------------------------------------------

	public Window() {
        setTitle("Bomber Postal");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.setFocusable(true);
		add(canvas);
	    canvas.addKeyListener(this);
       
        // Jugador
        player = new Player(speedPlayer, Color.yellow, structures);        
        
        startTime = System.nanoTime();
	}
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {
		
		// ---------------------------------------- Control de resolucion
	    switch (resolucion) {
	        case 1:
	            WIDTH = 1920;
	            HEIGHT = 1000;
	            break;
	        case 2:
	            WIDTH = 1280;
	            HEIGHT = 720;
	            bloqueTam = 60;
	            break;
	        case 3:
	            WIDTH = 1366;
	            HEIGHT = 768;
	            break;
	        case 4:
	            WIDTH = 800;
	            HEIGHT = 600;
	            break;
	        case 5:
	            WIDTH = 640;
	            HEIGHT = 480;
	            break;
	        default:
	            WIDTH = 640;
	            HEIGHT = 480;
	            break;
	    }

	    // Actualizar tamaño del JFrame y del Canvas
	    setSize(WIDTH, HEIGHT);
	    canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	    canvas.revalidate();

	    // Obtener medidas de la ventana para el responsive
	    alturaResponsive = getHeight();
	    anchoResponsive = getWidth();
	    espaciado = getWidth() / 20;

		// ACTUALIZACION DE ENTIDADES
		player.update(leftPressed, rightPressed, space, upPressed,aPressed,dPressed,wPressed,downPressed,sPressed,cPressed);

		 // Calcular dimensiones utilizables
	    int usableHeight = getHeight() - getInsets().top - getInsets().bottom;
	    int usableWidth = getWidth() - getInsets().left - getInsets().right;
	    
	    // Definir las dimensiones del área azul (cuadrícula) y verde (tabla)
	    int cuadriculaWidth = (int) (usableWidth * 0.75);
	    int tablaWidth = (int) (usableWidth * 0.25);

	    int numFilas = 10;
	    int numColumnas = 15;
	    
	    // Calcular el tamaño de los bloques para que se adapten a las dimensiones del área azul
	    int bloqueWidth = cuadriculaWidth / numColumnas;
	    int bloqueHeight = usableHeight / numFilas;
	    
	    // Usar el tamaño más pequeño para asegurar que los bloques quepan en ambos ejes
	    bloqueTam = Math.min(bloqueWidth, bloqueHeight);
	    
	    // Centrar la cuadrícula dentro del área azul
	    int offsetX = (cuadriculaWidth - (bloqueTam * numColumnas)) / 2;
	    int offsetY = (usableHeight - (bloqueTam * numFilas)) / 2;

	    
	    
		if (nivelSeleccionado == 1) {
		    if(frameSeleccionado == 1 && invalido != 1) {
		    	 structures.add(new Structure(bloqueTam * 2 + offsetX, bloqueTam * 3 + offsetY , bloqueTam, bloqueTam, Color.green)); 
			    
			    invalido = 1;
		    }   
		    
		    else if(frameSeleccionado == 2 && invalido != 1) {
		    	 structures.add(new Structure(bloqueTam * 2 + offsetX, bloqueTam * 6 + offsetY , bloqueTam, bloqueTam, Color.red)); 
				    
			    invalido = 1;
		    } 
		}




		// Actualizar el tiempo jugado
        elapsedTime = System.nanoTime() - startTime; // Calcular tiempo transcurrido
	}
	
	
	// funcion que dibujara todo el juego (cada segundo literalmente)
	private void draw() {
	    bs = canvas.getBufferStrategy();
	    if (bs == null) {
	        canvas.createBufferStrategy(3);
	        return;
	    }
	    
	    g = bs.getDrawGraphics();
	    
	    // Fondo gris
	    g.setColor(Color.gray);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    // Calcular dimensiones utilizables
	    int usableHeight = getHeight() - getInsets().top - getInsets().bottom;
	    int usableWidth = getWidth() - getInsets().left - getInsets().right;
	    
	    // Definir las dimensiones del área azul (cuadrícula) y verde (tabla)
	    int cuadriculaWidth = (int) (usableWidth * 0.75);
	    int tablaWidth = (int) (usableWidth * 0.25);

	    // Dibujar el área azul (cuadrícula)
	    g.setColor(Color.blue);
	    g.fillRect(0, 0, cuadriculaWidth, usableHeight);
	    
	    // Dibujar el área negra (tabla lateral)
	    g.setColor(Color.black);
	    g.fillRect(cuadriculaWidth, 0, tablaWidth, usableHeight);
	    
	    g.setFont(font);
	    g.setColor(Color.WHITE); 
	    int seconds = (int) (elapsedTime / 1_000_000_000); // Convertir de nanosegundos a segundos
	    g.drawString("Tiempo jugado: " + seconds + "s", cuadriculaWidth + cuadriculaWidth / 50, 50);
	    g.drawString("Puntuacion: " + puntuacion, cuadriculaWidth + cuadriculaWidth / 50, 100);
	    g.drawString("Vidas: " + player.vida, cuadriculaWidth + cuadriculaWidth / 50, 150);
	    
	    // Establecer el número fijo de filas y columnas
	    int numFilas = 10;
	    int numColumnas = 15;
	    
	    // Calcular el tamaño de los bloques para que se adapten a las dimensiones del área azul
	    int bloqueWidth = cuadriculaWidth / numColumnas;
	    int bloqueHeight = usableHeight / numFilas;
	    
	    // Usar el tamaño más pequeño para asegurar que los bloques quepan en ambos ejes
	    bloqueTam = Math.min(bloqueWidth, bloqueHeight);
	    
	    // Centrar la cuadrícula dentro del área azul
	    int offsetX = (cuadriculaWidth - (bloqueTam * numColumnas)) / 2;
	    int offsetY = (usableHeight - (bloqueTam * numFilas)) / 2;
	    
	    // Dibujar la cuadrícula con el tamaño ajustado
	    g.setColor(Color.black);
	    for (int i = 0; i < numColumnas; i++) {
	        for (int j = 0; j < numFilas; j++) {
	            int x = offsetX + i * bloqueTam;
	            int y = offsetY + j * bloqueTam;
	            
	            // Solo dibujar estructuras indestructibles en posiciones alternas (como en Bomberman)

                g.drawRect(x, y, bloqueTam, bloqueTam);  // Dibuja el rectángulo de la estructura

	        }
	    }
	
	    
	    // Dibujar el jugador
	    player.draw(g);
	    
	    // Dibuja las estructuras
	    for (Structure structure : structures) {
	        structure.draw(g);
	    }
	    
	    g.dispose();
	    bs.show();
	}
	
	public void run() {
		long now = 0;
		long lastTime = System.nanoTime();
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / TARGETTIME;
			lastTime = now;
			
			if (delta >= 1) {
				update();
				draw();
				delta--;
			}
		}
		stop();
	}
	
	// funciones para empezar/detener el juego
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// FUNCIONES PARA TECLAS
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			aPressed = true;
			break;
		case KeyEvent.VK_D:
			dPressed = true;
			break;
		case KeyEvent.VK_W:
			wPressed = true;
			break;
		case KeyEvent.VK_S:
			sPressed = true;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = true;
			break;
		case KeyEvent.VK_UP:
			upPressed = true;
			break;
		case KeyEvent.VK_DOWN:
			downPressed = true;
			break;
		case KeyEvent.VK_C:
			cPressed = true;
			break;
		case KeyEvent.VK_SPACE:
			space = true;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			aPressed = false;
			break;
		case KeyEvent.VK_D:
			dPressed = false;
			break;
		case KeyEvent.VK_W:
			wPressed = false;
			break;
		case KeyEvent.VK_S:
			sPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = false;
			break;
		case KeyEvent.VK_UP:
			upPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			downPressed = false;
			break;
		case KeyEvent.VK_C:
			cPressed = false;
			break;
		case KeyEvent.VK_SPACE:
			space = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// no se necesita
	}
}