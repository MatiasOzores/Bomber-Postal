package main.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Menu_Screen extends JFrame implements Runnable, KeyListener{
	


	// -------------------------- Declaracion de variables --------------------------
	
	private static final long serialVersionUID = 1L; 
	private Canvas canvas;
    private Thread thread;
    private boolean running;
    private boolean startGame = false;

    private BufferStrategy bs;
    private Graphics g;
    
    private Font tittleFont = new Font("BREAK IT", Font.PLAIN, 120);
    private String tittle = "Bomber Postal";
    
	// ------------------------------------------------------------------------------
    
    public Menu_Screen() {
        // seteamos los valores que necesitamos si o si para el frame
        setTitle("Undercraft");
        setSize(Window.WIDTH, Window.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);  

        // instanciamos canvas y sus dimensiones (con las mismas dimensiones de window)
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setMaximumSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setMinimumSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(this);
        
        setVisible(true);
        
      
    }

	public void update() {
		
	}
	
	public void draw() {
	    bs = canvas.getBufferStrategy();
	    if (bs == null) {
	        canvas.createBufferStrategy(3);
	        return;
	    }
	    g = bs.getDrawGraphics();
	    
	    // ---------------------------------------------- Empieza el dibujo

	    // Dibuja fondo con un tono suave y sangriento
	    Color softRed = new Color(89, 15, 15);  // Un rojo oscuro y suave
	    Color lightRed = new Color(128, 35, 35);  // Un rojo más claro para el degradado
	    for (int i = 0; i < Window.HEIGHT; i++) {
	        float ratio = (float) i / Window.HEIGHT;
	        Color gradient = blendColors(softRed, lightRed, ratio);
	        g.setColor(gradient);
	        g.fillRect(0, i, Window.WIDTH, 1);
	    }

	    // Dibuja algunas salpicaduras suaves
	    g.setColor(new Color(150, 50, 50, 180));  // Transparente para suavidad
	    for (int i = 0; i < 5; i++) {
	        int splashX = (int) (Math.random() * Window.WIDTH);
	        int splashY = (int) (Math.random() * Window.HEIGHT);
	        int splashSize = (int) (Math.random() * 80) + 40;  // Tamaños aleatorios pero más pequeños
	        g.fillOval(splashX, splashY, splashSize, splashSize);
	    }
	    
	    // Dibuja el título
	    g.setFont(tittleFont);
	    int tittleWidth = g.getFontMetrics().stringWidth(tittle); // variable para centrar
	    g.setColor(new Color(255, 70, 70));  // Un rojo suave
	    g.drawString(tittle, ((Window.WIDTH / 2) - (tittleWidth / 2)) - 2, (Window.HEIGHT / 2) - 123);
	          
	    // ---------------------------------------------- Termina el dibujo
	    
	    g.dispose();
	    bs.show();        
	}

	// Método para mezclar dos colores (degradado suave)
	private Color blendColors(Color color1, Color color2, float ratio) {
	    int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
	    int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
	    int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
	    return new Color(red, green, blue);
	}


	
	
	@Override
	public void run() {
        while (running) {
            draw();
            update();
            if (startGame) {
                running = false;
                this.dispose(); // Cerrar el menú
                new Window().start(); // Iniciar el juego
            }
            
            /*
            if(startOptions) {
            	running = false;
            	this.dispose();
            	new Menu_Options().start();
            }
            
            */
        }
	}
	
	public void start() {
       thread = new Thread(this);
       thread.start();
       running = true;		
	}
	
	public void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }		
	}
	
	
	// Funciones --------------------------------------------------------------
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            startGame = true; 
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
