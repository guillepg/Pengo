package pengo.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Temporizador;
import pengo.globales.Globales;

public class MenuPrincipal extends JPanel {

	private static final long serialVersionUID = 1L;
	private Temporizador tmp;
	private Globales globales;
	private GlobalesMenu globalesMenu;
	private int avanzar, i, credit;
	private boolean pintado;
	
	private String[] startimg = {
			"./img/menu/push-startButton1.png",
			"./img/menu/push-startButton2.png",
	};
	
	private String players = "./img/menu/1player.png";
	private String player_1 = "./img/varios/player_1.png";
	private String numPtos_0 = "./img/numeros/puntos/0.png";
	
	private String c = "./img/menu/credit.png";
	
	private String b = "./img/menu/bonus.png";

	private String[] num = {
			"./img/numeros/credits/0.png",
			"./img/numeros/credits/1.png",
			"./img/numeros/credits/2.png",
			"./img/numeros/credits/3.png",
			"./img/numeros/credits/4.png",
			"./img/numeros/credits/5.png",
			"./img/numeros/credits/6.png",
			"./img/numeros/credits/7.png",
			"./img/numeros/credits/8.png",
			"./img/numeros/credits/9.png",
	};

	public MenuPrincipal(Temporizador temp, Globales globales) {
		
		globalesMenu = new GlobalesMenu();
		tmp = temp;
		avanzar=i=0;
		credit = 1;
		pintado = false;
		this.globales = globales;
	}

	public void menu(Graphics2D g2d) {
		if(tmp.vecesEjecutado%2==0){
			//dibujarEncabezado(g2d);
			
            avanzar++;
    		if(avanzar%5==0) {
    			start(g2d);
    			players(g2d);
    			creditos(g2d);
    			bonus(g2d);
    		}
    		pintado = true;
		}
	}
	
	private void start(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(globalesMenu.COORD_X_INI_START, globalesMenu.COORD_X_INI_START, globalesMenu.ANCHO_IMG_START, globalesMenu.ALTO_IMG_START);
		g2d.drawImage(new ImageIcon(startimg[i%2]).getImage(), globalesMenu.COORD_X_INI_START, globalesMenu.COORD_Y_INI_START, this);
		i++; 
	}
	
	private void players(Graphics2D g2d) {
		g2d.drawImage(new ImageIcon(players).getImage(), globalesMenu.COORD_X_INI_PLAYER, globalesMenu.COORD_Y_INI_PLAYER, this);
	}
	
	private void creditos(Graphics2D g2d) {
		g2d.drawImage(new ImageIcon(c).getImage(), globalesMenu.COORD_X_INI_CREDIT, globalesMenu.COORD_Y_INI_CREDIT, this);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(globalesMenu.COORD_X_INI_NUM_CREDIT, globalesMenu.COORD_Y_INI_CREDIT, globalesMenu.ANCHO_TOT_IMG_NUM, globalesMenu.ALTO_IMG_NUM);
		g2d.drawImage(new ImageIcon(num[credit%10]).getImage(), 
				globalesMenu.COORD_X_INI_NUM_CREDIT + globalesMenu.ANCHO_IMG_NUM, globalesMenu.COORD_Y_INI_CREDIT, this);
		if(credit == 10)
			g2d.drawImage(new ImageIcon(num[1]).getImage(), 
					globalesMenu.COORD_X_INI_NUM_CREDIT, globalesMenu.COORD_Y_INI_CREDIT, this);	
		else if(credit>10)
			g2d.drawImage(new ImageIcon(num[credit/10]).getImage(), 
					globalesMenu.COORD_X_INI_NUM_CREDIT, globalesMenu.COORD_Y_INI_CREDIT, this);	
	}
	
	private void bonus(Graphics2D g2d) {
		g2d.drawImage(new ImageIcon(b).getImage(), globalesMenu.COORD_X_INI_BONUS, globalesMenu.COORD_Y_INI_BONUS, this);
	}
	
	private void dibujarEncabezado(Graphics2D g2d){
		//Dibujar icono de jugador 1
		g2d.drawImage(new ImageIcon(player_1).getImage(), 
				globales.COORD_X_INI_BLOQUES, globales.COORD_Y_INI_PUNTOS, this);
		g2d.drawImage(new ImageIcon(numPtos_0).getImage(), 
				globales.COORD_X_FIN_PUNTOS - globales.ANCHO_IMG_NUM_PTOS, globales.COORD_Y_INI_PUNTOS, this);
	}
	
	public boolean pintado() {
		return pintado;
	}

	public int masCreditos() {
		if(credit<99) 
			credit++;
		return credit;
	}
	
	public void menosCreditos(){
		credit--;
	}
}
