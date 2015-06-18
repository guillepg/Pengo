package pengo.sonidos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.mp3transform.Decoder;

public class Sonidos extends Thread{

    private final String MP3_SUFFIX = ".mp3";
    private Decoder decoder;
	
    public static void main(String[] args){}
    
	public void run(){
		decoder = new Decoder();
		try {
			File prep1 = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/preparando_laberinto.mp3");
			File prep2 = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/preparado.mp3");
			File inicial = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/bucle_completo1.mp3");
			File bucle_normal = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/bucle_completo2.mp3");
			File fin_bueno = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/partida_ganada.mp3");
			File fin_malo = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/muerto.mp3");
			
			/*---en el OTRO hilo se reproduce todo lo de fondo en bucle---*/
			Sonidos_runnable fondo = new Sonidos_runnable( 
					new File[]{prep1,prep2,inicial,bucle_normal,fin_bueno,fin_malo} );
			Thread tf = new Thread(fondo);
			tf.start();
			
			/*---esto va en el constructor de Juego.java---*/
//			Sonidos son = new Sonidos(); son.start();
			
			/*---en ESTE hilo se reproducen fragmentos como romper/mover bloque---*/
//			File aaa = new File("C:/Users/Guille/Dropbox/Clase/Videojuegos/sonidos/bloque1.mp3");
//			FileInputStream in = new FileInputStream(aaa);
//			this.play(in); 
//			in.close();
			
			/*---cuando proceda, se para el hilo de fondo---*/
//			tf.stop();
			/*reproducir sonido de fin bueno, malo o muerte*/
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play(FileInputStream fis) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(fis, 128 * 1024);
		decoder.play("", bis);
		bis.close();
    }
	
	boolean isMp3(File f) {
	    return f.getName().toLowerCase().endsWith(MP3_SUFFIX);
	}

}
