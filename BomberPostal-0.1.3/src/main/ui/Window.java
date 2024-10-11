package main.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
    
    Font font = new Font("Dirty Headline", Font.BOLD, (int)(Window.anchoResponsive * 0.06));
    
    // Establecer limites del jugador con los bordes
    public static int limiteJugadorIzquierda, limiteJugadorDerecha, limiteJugadorArriba, limiteJugadorAbajo;
    
    // ------------------------------------------------------------------------------ Imagenes
    private Image barraLateralImage;
    
    
    // Imagenes estructuras N1 F1
    private Image N1F1PA1,N1F1PA2,N1F1PA3,N1F1PA4,N1F1PA5,N1F1PA6,N1F1PA7,N1F1PA8,N1F1PA9, N1F1SUELO, N1F1BORDE, N1F1ESQ_IZQ_INF, N1F1ESQ_IZQ_SUP, N1F1ESQ_DER_INF, N1F1ESQ_DER_SUP;
    
    
    // ------------------------------------------------------------------------------ Fin Imagenes
    
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
        
        try {
            barraLateralImage = ImageIO.read(new File("assets/images/lateralImage.jpg"));
            N1F1PA1 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedColumna.png"));
            N1F1PA2 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedExtremoDerecho.png"));
            N1F1PA3 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedExtremoInferior.png"));
            N1F1PA4 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedExtremoIzquierdo.png"));
            N1F1PA5 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedExtremoSuperior.png"));
            N1F1PA6 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedInferiorDerecho.png"));
            N1F1PA7 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedInferiorIzquierdo.png"));
            N1F1PA8 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedSuperiorDerecho.png"));
            N1F1PA8 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/ParedSuperiorIzquierdo.png"));
            N1F1SUELO = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BaldosaOficina.png"));
            N1F1BORDE= ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BloqueBordeOficina.png"));
            N1F1ESQ_IZQ_INF = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BordeOficinaEsquinaInferiorIzquierda.png"));
            N1F1ESQ_IZQ_SUP = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BordeOficinaEsquinaSuperiorIzquierda.png"));
            N1F1ESQ_DER_INF = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BordeOficinaEsquinaInferiorDerecha.png"));
            N1F1ESQ_DER_SUP = ImageIO.read(new File("assets/images/SpritesBomberPostal/Mapa/Dia1/1_Oficina/BordeOficinaEsquinaSuperiorDerecha.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {

		// ---------------------------------------- Control de resolucion
	    switch (resolucion) {
	        case 1:
	            WIDTH = 1800;
	            HEIGHT = 900;
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
	            WIDTH = 1024;
	            HEIGHT = 768;
	            break;
	        case 5:
	            WIDTH = 800;
	            HEIGHT = 480;
	            break;
	        default:
	            WIDTH = 800;
	            HEIGHT = 480;
	            break;
	    }

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
		    	 structures.add(new Structure(bloqueTam * 2 + offsetX, bloqueTam * 3 + offsetY , bloqueTam, bloqueTam, N1F1PA1)); 
		    	 structures.add(new Structure(bloqueTam * 1 + offsetX, bloqueTam * 3 + offsetY , bloqueTam, bloqueTam, N1F1PA3));
		    	 // Mensaje para Amodeo: Podes modificar los 3 atributos de las estructuras, la imagen y la posicion de x e y. 
		    	 /* 
		    	  * 
		    	  * Tu trabajo va ser estructurar un mapa de cada uno de los frames del juego a lo largo de todo el proyecto
		    	  * Es mas de creatividad que otra cosa, porque programarlo la verdad es que es muy facil, solo mueves los bloques y le cambias la imagen para que vayan acorde a la estructura
		    	  * Por cada frame te voy a decir la tematica para que te orientes mejor por ejemplo el primero se trata de una oficina. Por ahora el sistema de niveles es asi, todavia no lo separe por clases porque se me complico
		    	  * Solo me vas a proporcionar toda esta parte del codigo con todos las estructuras colocadas perfectamente, es decir, todo lo que abarca if(frameSeleccionado == 1 && invalido != 1) { } todo lo que estaria dentro de estas dos llaves
		    	  * Osea donde estoy escribiendo aca dentro, todo eso.
		    	  * 
		    	  * Pautas: En este primer frame sentite libre de crear todas las estructuras que quieras con structures.add(new Structure(bloqueTam * 2 + offsetX, bloqueTam * 3 + offsetY , bloqueTam, bloqueTam, N1F1PA1)); 
		    	  * 
		    	  * Luego en los siguientes frames te voy a ir diciendo que cosas van cambiando.
		    	  *  
		    	  *  
		    	  *  
		    	  * */
			    structures.get(0).image = N1F1PA2;
			    structures.get(0).x = bloqueTam * 2 + offsetX;
			    structures.get(0).y = bloqueTam * 6 + offsetY;
		    	 
			    invalido = 1;
		    }   
		    
		    else if(frameSeleccionado == 2 && invalido != 1) {
		    	 structures.add(new Structure(bloqueTam * 2 + offsetX, bloqueTam * 6 + offsetY , bloqueTam, bloqueTam, N1F1PA2)); 

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
	    
	    // -------------- FONDO DEFAULT -----------------------
	    g.setColor(Color.gray);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    // -------------- FIN FONDO DEFAULT -------------------
	    

	    // ---------------------------------------------------- GRILLA
	    int usableHeight = getHeight() - getInsets().top - getInsets().bottom;
	    int usableWidth = getWidth() - getInsets().left - getInsets().right;
	    int cuadriculaWidth = (int) (usableWidth * 0.75);
	    int tablaWidth = (int) (usableWidth * 0.25);
	    g.setColor(Color.black);
	    g.fillRect(0, 0, cuadriculaWidth, usableHeight);
	    
	    int numFilas = 10;
	    int numColumnas = 15;
	    int bloqueWidth = cuadriculaWidth / numColumnas;
	    int bloqueHeight = usableHeight / numFilas;
	    bloqueTam = Math.min(bloqueWidth, bloqueHeight);
	    int offsetX = (cuadriculaWidth - (bloqueTam * numColumnas)) / 2;
	    int offsetY = (usableHeight - (bloqueTam * numFilas)) / 2;
	    
	    for (int i = 0; i < numColumnas; i++) {
	        for (int j = 0; j < numFilas; j++) {
	            int x = offsetX + i * bloqueTam;
	            int y = offsetY + j * bloqueTam;
	            if(nivelSeleccionado == 1) {
	            	if(frameSeleccionado == 1) {
	            		
	            		// Arriba a la izquierda
	            		if(j == 0 && i == 0) {
			            	g.drawImage(N1F1ESQ_IZQ_SUP, x, y, bloqueTam, bloqueTam, null);
	            		}
	            		
	            		// Arriba a la derecha
	            		else if(j == 0 && i == numColumnas-1) {
			            	g.drawImage(N1F1ESQ_DER_SUP, x, y, bloqueTam, bloqueTam, null);
	            		}
	            		
	            		// Abajo a la derecha
	            		else if(j == numFilas-1 && i == numColumnas-1) {
			            	g.drawImage(N1F1ESQ_DER_INF, x, y, bloqueTam, bloqueTam, null);
	            		}
	            		
	            		// Abajo a la izquierda
	            		else if(j == numFilas-1 && i == 0) {
			            	g.drawImage(N1F1ESQ_IZQ_INF, x, y, bloqueTam, bloqueTam, null);
	            		}
	            		
	            		// Bordes de arriba 
	            		else if(j == 0) {
			            	g.drawImage(N1F1BORDE, x, y, bloqueTam, bloqueTam, null);		            		
		            	}
		            	
	            		// Bordes de abajo
		            	else if(j == numFilas - 1) {
		            	    Graphics2D g2d = (Graphics2D) g.create(); 
		            	    
		            	    g2d.rotate(Math.toRadians(180), x + bloqueTam / 2, y + bloqueTam / 2);
		            	    
		            	    g2d.drawImage(N1F1BORDE, x-1, y-1, bloqueTam, bloqueTam, null);
		            	    
		            	    g2d.dispose(); 
		            	}
		            	
	            		// Bordes de la izquierda
		            	else if(i == 0) {
		            	    Graphics2D g2d = (Graphics2D) g.create(); 
		            	    
		            	    g2d.rotate(Math.toRadians(-90), x + bloqueTam / 2, y + bloqueTam / 2);
		            	    
		            	    g2d.drawImage(N1F1BORDE, x-1, y, bloqueTam, bloqueTam, null);
		            	    g2d.dispose(); 
		            	    
		            	    limiteJugadorIzquierda = bloqueTam * 1 + offsetX;
		            	    limiteJugadorDerecha = (int)(bloqueTam * 13.25 + offsetX);
		            	    limiteJugadorArriba = (int)(bloqueTam * 1+ offsetY);
		            	    limiteJugadorAbajo = (int)(bloqueTam * 8.25 + offsetY);
		            	}
		            	
	            		// Bordes de la derecha
		            	else if(i == numColumnas - 1) {
		            	    Graphics2D g2d = (Graphics2D) g.create(); 
		            	    
		            	    g2d.rotate(Math.toRadians(90), x + bloqueTam / 2, y + bloqueTam / 2);
		            	    
		            	    g2d.drawImage(N1F1BORDE, x, y-1, bloqueTam, bloqueTam, null);
		            	    
		            	    g2d.dispose(); 	
		            	    

		            	}

		            		
	            		// Suelo
		            	else {
		            		g.drawImage(N1F1SUELO, x, y, bloqueTam, bloqueTam, null);
		            	}
	            	}
	            }
	            
	              
	        }
	    }
	    // ---------------------------------------------------- FIN GRILLA
	    
	 // ---------------------------------------------------- BARRA LATERAL
	
        // Gradiente suave en lugar de un fondo sólido
        GradientPaint gradient = new GradientPaint(
            cuadriculaWidth, 0, new Color(45, 45, 48), 
            cuadriculaWidth + tablaWidth, usableHeight, new Color(30, 30, 34));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gradient);
        g2d.fillRect(cuadriculaWidth, 0, tablaWidth, usableHeight);


	    

	    // Texto estilizado
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Smash", Font.PLAIN, (int)(Window.anchoResponsive * 0.028)));
	    g.drawString("MARCADOR: " + puntuacion, cuadriculaWidth + (int)(Window.anchoResponsive * 0.025), (int)(Window.alturaResponsive * 0.1));
	    
	    g.setColor(Color.RED);
	    g.setFont(new Font("Smash", Font.PLAIN, (int)(Window.anchoResponsive * 0.028)));
	    g.drawString("BAJAS: " + puntuacion, cuadriculaWidth + (int)(Window.anchoResponsive * 0.06), (int)(Window.alturaResponsive * 0.2));
	    
	    
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Smash", Font.BOLD, (int)(Window.anchoResponsive * 0.02)));
	    g.drawString("VIDAS: " + player.vida, cuadriculaWidth + (int)(Window.anchoResponsive * 0.0725), (int)(Window.alturaResponsive * 0.3));
	    
	    g.setFont(new Font("Smash", Font.BOLD, (int)(Window.anchoResponsive * 0.02))); // Fuente más moderna
	    g.setColor(Color.WHITE);
	    int seconds = (int) (elapsedTime / 1_000_000_000); // Convertir de nanosegundos a segundos
	    g.drawString("TIEMPO: " + seconds + "s", cuadriculaWidth + (int)(Window.anchoResponsive * 0.062), (int)(Window.alturaResponsive * 0.9));
	    
	    
	    


	    // ---------------------------------------------------- FIN BARRA LATERAL

        
	   
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