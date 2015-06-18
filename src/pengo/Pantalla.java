package pengo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import pengo.maquinaEstados.MaquinaEstados;

public class Pantalla extends JFrame {

	private static final long serialVersionUID = 1L;
	private MaquinaEstados maquinaEstados;
	
	public Pantalla(MaquinaEstados m){
		maquinaEstados = m;
	}
	
	public void iniciar(){
		this.setTitle("PENGO");
		this.setIgnoreRepaint(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		addKeyListener(new TAdapter());
	}
	
	private class TAdapter extends KeyAdapter{
		
		public void keyReleased(KeyEvent e){
			maquinaEstados.keyReleased(e);
		}
		
		public void keyPressed(KeyEvent e){
			maquinaEstados.keyPressed(e);
		}
	}
}
