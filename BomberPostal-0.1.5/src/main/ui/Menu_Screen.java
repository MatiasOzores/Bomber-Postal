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
    
    private Font tittleFont = new Font("BREAK IT", Font.PLAIN, 60);
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

        // Dibuja fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
        
        // Dibuja titulo
        g.setFont(tittleFont);
        int tittleWidth = g.getFontMetrics().stringWidth(tittle); // variable para que quede literalmente centrado
        g.setColor(Color.gray);
        g.drawString(tittle, ((Window.WIDTH/2)-(tittleWidth/2))-2, (Window.HEIGHT/2)-123);
              
        // ---------------------------------------------- Termina el dibujo
        
        g.dispose();
        bs.show();		
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
