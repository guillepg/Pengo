package pengo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import pengo.maquinaEstados.MaquinaEstados;

public class Pengo extends Canvas {

	private static final long serialVersionUID = 1L;
	
	private long dist = 1;
	private long tiempoAnterior = 0;
	
	private boolean jugando = true;
	
	final public static int ancho = 510;
	final public static int alto = 670;
	
    private Canvas canvas;
    private BufferStrategy strategy;
    
    private MaquinaEstados maquinaEstados;
    private Temporizador tmp;

	public Pengo() {
		
    	tmp = new Temporizador();
		
    	maquinaEstados = new MaquinaEstados(tmp);
		
		Pantalla pengo = new Pantalla(maquinaEstados);
		pengo.iniciar();
		
		// Create canvas for painting...
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.BLACK);
		canvas.setSize(ancho, alto);
		
		pengo.add(canvas);
		pengo.pack();
		pengo.setLocationRelativeTo(null);
		pengo.setVisible(true);

    	canvas.createBufferStrategy(2);
    	do {
    		strategy = canvas.getBufferStrategy();
    	} while (strategy == null);
		    	
	}

	public static void main(String[] args) {
		Pengo pengo = new Pengo();
		pengo.dibujar();
	}
	
	private void dibujar(){
		

    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		jugando = (maquinaEstados.getEstado()<=4);
		
		while(jugando){
			
			long tiempoActual = System.nanoTime();	
			dist += (tiempoActual - tiempoAnterior);
			tiempoAnterior = tiempoActual;
			
			if((dist/tmp.actualizar) >= 1){
				tmp.vecesEjecutado++;
				
				//dibujar
				Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();
				
				maquinaEstados.dibujar(g2d);

				g2d.dispose();
				strategy.show();
				
				dist = 0;		
				
				jugando = (maquinaEstados.getEstado()<=4);					
			}			
		}
		
		System.exit(0);
	}
	
}