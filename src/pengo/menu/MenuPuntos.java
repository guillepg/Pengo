package pengo.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Temporizador;

public class MenuPuntos extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private int puntos;
	private String ptos;
	private String[] score;
	private String[] numeros_puntos;
    private boolean acabado;
    private Temporizador tmp;
    private int op;
    private int j = 180, yScore = 250, yPtos = 290;
    private int i=0, k = 0;
	
	public MenuPuntos(int ptos, Temporizador t){
		puntos = ptos;
		this.ptos = String.valueOf(puntos);
		tmp = t;
		op = 0;
		acabado = false;
		iniciarImagenes();
	}
	
	public void iniciarImagenes(){
		score = new String[]{
				"./img/letras/score/S.png",
				"./img/letras/score/C.png",
				"./img/letras/score/O.png",
				"./img/letras/score/R.png",
				"./img/letras/score/E.png",			
			};
		numeros_puntos = new String[]{
				"./img/numeros/puntos/0.png",
				"./img/numeros/puntos/1.png",
				"./img/numeros/puntos/2.png",
				"./img/numeros/puntos/3.png",
				"./img/numeros/puntos/4.png",
				"./img/numeros/puntos/5.png",
				"./img/numeros/puntos/6.png",
				"./img/numeros/puntos/7.png",
				"./img/numeros/puntos/8.png",
				"./img/numeros/puntos/9.png"};
	}

	public void menu(Graphics2D g2d) {
		if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/3) )%tmp.frames_per_sec )==0 ){
        	switch(op){
        	case 0:
        		score(g2d);
        		break;
        	case 1:
        		dibujarPuntos(g2d);
        		break;
        	default: 
        		acabado = true;
            	try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	break;
        	}
        }
	}

	private void score(Graphics2D g2d){
		System.out.println(score[i]);
        g2d.drawImage(new ImageIcon(score[i]).getImage(), j, yScore, this);
        j += 25; i++;
    	if(i==score.length) {
    		op++;
    		j = 180;
    	}
	}
	
	public void dibujarPuntos(Graphics2D g2d){
		int num = Integer.parseInt(String.valueOf(ptos.charAt(k)));;
			
		g2d.drawImage(new ImageIcon(numeros_puntos[num]).getImage(), j, yPtos, this);
		j += 25;
		k++;
		if(k == ptos.length()) op++;
	}
	
	public boolean isAcabado() {
		return acabado;
	}
}
