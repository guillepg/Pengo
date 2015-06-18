package pengo.sonidos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.mp3transform.Decoder;


public class Sonidos_runnable implements Runnable {

    private Decoder decoder = new Decoder();
	public File[] sonidos;
    private final String MP3_SUFFIX = ".mp3";
    private boolean fin;
    private int actual;
    
    public Sonidos_runnable(File[] f){
    	sonidos = f;
    	//orden: preparando laberinto, todo listo, inicial, normal, fin bueno, fin malo
    	fin = false; actual=0;
    }
    
	@Override
	public void run() {
		try {
			while(!fin){
				FileInputStream in = new FileInputStream(sonidos[actual]);
				play(in); 
				in.close();
				if(actual<3) actual++;
	        }
		} catch (IOException e) {e.printStackTrace();}
		//aqui se reproduciria el de fin bueno o malo
		
	}
	
	public void play(FileInputStream fis) throws IOException {
        //player.setCurrentFile(file);
		fin = false;
		BufferedInputStream bis = new BufferedInputStream(fis, 128 * 1024);
		decoder.play("", bis);
		bis.close();
    }
	
	boolean isMp3(File f) {
	    return f.getName().toLowerCase().endsWith(MP3_SUFFIX);
	}

	void stop(){
		fin=true;
	}
}
