package pengo.juego.enemigos;

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

public class Enemigo extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private Laberinto lab;
    private Temporizador tmp;
	private Image imgActual;
	private Globales globales;
	private LinkedList<Integer> key = new LinkedList<Integer>();
	Controlador cont = null;
	Controlador_2 cont2 = null;
	
	private String ruta_img;
	private String[] andar = new String[8];
	private String[] empujar = new String[8];
	private String[] morir = new String[8];
	private String[] marear = new String[2];
	private String[] aparecer = new String[6];
	private String[] varios = new String[8];
	
	private boolean puede_romper, muerto, mareado, aparecido=false, 
		pintarMorir = false, pintarMareado=false, pintarAparecido=false;
	
	private int id, x, y, dx, dy, i=0, imgMorirPintadas, imgMarearPintadas, imgAparecerPintadas=0;
	private int offsetY, tecla_ant, IA, movimiento = 0;
	private int duracion = 0,romper = 0, init_X, init_Y;

	public boolean verbose=false;

	public Enemigo(Laberinto l, Temporizador temp, String ruta, boolean rompe, int id, 
			int tipo_IA, Globales globals, int X, int Y){
		tmp = temp;
		lab = l;
		ruta_img=ruta;
		puede_romper=rompe;
		this.id=id; //rango [0,2]
		IA=tipo_IA;
		globales = globals;
		muerto = false; mareado=false;
		init_X=X; init_Y=Y;
	}
	
	public void inicializar(){
		cargarImagenes();
		x = globales.COORD_X_INI_BLOQUES + init_X*globales.ANCHO_IMG; // coordenada inicial x del enemigo
		y = globales.COORD_Y_INI_BLOQUES + init_Y*globales.ALTO_IMG; // coordenada inicial y del enemigo
		offsetY = globales.COORD_Y_INI_BLOQUES/globales.ALTO_IMG;
		imgActual = new ImageIcon(andar[0]).getImage();
		
		if(IA==1){
			cont = new Controlador(this);
			cont.start();
		}else{
			cont2 = new Controlador_2(this); 
			cont2.start();
		}
	}
	
	private void cargarImagenes(){
		File f = new File(ruta_img);
		if(!f.exists()){
			System.exit(0);
		}
		File[] ficheros = f.listFiles();
		for (int x=0; x<ficheros.length; x++){
			File[] img = new File(ruta_img+ficheros[x].getName()).listFiles();
			switch(ficheros[x].getName()){
				case "andar": 
					for (int i=0; i<8; i++){
						andar[i] = ruta_img+ficheros[x].getName() + "/" + img[i].getName();
					}
					break;
				case "empujar":
					for (int i=0; i<8; i++){
						empujar[i] = ruta_img+ficheros[x].getName() + "/" + img[i].getName();
					}
					break;
				case "morir":
					for (int i=0; i<8; i++){
						morir[i] = ruta_img+ficheros[x].getName() + "/" + img[i].getName();
					}
					marear[0] = ruta_img+ficheros[x].getName() + "/" + img[8].getName();
					marear[1] = ruta_img+ficheros[x].getName() + "/" + img[9].getName();
					break;
				case "cambio_nivel":
					for (int i=0; i<6; i++){
						aparecer[i] = ruta_img+ficheros[x].getName() + "/" + img[i].getName();
					}
					break;
				/*case "varios":
					for (int i=0; i<8; i++){
						varios[i] = ruta_img+ficheros[x].getName() + "/" + img[i].getName();
					}
					break;*/
			}
		}
	}

	/*-------GETTERS/SETTERS-------*/
	public void resetKeyList(){
		key = new LinkedList<Integer>();
	}
	
	public boolean keyListIsEmpty(){
		return key.isEmpty();
	}
	
	public int getKeySize(){
		return key.size();
	}
	
	public int getId(){
		return id;
	}
	
	public void setMareado(boolean b){
		mareado=b;
		if(b){
			duracion = 0;
			imgMarearPintadas = 1;
			imgActual = new ImageIcon(marear[0]).getImage();
		}
		else{
			mareado=false;
		}
	}
	
	public boolean getMareado(){
		return mareado;
	}
	
	public void setMorir(){
		muerto = true;
	}
	
	public boolean estaMuerto(){
		return muerto;
	}
	
	public boolean getAparecido(){
		return aparecido;
	}

	private void setAparecido(boolean b) {
		aparecido=b;		
	}
	
	public int getX(int movX){
		double div = (x+movX)/(double)globales.ANCHO_IMG;
		if(div<0.0)
			return -1;
		else
			return (int)div;
	}
	
	public int getY(int movY){
		return ((y+movY)/globales.ALTO_IMG)-offsetY;
	}
	
	public Laberinto getLab(){
		return lab;
	}
	
	public boolean puedeRomper(){
		return puede_romper;
	}
	
	public int getTipoIA(){
		return IA; 
	}
	
	public Image getImgActual(){
		return imgActual;
	}
	
	public Image getImgMorir(int i){
		return new ImageIcon(morir[i]).getImage();
	}
	
	/*-------MÉTODOS DE MOVIMIENTO/ACCIÓN-------*/
	/**
	 * Cambia las coordenadas del sprite, estas con "X" y "Y" y estos valores
	 * son usados por el método paint para dibujar el Sprite en pantalla.
	 */
	public void move() {
		if (dx > 0 && x <= globales.COORD_X_FIN_BLOQUES) {
			x += dx;
		} else {
			if (dx < 0 && x > globales.COORD_X_INI_BLOQUES) {
				x += dx;
			}
			if (dy > 0 && y <= globales.COORD_Y_FIN_BLOQUES) {
				y += dy;
			} else {
				if (dy < 0 && y >= globales.COORD_Y_INI_BLOQUES) {
					y += dy;
				}
			}
		}
	}	
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()!=0) {//Insertamos la tecla al final de la lista
			key.add(e.getKeyCode());
		}
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
	
	private void sigMovimiento(){
		int tecla = -1;
		try{
			tecla = key.element();
		}
		catch(Exception ex){}
		tecla_ant = tecla;
		
		//MOVER A LA DERECHA
		if (tecla == KeyEvent.VK_RIGHT) {			
			moverEnemigo(globales.MOV_DER_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_DER, 1, 0);
		}
		//MOVER A LA IZQUIERDA
		else if (tecla == KeyEvent.VK_LEFT) {
			moverEnemigo(globales.MOV_IZQ_SIG_BLOQUE, 0, globales.IMG_INI_MOVER_IZQ, -1, 0);
		}
		else if (tecla == KeyEvent.VK_UP) {
			moverEnemigo(0, globales.MOV_ARRIBA_SIG_BLOQUE, globales.IMG_INI_MOVER_ARRIBA, 0, -1);
		}
		else if (tecla == KeyEvent.VK_DOWN) {
			moverEnemigo(0, globales.MOV_ABAJO_SIG_BLOQUE, globales.IMG_INI_MOVER_ABAJO, 0, 1);
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
		if(lab.hayPared(getX(movX), getY(movY))){}
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
				if(romper==0){ 
					lab.romperBloque(getX(movX), getY(movY));
					duracion = tmp.vecesEjecutado;
					romper = 1;
				}
				accionBloque_tiempo(movX, movY, img);
			}
			else{ //mover bloque
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
			imgActual = new ImageIcon(andar[i]).getImage();
		}
		else		
			imgActual = new ImageIcon(empujar[i]).getImage();
	}
	
	public void moverEnemigo(int movX, int movY, int img, int dirX, int dirY){
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
					try{ tecla = key.get(1);}
					catch(Exception ex){ tecla = -1; }
				}
			}
		}
		else if(tmp.vecesEjecutado==(duracion+movimiento)){
			dx = dirX;
			dy = dirY;
			duracion = 0;
			lab.setPosicionEnemigo(id, getX(0)-dirX, getY(0)-dirY, getX(0), getY(0));
			if(i==img)
				i++;
			else
				i--;
			key.remove();
		}		
	}
		
	/*-------MÉTODOS DE DIBUJAR-------*/
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
			
			if(i==0)
				i++;
			else
				i--;
			
			g2d.drawImage(imgActual, x, y, this);
			
			if(imgMorirPintadas == 8){
				pintarMorir = true;
			}
			imgMorirPintadas++;
		}
	}
	
	public void paintMareado(Graphics2D g2d) {
		if((( tmp.vecesEjecutado * (tmp.frames_per_sec/10) )%tmp.frames_per_sec )==0){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, globales.ANCHO_IMG, globales.ALTO_IMG);
			
			g2d.drawImage(imgActual, x, y, this);
			
			//if(imgMarearPintadas%4==0){
				if(i==0) i=1;
				else i=0;
				imgActual = new ImageIcon(marear[i]).getImage();
			//}
			
			if(imgMarearPintadas == 32){
				pintarMareado = true;
				this.setMareado(false);
			}
			imgMarearPintadas++;
		}
	}
	
	public void paintAparecer(Graphics2D g2d) {
		if((( tmp.vecesEjecutado * (tmp.frames_per_sec/10) )%tmp.frames_per_sec )==0){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, globales.ANCHO_IMG, globales.ALTO_IMG);
			
			g2d.drawImage(imgActual, x, y, this);
			
			if(imgAparecerPintadas%2==1){
				if(i==5){ imgActual = new ImageIcon(andar[0]).getImage(); }
				else{ i++; imgActual = new ImageIcon(aparecer[i]).getImage(); }
				
			}
			
			if(imgAparecerPintadas == 15){
				pintarAparecido = true;
				this.setAparecido(true);
			}
			imgAparecerPintadas++;
		}
	}
	
	public void liberar(){
		imgActual=null;
		if(IA==1){cont = null;}
		else{cont2 = null;}
	}
	
}
