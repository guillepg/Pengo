package pengo.maquinaEstados;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import pengo.Pengo;
import pengo.Temporizador;
import pengo.globales.Globales;
import pengo.juego.Juego;
import pengo.menu.MenuFin;
import pengo.menu.MenuInicio;
import pengo.menu.MenuPrincipal;
import pengo.menu.MenuPuntos;

public class MaquinaEstados {
	
	private int estado;
	private int monedas;
	private int nivel = 1;
	private int maxNiveles = 10;
	private int vidasEnemigo;
	private int vidasPinguino;
	private int puntos = 0;
	private Juego juego;
	private MenuInicio menu;
	private MenuFin fin;
	private MenuPuntos mptos;
	private MenuPrincipal menuPrincipal;
	private Temporizador tmp;
	private Globales globales;
	private boolean seguir, limpiar, haGanado, startPaintPoints = true, primera = true;
	
	public MaquinaEstados(Temporizador tmp){
		seguir = limpiar = false;
		estado = 0;
		globales = new Globales();
		vidasEnemigo = globales.MAX_NUM_ENEMIGOS;
		menu = new MenuInicio(tmp);
		fin = new MenuFin(tmp);
		menuPrincipal = new MenuPrincipal(tmp, globales);
		juego = new Juego(tmp, nivel, 3, vidasEnemigo, globales, 0, true);
		this.tmp = tmp;
		this.monedas = 1;
	}
	
	public void dibujar(Graphics2D g2d) {
		switch(estado){
			case 0: //Apertura
				menu.menu(g2d);
				if(menu.isAcabado()) {
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, Pengo.ancho, Pengo.alto);
					estado++;
				}
				break;
			case 1: //Menu
				if(limpiar){ 
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, Pengo.ancho, Pengo.alto);
					limpiar=false; 
				}
				menuPrincipal.menu(g2d);
				if(seguir){
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, Pengo.ancho, Pengo.alto);
					estado++;
				}
				if(!primera){
					juego = new Juego(tmp, nivel, 3, vidasEnemigo, globales, 0, true);
				}
				break;
			case 2: //Juego				
				if(!juego.acabar()){
					juego.dibujar(g2d);
				}
				else if(!juego.despintarAcabado()){
					juego.dibujarNegro(g2d);
				}
				else {
					puntos = juego.getPuntos();
					vidasPinguino = juego.vidasPinguino();
					if(juego.vidasPinguino()!=0){
						if(nivel<maxNiveles){
							if( juego.getFinNivel() ){
								nivel++; 
								estado = 3;
							}
							else{
								vidasEnemigo = juego.getVidasEnemigo();
								juego = new Juego(tmp, nivel, vidasPinguino-1, 6, globales, puntos, false);
							}
						}
						else{ 
							estado = 4;	
							haGanado = true;
						}
					}
					else{			
						menuPrincipal.menosCreditos();
						if(monedas > 0){	
							estado = 1;
							nivel = 1;
							primera = false;
							seguir = false;
						}
						else{
							estado = 4;
							haGanado = false;
						}
					}
				}
				break;
			case 3: //Cambiar de nivel
				//ANIMACION DEL CAMBIO DE NIVEL
				vidasEnemigo = globales.MAX_NUM_ENEMIGOS;
				//Al cambiar de nivel los puntos vuelven a 0????
				juego = new Juego(tmp, nivel, vidasPinguino, vidasEnemigo, globales, puntos, false);
				estado = 2;
				break;
			case 4: //Fin
				if(!fin.isAcabado())
					fin.fin(g2d);
				else{
					if(haGanado){
						if(startPaintPoints){
							g2d.setColor(Color.BLACK);
							g2d.fillRect(0, 0, Pengo.ancho, Pengo.alto);
							mptos = new MenuPuntos(puntos, tmp);
							startPaintPoints = false;
						}
						if(!mptos.isAcabado())
							mptos.menu(g2d);
						else
							estado++;
					}
					else
						estado++;
				}
				break;
			default:
				break;
		}
	}
	
	public int getEstado(){
		return estado;
	}

	public void keyReleased(KeyEvent e){
		juego.keyReleased(e);
	}
		
	public void keyPressed(KeyEvent e){
		switch(estado){
			case 0:
				if(e.getKeyCode() == KeyEvent.VK_1) { estado++; limpiar=true; }
				if(e.getKeyCode() == KeyEvent.VK_5) { estado++; limpiar=true; }
				break;
			case 1: //Menu
				if(e.getKeyCode() == KeyEvent.VK_1) seguir = true;
				else if(menuPrincipal.pintado() && e.getKeyCode() == KeyEvent.VK_5) {
					monedas = menuPrincipal.masCreditos();
				}
				break;
			case 2: //Juego
				juego.keyPressed(e);
				break;
			default:
				break;
		}
	}

}
