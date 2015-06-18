package pengo.juego; 

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Pengo;
import pengo.Temporizador;
import pengo.globales.Globales;
import pengo.juego.enemigos.Enemigo;
import pengo.juego.laberinto.Laberinto;
import pengo.juego.pinguino.Pinguino;
import pengo.sonidos.Sonidos;

public class Juego extends JPanel{

	private static final long serialVersionUID = 1L;

	private Temporizador tmp;
	private Globales globales;
	private Laberinto lab;
	private Pinguino pinguino;
	private Enemigo[] enemigos;
	public int enemigos_colocados, enemigos_muertos;
	
	private boolean labIni = false;
	private boolean acabar, fin_nivel=false;
	private boolean quitarVidaP = true;
	private boolean pintarAbajo = true;
	private boolean pintarArriba = true;
	private boolean despintarAcabado = false;
	private boolean nuevoNivel;
	private int nivel, vidasPinguino, vidasEnemigo, y = 0, ind_vidaEnemigo = 0, puntos;
	private long ini = 0;
	
	private String[] numeros_nivel;
	private String[] numeros_puntos;
	private String[] varios;
	private String[] vidaPinguino;
	private String[] vidaEnemigo;
	private String rutaEnemigo;
	private String player_1;
	
	public Juego(Temporizador tmp, int nivel, int vidasPing, int vidasEnem, Globales globals, int puntos, boolean nuevoNivel){	

		this.nuevoNivel = nuevoNivel;
		this.tmp = tmp;
		globales = globals;
		this.nivel = nivel;
		vidasPinguino = vidasPing;
		vidasEnemigo = vidasEnem;
		acabar = false;
		this.puntos = puntos;
		enemigos = new Enemigo[globales.MAX_NUM_ENEMIGOS_SIMULT];
		enemigos_colocados = 0;
		enemigos_muertos=0;
		switchEnemigo(nivel);
		iniciarImagenes();
		
		lab = new Laberinto(tmp, rutaEnemigo, globales);
		lab.inicializar();
		
		pinguino = new Pinguino(lab, tmp, globales, this.puntos);
		pinguino.inicializar();
		lab.setPinguino(pinguino);	
		
		/*---DESCOMENTAR ESTA LÍNEA PARA ACTIVAR SONIDOS DE FONDO---*/
//		Sonidos son = new Sonidos(); son.start();
	}
	
	private void switchEnemigo(int nivel){
		int modulo = nivel % 3;
		switch(modulo){
			case 1:
				rutaEnemigo = "./img/enemigo_verde/";
				break;
			case 2:
				rutaEnemigo = "./img/enemigo_rojo/";
				break;
			case 0:
				rutaEnemigo = "./img/enemigo_amarillo/";
				break;
			default:
				rutaEnemigo = "./img/enemigo_verde/";
				break;
		}
	}
	
	private void iniciarImagenes(){
		vidaPinguino = new String[]{
				"./img/vidas/vida_v_p.png",
				"./img/vidas/vida_m_p.png"};		
		vidaEnemigo = new String[]{
				"./img/vidas/vida_e_01.png",
				"./img/vidas/vida_e_02.png"};		
		varios = new String[]{
				"./img/varios/nivel.png",
				"./img/varios/p_nivel.png",
				"./img/varios/sega_1982.png",
				"./img/varios/player_1.png"};	
		player_1 = "./img/varios/player_1.png";
		numeros_nivel = new String[]{
				"./img/numeros/nivel/0.png",
				"./img/numeros/nivel/1.png",
				"./img/numeros/nivel/2.png",
				"./img/numeros/nivel/3.png",
				"./img/numeros/nivel/4.png",
				"./img/numeros/nivel/5.png",
				"./img/numeros/nivel/6.png",
				"./img/numeros/nivel/7.png",
				"./img/numeros/nivel/8.png",
				"./img/numeros/nivel/9.png"};
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
	
	public void dibujar(Graphics2D g2d){
		if(!fin_nivel){
			int puntos_aux = pinguino.getPuntos();
			if(puntos_aux > puntos){
				puntos = puntos_aux;
				dibujarPuntos(g2d);
			}
			
			if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/15) )%tmp.frames_per_sec )==0){
				//BUCLE A TRAVES DE LAS VIDAS DEL ENEMIGO
				for(int i=0; i<vidasEnemigo; i++){
					g2d.drawImage(new ImageIcon(vidaEnemigo[ind_vidaEnemigo]).getImage(), 
							globales.COORD_X_VIDAS_ENEM - (globales.ANCHO_IMG_VIDAS_ENEM*i), globales.COORD_Y_VIDAS, this);		
				}
				if(ind_vidaEnemigo%2 == 0)
					ind_vidaEnemigo++;
				else
					ind_vidaEnemigo--;
			}
			
			if(pintarAbajo){
				dibujarAbajo(g2d);
			}
			
			if(!lab.laberintoIni()){ 
				lab.paintFull(g2d); 
			}
			else if(pintarArriba && lab.laberintoIni()){
				dibujarArriba(g2d);
			}
			else if(!lab.recorridoCompleto()){
				lab.paintRecorrido(g2d);
				labIni = lab.recorridoCompleto();
			}
			
			if(!pinguino.estaMuerto()){
				if(labIni){
					pinguino.paint(g2d);
					if(nuevoNivel)
						eliminarVidaPinguino(g2d);
					
					for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
						if(enemigos[ind]!=null){
							if(enemigos[ind].getAparecido()){
								if(enemigos[ind].getMareado()){
									enemigos[ind].paintMareado(g2d);
								}else if (enemigos[ind].estaMuerto()){
									enemigos[ind]=null; 
									enemigos_muertos++;
								}else {
									enemigos[ind].paint(g2d);
								}
							}else 	enemigos[ind].paintAparecer(g2d);						
						}
						else if(vidasEnemigo>0){
							if(lab.cambiosMoverBloqueConEnemigo.size()==0){
								if(ind%2==0){
									int[] nacer = lab.getPosicionAleatoria();
									enemigos[ind] = new Enemigo(lab, tmp, rutaEnemigo, true, 0, 1, globales, nacer[0], nacer[1]);
									/*orden de parámetros: Lab, temporizador, ruta imagenes, rompe?, id, tipo_IA, globales, X, Y)*/
								}else{
									int[] nacer = lab.getPosicionAleatoria();
									enemigos[ind] = new Enemigo(lab, tmp, rutaEnemigo, true, 1, 2, globales, nacer[0], nacer[1]);
								}
								vidasEnemigo--;
								g2d.setColor(Color.BLACK);
								g2d.fillRect(globales.COORD_X_VIDAS_ENEM - (globales.ANCHO_IMG_VIDAS_ENEM*vidasEnemigo), globales.COORD_Y_VIDAS, 
											globales.ANCHO_IMG_VIDAS_ENEM, globales.ALTO_IMG);
								lab.setEnemigo(ind, enemigos[ind]);
								lab.aumentarNumeroEnemigos();
								enemigos[ind].inicializar();
								enemigos_colocados++;
							}else {
								ini++;
								if(ini==500){
									ini=0;
									lab.cambiosMoverBloqueConEnemigo.remove(0);
								}
							}
						}
						if(enemigos_muertos == enemigos_colocados && vidasEnemigo==0){
							//fin de la partida aqui
							if(lab.cambiosMoverBloqueConEnemigo.size()==0){
								acabar=true; fin_nivel=true;
								despintarAcabado=false;
							}else{
								ini++;
								if(ini==250){
									ini=0;
									lab.cambiosMoverBloqueConEnemigo.remove(0);
									acabar=true; fin_nivel=true;
									despintarAcabado=false;
								}
							}
						}
					}					
					if(lab.dibujarNuevosEnemigos())	lab.paintNuevosEnemigos(g2d);
				}
			}
			else{
				pintarMorir(g2d);
			}

			if(lab.haveToChange()){lab.paintChange(g2d);}
			
			acabar = pinguino.pintarMorir() || fin_nivel;
		}				
	}

	public void dibujarNegro(Graphics2D g2d){
		if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/3) )%tmp.frames_per_sec )==0){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, y, Pengo.ancho, globales.ALTO_IMG);
			if(y == globales.ALTO_IMG*2 || y == globales.COORD_Y_FIN_BLOQUES)
				y += globales.ANCHO_BORDE;
			else
				y += globales.ALTO_IMG;
			
			despintarAcabado = (y >= (globales.COORD_Y_FIN_BLOQUES + globales.ALTO_IMG));
		}
	}
	
	public void dibujarPuntos(Graphics2D g2d){
		//PINTAR PUNTOS
		String ptos = String.valueOf(puntos);
		int num = 0;
		for(int i=ptos.length()-1; i>=0; i--){
			num = Integer.parseInt(String.valueOf(ptos.charAt(i)));

			g2d.setColor(Color.BLACK);
			g2d.fillRect(globales.COORD_X_FIN_PUNTOS - (ptos.length()-i)*globales.ANCHO_IMG_NUM_PTOS, globales.COORD_Y_INI_PUNTOS, 
						globales.ANCHO_IMG_NUM_PTOS, globales.ALTO_IMG_NUM_PTOS);
			
			g2d.drawImage(new ImageIcon(numeros_puntos[num]).getImage(), 
					globales.COORD_X_FIN_PUNTOS - (ptos.length()-i)*globales.ANCHO_IMG_NUM_PTOS, globales.COORD_Y_INI_PUNTOS, this);		
		}
	}
	
	public void dibujarAbajo(Graphics2D g2d){
		//Dibujar cosas de abajo
		g2d.drawImage(new ImageIcon(varios[0]).getImage(), globales.COORD_X_ACT, globales.COORD_Y_VARIOS, this);
		g2d.drawImage(new ImageIcon(varios[2]).getImage(), globales.COORD_X_LOGO, globales.COORD_Y_VARIOS, this);
		if(nivel<10){
			g2d.drawImage(new ImageIcon(varios[1]).getImage(), 
					globales.COORD_X_PNIVEL - globales.ANCHO_IMG, globales.COORD_Y_VARIOS, this);
			g2d.drawImage(new ImageIcon(numeros_nivel[nivel]).getImage(), globales.COORD_X_NUM_NIVEL, globales.COORD_Y_VARIOS, this);
		}
		else{
			g2d.drawImage(new ImageIcon(varios[1]).getImage(), globales.COORD_X_PNIVEL, globales.COORD_Y_VARIOS, this);
			g2d.drawImage(new ImageIcon(numeros_nivel[1]).getImage(), globales.COORD_X_NUM_NIVEL, globales.COORD_Y_VARIOS, this);
			g2d.drawImage(new ImageIcon(numeros_nivel[0]).getImage(), 
					globales.COORD_X_NUM_NIVEL + globales.ANCHO_IMG, globales.COORD_Y_VARIOS, this);
		}
		pintarAbajo = false;
	}
	
	public void dibujarArriba(Graphics2D g2d){
		//BUCLE A TRAVES DE LAS VIDAS QUE POSEE EL PINGUINO
		for(int i=0; i<vidasPinguino; i++){
			g2d.drawImage(new ImageIcon(vidaPinguino[0]).getImage(), 
					globales.COORD_X_VIDAS_P+(globales.ANCHO_IMG*i), globales.COORD_Y_VIDAS, this);				
		}
		
		//Dibujar icono de jugador 1
		g2d.drawImage(new ImageIcon(player_1).getImage(), 
				globales.COORD_X_INI_BLOQUES, globales.COORD_Y_INI_PUNTOS, this);
		dibujarPuntos(g2d);
		
		pintarArriba = false;
	}
	
	private void eliminarVidaPinguino(Graphics2D g2d) {
		if(quitarVidaP){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(globales.COORD_X_VIDAS_P+(globales.ANCHO_IMG*(vidasPinguino-1)), globales.COORD_Y_VIDAS, 
						globales.ANCHO_IMG, globales.ALTO_IMG);
			vidasPinguino--;
			quitarVidaP = false;
		}
	}
	
	private void pintarMorir(Graphics2D g2d){
		//BUCLE A TRAVES DE LAS VIDAS QUE POSEE EL PINGUINO
		for(int i=0; i<vidasPinguino; i++){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(globales.COORD_X_VIDAS_P+(globales.ANCHO_IMG*(vidasPinguino-1)), globales.COORD_Y_VIDAS, 
						globales.ANCHO_IMG, globales.ALTO_IMG);
			g2d.drawImage(new ImageIcon(vidaPinguino[0]).getImage(), 
					globales.COORD_X_VIDAS_P+(globales.ANCHO_IMG*i), globales.COORD_Y_VIDAS, this);				
		}
		pinguino.paintDead(g2d);
	}
	
	public boolean acabar(){
		return acabar;
	}
	
	public boolean getFinNivel(){
		return fin_nivel;
	}
	
	public int vidasPinguino(){
		return vidasPinguino;
	}
	
	public boolean despintarAcabado(){
		return despintarAcabado;
	}
	
	public void keyReleased(KeyEvent e){
		pinguino.keyReleased(e);
	}
		
	public void keyPressed(KeyEvent e){
		pinguino.keyPressed(e);
	}

	public int getVidasEnemigo(){
		return vidasEnemigo + lab.getEnemigosVivos();
	}

	public int getPuntos() {
		return puntos;
	}

	public void liberar() {		
		pinguino=null;
		for(int i=0;i<globales.MAX_NUM_ENEMIGOS_SIMULT;i++){
			if(enemigos[i]!=null) enemigos[i].liberar();
			enemigos[i]=null;
		}
		enemigos=null;
		
	}

}

