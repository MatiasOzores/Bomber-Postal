package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.ui.Window;

public class Bomb {
    private int x, y;
    private int size;
    private long creationTime;
    private final long explosionDelay = 3000; // Tiempo antes de la explosión
    private boolean exploded = false;
    private Image bombImage0, bombImage1, bombImage2, bombImage3, bombImage4; // Imágenes para la secuencia de la bomba
    private Image explosionImage; // Imagen de la explosión

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = (int)(Window.anchoResponsive * 0.04);
        this.creationTime = System.currentTimeMillis();
        
        // Cargar las imágenes de la bomba y la explosión
        try {
            bombImage0 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase1.png"));
            bombImage1 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase2.png"));
            bombImage2 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase3.png"));
            bombImage3 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase4.png"));
            bombImage4 = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase5.png"));
            explosionImage = ImageIO.read(new File("assets/images/SpritesBomberPostal/Objetos/Bomba/TNT-Fase5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Actualizar si la bomba ha explotado basado en el tiempo
        if (!exploded && (System.currentTimeMillis() - creationTime) >= explosionDelay) {
            exploded = true;
        }
    }

    public void draw(Graphics g) {
        if (exploded) {
            // Dibujar la imagen de la explosión
            g.drawImage(explosionImage, x, y, size, size, null);
        } else {
            // Determinar qué imagen mostrar basándose en el tiempo transcurrido
            long elapsedTime = System.currentTimeMillis() - creationTime;
            int phase = (int)((elapsedTime / (explosionDelay / 5)) % 5); // 5 fases para la bomba
            
            // Dibujar la imagen correspondiente a la fase de la bomba
            switch (phase) {
                case 0:
                    g.drawImage(bombImage0, x, y, size, size, null);
                    break;
                case 1:
                    g.drawImage(bombImage1, x, y, size, size, null);
                    break;
                case 2:
                    g.drawImage(bombImage2, x, y, size, size, null);
                    break;
                case 3:
                    g.drawImage(bombImage3, x, y, size, size, null);
                    break;
                case 4:
                    g.drawImage(bombImage4, x, y, size, size, null);
                    break;
            }
        }
    }

    public boolean hasExploded() {
        return exploded;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
