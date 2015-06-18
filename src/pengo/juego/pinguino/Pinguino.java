package pengo.juego.pinguino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Temporizador;
import pengo.globales.Globales;
import pengo.juego.laberinto.Laberinto;

public class Pinguino extends JPanel{

	private static final long serialVersionUID = 1L;

	private Laberinto lab;
    private Temporizador tmp;
	private Globales globales;
	
	private String[] andar = new String[8];
	private String[] empujar = new String[8];
	private String[] ganar = new String[2];
	private String[] morir = new String[2];
	private int x, y, dx, dy, i=0, imgMorirPintadas, puntos;
	private int offsetY;
	private int duracion = 0;
	private int tecla_ant;
	private int romper = 0;
	private boolean muerto = false, pintarMorir = false;

	int movimiento = 0;
	
	private LinkedList<Integer> key = new LinkedList<Integer>();
	
	private Image imgActual;
	
	public Pinguino(Laberinto l, Temporizador temp, Globales globals, int puntos){
		globales = globals;
		tmp = temp;
		lab = l;
		this.puntos = puntos;
	}

	public void inicializar(){
		cargarImagenes();

		x = globales.COORD_X_INI_BLOQUES + 6*globales.ANCHO_IMG; // coordenada inicial x del pinguino
		y = globales.COORD_Y_INI_BLOQUES + 7*globales.ALTO_IMG; // coordenada inicial y del pinguino
		
		offsetY = globales.COORD_Y_INI_BLOQUES/globales.ALTO_IMG;
		
		imgActual = new ImageIcon(andar[0]).getImage();
		
	}
	
	private void cargarImagenes(){
		
		File f = new File("./img/pinguino_rojo");
		if(!f.exists()){
			System.err.println("No existe el directorio con las imagenes del pinguino");
			System.exit(0);
		}
		
		andar = new String[]{
				"./img/pinguino_rojo/andar/p_abajo_01.png",
				"./img/pinguino_rojo/andar/p_abajo_02.png",
				"./img/pinguino_rojo/andar/p_arriba_01.png",
				"./img/pinguino_rojo/andar/p_arriba_02.png",
				"./img/pinguino_rojo/andar/p_der_01.png",
				"./img/pinguino_rojo/andar/p_der_02.png",
				"./img/pinguino_rojo/andar/p_izq_01.png",
				"./img/pinguino_rojo/andar/p_izq_02.png"};
		
		empujar = new String[]{
				"./img/pinguino_rojo/empujar/p_emp_abajo_01.png",
				"./img/pinguino_rojo/empujar/p_emp_abajo_02.png",
				"./img/pinguino_rojo/empujar/p_emp_arriba_01.png",
				"./img/pinguino_rojo/empujar/p_emp_arriba_02.png",
				"./img/pinguino_rojo/empujar/p_emp_der_01.png",
				"./img/pinguino_rojo/empujar/p_emp_der_02.png",
				"./img/pinguino_rojo/empujar/p_emp_izq_01.png",
				"./img/pinguino_rojo/empujar/p_emp_izq_02.png"};

		ganar = new String[]{
				"./img/pinguino_rojo/ganar/p_ganar_01.png",
				"./img/pinguino_rojo/ganar/p_ganar_02.png"};

		morir = new String[]{
				"./img/pinguino_rojo/morir/p_morir_01.png",
				"./img/pinguino_rojo/morir/p_morir_02.png"};
	}

	/**
	 * Cambia las coordenadas del sprite, estas con "X" y "Y" y estos valores
	 * son usados por el método paint para dibujar el Sprite en pantalla.
	 */
	public void move() {
		if (dx > 0
				&& x <= globales.COORD_X_FIN_BLOQUES) {
				/*&& x <= Pengo.ancho - 24/* tamaño maximo pantalla eje x 
								- globales.ANCHO_IMG/* tamaño x del pinguino ) {*/
			x += dx;
		} else {
			if (dx < 0 && x > globales.COORD_X_INI_BLOQUES) {
				x += dx;
			}
			if (dy > 0
					&& y <= globales.COORD_Y_FIN_BLOQUES) {
					/*&& y <= Pengo.alto - 6/* tamaño maximo pantalla eje y 
									- globales.ALTO_IMG * 2/* tamaño y del pinguino ) {*/
				y += dy;
			} else {
				if (dy < 0 && y >= globales.COORD_Y_INI_BLOQUES) {
					y += dy;
				}
			}
		}
	}

	public int getX(int movX) {
		double div = (double)(x+movX)/(double)globales.ANCHO_IMG;
		if(div<0.0)
			return -1;
		else
			return (int)div;
	}

	public int getY(int movY) {
		return ((y+movY)/globales.ALTO_IMG)-offsetY;
	}

	public void keyPressed(KeyEvent e) {
		if(!muerto & e.getKeyCode()!=0) //Insertamos la tecla al final de la lista
			key.add(e.getKeyCode());
	}

	private void sigMovimiento(){
		int tecla = -1;
		try{
			tecla = key.element();
		}
		catch(Exception ex){}

		tecla_ant = tecla;
		
		//MOVER A LA DERECHA
		if (tecla == KeyEvent.VK_RIGHT) {			
			moverPinguino(globales.MOV_DER_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_DER, 1, 0);
		}
		//MOVER A LA IZQUIERDA
		else if (tecla == KeyEvent.VK_LEFT) {
			moverPinguino(globales.MOV_IZQ_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_IZQ, -1, 0);
		}
		else if (tecla == KeyEvent.VK_UP) {
			moverPinguino(0, globales.MOV_ARRIBA_SIG_BLOQUE, globales.IMG_INI_MOVER_ARRIBA, 0, -1);
		}
		else if (tecla == KeyEvent.VK_DOWN) {
			moverPinguino(0, globales.MOV_ABAJO_SIG_BLOQUE, globales.IMG_INI_MOVER_ABAJO, 0, 1);
		}
		
		else if (tecla == KeyEvent.VK_SPACE) {
			if(i==globales.IMG_INI_MOVER_ABAJO || i==(globales.IMG_INI_MOVER_ABAJO+1)){//abajo
				accionBloque(0, globales.MOV_ABAJO_SIG_BLOQUE, globales.IMG_INI_MOVER_ABAJO, 0, 1);
			}
			else if(i==globales.IMG_INI_MOVER_ARRIBA || i==(globales.IMG_INI_MOVER_ARRIBA+1)){//arriba
				accionBloque(0, globales.MOV_ARRIBA_SIG_BLOQUE, globales.IMG_INI_MOVER_ARRIBA, 0, -1);
			}
			else if(i==globales.IMG_INI_MOVER_DER || i==(globales.IMG_INI_MOVER_DER+1)){//derecha
				accionBloque(globales.MOV_DER_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_DER, 1, 0);
			}
			else {//izquierda
				accionBloque(globales.MOV_IZQ_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_IZQ, -1, 0);
			}
		}
		else if(tecla!=-1){
			key.remove();
		}
		
	}
	
	private void accionBloque(int movX, int movY, int img, int rX, int rY){
		if(lab.hayPared(getX(movX), getY(movY))){
			if(romper==0){ //Si es la primera vez que se entra
				lab.moverPared(getX(movX), getY(movY), rX, rY);
				duracion = tmp.vecesEjecutado;
				romper = 1;
			}
			accionBloque_tiempo(getX(movX), getY(movY), img);
		}
		else if(lab.hayBloque(getX(movX), getY(movY))){
			if(lab.hayPared(getX(movX)+rX, getY(movY)+rY)){

				if(romper==0){ //Si es la primera vez que se entra
					lab.romperBloque(getX(movX), getY(movY));
					duracion = tmp.vecesEjecutado;
					romper = 1;
				}
				accionBloque_tiempo(movX, movY, img);
			}
			else if(lab.hayBloque(getX(movX)+rX, getY(movY)+rY)){
				
				if(romper==0){ //Si es la primera vez que se entra
					lab.romperBloque(getX(movX), getY(movY));
					duracion = tmp.vecesEjecutado;
					romper = 1;
				}
				accionBloque_tiempo(movX, movY, img);
			}
			else{ //mover bloque
				lab.moverBloque(getX(movX), getY(movY), rX, rY);
				key.remove();
			}
		}
		else if(romper>=1 && romper<5){
			accionBloque_tiempo(movX, movY, img);
		}
		
		else{
			if(i==img)
				i++;
			else
				i--;
			imgActual = new ImageIcon(andar[i]).getImage();
			key.remove();
		}
	}
	
	public void accionBloque_tiempo(int movX, int movY, int img){
		if(i!=img && i!=(img+1))
			i=img;

		if(tmp.vecesEjecutado==(duracion+6)){ //Se cambia de imagen cada 6 frames
			if(i==img)
				i++;
			else
				i--;
			romper++;
			duracion = tmp.vecesEjecutado;
		}		
		if(romper==5){ //Cuando ya se han dibujado las 4 imagenes de romper
			romper = 0;
			duracion = 0;
			key.remove();
			sumarPuntos(30);
			imgActual = new ImageIcon(andar[i]).getImage();
		}
		else		
			imgActual = new ImageIcon(empujar[i]).getImage();
	}
	
	public void moverPinguino(int movX, int movY, int img, int dirX, int dirY){
		if(i!=img && i!=(img+1))
			i=img;
		
		if(lab.hayPared(getX(movX), getY(movY)) || lab.hayBloque(getX(movX), getY(movY)) ){
			dx = 0;
			dy = 0;
			moverEstatico(img);
		}
		else{
			andar(img, dirX, dirY);
		}
		imgActual = new ImageIcon(andar[i]).getImage();
	}
	
	private void moverEstatico(int img){
		if(duracion==0){
			duracion = tmp.vecesEjecutado;
		}
		else if(tmp.vecesEjecutado==(duracion+10)){
			duracion = 0;
						
			if(i==img)
				i++;
			else
				i--;			
		
			if(key.size()>1){
				int tecla = key.get(1);
				while(tecla==tecla_ant){
					key.remove();
					try{
						tecla = key.get(1);
					}
					catch(Exception ex){ tecla = -1; }
				}
			}
			
			key.remove();
		}		
	}
	
	private void andar(int img, int dirX, int dirY){
		if(dirX!=0)
			movimiento = globales.ANCHO_IMG-1;
		if(dirY!=0)
			movimiento = globales.ALTO_IMG-1;
		
		if(duracion==0){
			duracion = tmp.vecesEjecutado;
			dx = dirX;
			dy = dirY;
		}
		else if(tmp.vecesEjecutado!=(duracion+movimiento)){
			dx = dirX;
			dy = dirY;
		
			if(key.size()>1){
				int tecla = key.get(1);
				while(tecla==tecla_ant){
					key.remove();
					try{
						tecla = key.get(1);
					}
					catch(Exception ex){ tecla = -1; }
				}
			}
		}
		else if(tmp.vecesEjecutado==(duracion+movimiento)){
			dx = dirX;
			dy = dirY;
			duracion = 0;

			lab.setPosicionPinguino(getX(0)-dirX, getY(0)-dirY, getX(0), getY(0));
			
			if(i==img)
				i++;
			else
				i--;

			key.remove();
		}		
	}
	
	public void setMorir(){
		muerto = true;
		imgActual = new ImageIcon(morir[0]).getImage();
		i = 0;
		imgMorirPintadas = 1;
	}
	
	public boolean estaMuerto(){
		return muerto;
	}
	
	public boolean pintarMorir(){
		return pintarMorir;
	}
	
	public void keyReleased(KeyEvent e) {
		int keyRelease = e.getKeyCode();
		if (keyRelease == KeyEvent.VK_RIGHT)
			dx = 0;
		if (keyRelease == KeyEvent.VK_LEFT)
			dx = 0;
		if (keyRelease == KeyEvent.VK_UP)
			dy = 0;
		if (keyRelease == KeyEvent.VK_DOWN)
			dy = 0;
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, globales.ANCHO_IMG, globales.ALTO_IMG);
		
		sigMovimiento();
		move();
		dy = 0;
		dx = 0;
		g2d.drawImage(imgActual, x, y, this);
	}
	
	public void paintDead(Graphics2D g2d) {
		if((( tmp.vecesEjecutado * (tmp.frames_per_sec/10) )%tmp.frames_per_sec )==0){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, globales.ANCHO_IMG, globales.ALTO_IMG);
			
			g2d.drawImage(imgActual, x, y, this);
			
			if(i==0)
				i++;
			else
				i--;
			imgActual = new ImageIcon(morir[i]).getImage();
			
			if(imgMorirPintadas == 8){
				pintarMorir = true;
			}
			imgMorirPintadas++;
		}
	}

	public int getPuntos() {
		return puntos;
	}
	
	public void sumarPuntos(int suma){
		puntos+=suma;
	}
}