package pengo.juego.laberinto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Temporizador;
import pengo.globales.Globales;
import pengo.juego.MoverBloque;
import pengo.juego.MoverBloqueConEnemigo;
import pengo.juego.RomperBloque;
import pengo.juego.enemigos.Enemigo;
import pengo.juego.pinguino.Pinguino;

public class Laberinto extends JPanel{

	private static final long serialVersionUID = 1L;
	
    private Temporizador tmp;
	private Globales globales;

	private String rutaEnemigo;
	
	private String bloqueAzul;
	private String bloqueEnemigo;
	private String[] bloqueRombo;
	private String[] romperBloque;
	private String[] borde;
	private String borde_iniciar;

	//Indica si se ha pintado el recorrido completo y si se han dibujado los cuadros que indican nuevos enemigos
	private boolean recorridoCompleto = false, dibujarNuevosEnemigos = false;
	
	//Inidca el borde que hay que pintar y cuantos se han pintado
	private int posx_bloque, posy_bloque, indBorde, movBorde = 0;
	//Inidica que hay que pintar el movimiento de la pared
	private int moverPared = 0;
	//Indica si se ha pintado el laberinto por primera vez
	private boolean iniciado = false;
	//Indica el numero de bloques que se han quitado al formar el recorrido
	private int bloquesQuitados = 0;
    private int bloquesAQuitar;
    
	private int fila;//indica que fila pintar al inicializar el tablero
	private Integer[][] lab;
	
	//Lista de celdas que forman el camino del laberinto
	private Integer[] recorridoX;
	private Integer[] recorridoY;
	//Listas de las posiciones de nuevos enemigos a crear
	private Integer[] nuevoEnemigoX;
	private Integer[] nuevoEnemigoY;
	private int numAvisoNuevosEnemigos = 0;
	//Listas de las posiciones de los enemigos
	
	private ArrayList<RomperBloque> cambiosRomperBloque;
	private ArrayList<MoverBloque> cambiosMoverBloque;
	public ArrayList<MoverBloqueConEnemigo> cambiosMoverBloqueConEnemigo;
	
	private int[] posicion_pinguino;
	private int[][] posicion_enemigos, posicion_enemigos_coord;

	private Pinguino pingu;
	private Enemigo[] enemigos;
	private int enemigos_vivos;
	
	public Laberinto(Temporizador temp, String rutaEnem, Globales globals){
		globales = globals;
		tmp = temp;
		fila = globales.ALTO_LAB; 
		lab = new Integer[globales.ANCHO_LAB][globales.ALTO_LAB];
		int min = 95;
		int max = 115;
		bloquesAQuitar = min + (int)(Math.random() * ((max - min) + 1));
		recorridoX = new Integer[bloquesAQuitar];
		recorridoY = new Integer[bloquesAQuitar];
		rutaEnemigo = rutaEnem;
		cambiosRomperBloque = new ArrayList<RomperBloque>();
		cambiosMoverBloque = new ArrayList<MoverBloque>();
		cambiosMoverBloqueConEnemigo = new ArrayList<MoverBloqueConEnemigo>();
		posicion_pinguino=new int[2];
		posicion_enemigos=new int[globales.MAX_NUM_ENEMIGOS_SIMULT][2];
		posicion_enemigos_coord=new int[globales.MAX_NUM_ENEMIGOS_SIMULT][2];
		enemigos=new Enemigo[globales.MAX_NUM_ENEMIGOS_SIMULT];
		enemigos_vivos=0;
	}
		
	public void inicializar() {
		cargarImagenes();
		
		for(int j = 0; j<globales.ANCHO_LAB; j++) {
			for(int i = 0; i<globales.ALTO_LAB; i++) {
				lab[j][i] = globales.LAB_BLOQUE_AZUL;
			}
		}        
		
		crearRecorrido();
        
		for(int j = 0; j<recorridoX.length-1; j++) {
			lab[recorridoX[j]][recorridoY[j]] = globales.LAB_BLOQUE_AZUL;
		}
        
		lab[6][7] = globales.LAB_PINGUINO;
		posicion_pinguino[0] = 6;
		posicion_pinguino[1] = 7;
	}
	
	private void cargarImagenes(){
		String ruta = "./img/laberinto";
		File f = new File(ruta);
		if(!f.exists()){
			System.exit(0);
		}
		
		bloqueEnemigo = rutaEnemigo + "bloque.png";
		bloqueAzul = "./img/laberinto/bloque_azul.png";

		romperBloque = new String[]{
				"./img/laberinto/romper/romper_01.png",
				"./img/laberinto/romper/romper_02.png",
				"./img/laberinto/romper/romper_03.png",
				"./img/laberinto/romper/romper_04.png",
				"./img/laberinto/romper/romper_05.png",
				"./img/laberinto/romper/romper_06.png",
				"./img/laberinto/romper/romper_07.png"};

		bloqueRombo = new String[]{
				"./img/laberinto/rombo/bloque_rombo_01.png",
				"./img/laberinto/rombo/bloque_rombo_02.png"};

		borde_iniciar = "./img/laberinto/borde/borde_lat_iniciar.png";
		
		borde = new String[]{
				"./img/laberinto/borde/borde_sup.png",
				"./img/laberinto/borde/borde_lat.png",
				"./img/laberinto/borde/borde_sup_mov_01.png",
				"./img/laberinto/borde/borde_sup_mov_02.png",
				"./img/laberinto/borde/borde_lat_mov_01.png",
				"./img/laberinto/borde/borde_lat_mov_02.png"};
	}
	
	public Image getImage(int x, int y) {
		if(lab[x][y] == globales.LAB_BLOQUE_AZUL){
			return new ImageIcon(bloqueAzul).getImage();
		}
		else if(lab[x][y] == globales.LAB_DIAMANTE){
			return new ImageIcon(bloqueRombo[0]).getImage();
		}
		else{
			return null;
		}
	}
	
	private Image getImageByNumber(int cual){
		if(cual == globales.LAB_BLOQUE_AZUL){
			return new ImageIcon(bloqueAzul).getImage();
		}
		else if(cual == globales.LAB_DIAMANTE){
			return new ImageIcon(bloqueRombo[0]).getImage();
		}
		else
			return null;
	}
	
	private void crearRecorrido(){
		
		int x, y;
        
        int[] xi = {0, 0, globales.ANCHO_LAB-1, globales.ANCHO_LAB-1};
        int[] yi = {globales.ALTO_LAB - 1, 0, globales.ALTO_LAB - 1, globales.ANCHO_LAB-1};
         
        int camino1 = (int)(Math.random() * bloquesAQuitar/2);
        int camino2 = bloquesAQuitar/2 - camino1;
        int camino3 = (int)(Math.random() * bloquesAQuitar/2);
        int camino4 = bloquesAQuitar/2 - camino3;
         
        int[] camino = {camino1, camino2, camino3, camino4};
         
        for(int i=0; i<4; i++){
            x = xi[i]; 
            y = yi[i]; 
            lab[x][y] = globales.LAB_VACIO;
            while(camino[i]>0){
                int direccion = (int)(Math.random() * 4);
                switch(direccion){
                case 0: //izq
                    if(!hayPared(x-1,y) && lab[x-1][y]!=globales.LAB_VACIO){
                        recorridoX[bloquesQuitados] = x-1;
                        recorridoY[bloquesQuitados] = y;
                        lab[x-1][y]=globales.LAB_VACIO; x--; camino[i]--;
                        bloquesQuitados++;
                    } else { x=(x+1)%13; y=(y+1)%15; }
                    break;
                case 1: //der
                    if(!hayPared(x+1,y) && lab[x+1][y]!=globales.LAB_VACIO){
                        recorridoX[bloquesQuitados] = x+1;
                        recorridoY[bloquesQuitados] = y;
                        lab[x+1][y]=globales.LAB_VACIO; x++; camino[i]--;
                        bloquesQuitados++;
                    } else { x=(x+1)%13; y=(y+1)%15; }
                    break;
                case 2: //arriba
                    if(!hayPared(x,y-1) && lab[x][y-1]!=globales.LAB_VACIO){
                        recorridoX[bloquesQuitados] = x;
                        recorridoY[bloquesQuitados] = y-1;
                        lab[x][y-1]=globales.LAB_VACIO; y--; camino[i]--;
                        bloquesQuitados++;
                    } else { x=(x+1)%13; y=(y+1)%15; }
                    break;
                case 3: //abajo
                    if(!hayPared(x,y+1) && lab[x][y+1]!=globales.LAB_VACIO){
                        recorridoX[bloquesQuitados] = x;
                        recorridoY[bloquesQuitados] = y+1;
                        lab[x][y+1]=globales.LAB_VACIO;y++; camino[i]--;
                        bloquesQuitados++;
                    } else { x=(x+1)%13; y=(y+1)%15; }
                    break;
                }
            }
        }
        bloquesQuitados = 0;
	}
	
	public void aumentarNumeroEnemigos(){
		enemigos_vivos++;
	}
	
	public boolean hayBloque(int x, int y) {
		return lab[x][y]!=globales.LAB_VACIO && lab[x][y]<globales.LAB_PINGUINO;
	}
	
	public boolean hayPared(int x, int y) {
		if(x>=globales.ANCHO_LAB || x<0 || y<0 || y>=globales.ALTO_LAB)
			return true;
		else
			return false;
	}
	
	public boolean hayEnemigo(int x, int y, int id) {
		return posicion_enemigos[id][0] == x && posicion_enemigos[id][1] == y;
	}
	
	public void setPosicionPinguino(int oldx, int oldy, int newx, int newy){
		posicion_pinguino[0]=newx; posicion_pinguino[1]=newy;
		lab[oldx][oldy] = globales.LAB_VACIO;
		lab[newx][newy] = globales.LAB_PINGUINO;
		for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
			int[] enem = getPosicionEnemigo(ind);
			//detectar si algun enemigo está debajo
			if(enemigos[ind]!=null && enemigos[ind].getMareado() && 
					(enem[0]==newx && enem[1]==newy)){
				enemigos[ind].setMorir();
				pingu.sumarPuntos(globales.PUNTOS_MATAR_ENEMIGO_MAREO);
			}
		}
	}
	
	public void setPosicionEnemigo(int id, int oldx, int oldy, int newx, int newy){
		posicion_enemigos[id][0]=newx; 
		posicion_enemigos[id][1]=newy;
		posicion_enemigos_coord[id][0]=(newx*globales.ANCHO_IMG)+globales.COORD_X_INI_BLOQUES; 
		posicion_enemigos_coord[id][1]=(newy*globales.ALTO_IMG)+globales.COORD_Y_INI_BLOQUES;
		lab[oldx][oldy] = globales.LAB_VACIO;
		if(newx!=-1) lab[newx][newy] = globales.LAB_ENEMIGO;
		int[] pos_pengo = getPosicionPinguino();
		if(pos_pengo[0]==newx && pos_pengo[1]==newy) pingu.setMorir();
	}
	
	public int[] getPosicionEnemigo(int id){
		return posicion_enemigos[id];
	}
	
	public int[] getPosicionExactaEnemigo(int id){
		return posicion_enemigos_coord[id];
	}
	
	public int[] getPosicionPinguino(){
		return posicion_pinguino;
	}
	
	public void moverPared(int x, int y, int dirX, int dirY){
		
		if(dirY!=0){//Mover borde inferior o superior
			moverPared = 1;
			indBorde = 2;
			if(y==-1){ //mover borde superior
				posx_bloque = globales.COORD_X_BORDE_SUP_INF_IZQ;
				posy_bloque = globales.COORD_Y_BORDE_SUP;
			}
			else if(y==globales.ALTO_LAB){ //mover borde inferior
				posx_bloque = globales.COORD_X_BORDE_SUP_INF_IZQ;
				posy_bloque = globales.COORD_Y_BORDE_INF;
			}
			for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
				//detectar si algun enemigo está pegado a las paredes HORIZONTALES
				if(enemigos[ind]!=null){
					if ((y==-1 && posicion_enemigos[ind][1]==0) || 
						(y==globales.ALTO_LAB && posicion_enemigos[ind][1]==globales.ALTO_LAB-1)){
						enemigos[ind].setMareado(true);
					}
				}
			}
		}
		else{//Mover borde lateral derecho o izquierdo
			moverPared = 1;
			indBorde = 4;
			if(x<0){ //mover borde lateral izquierdo
				posx_bloque = globales.COORD_X_BORDE_SUP_INF_IZQ;
				posy_bloque = globales.COORD_Y_BORDE_IZQ_DER;
			}
			else if(x>=globales.ANCHO_LAB){ //mover borde lateral derecho
				posx_bloque = globales.COORD_X_BORDE_DER;
				posy_bloque = globales.COORD_Y_BORDE_IZQ_DER;
			}
			//detectar si algun enemigo está a 1 bloque de distancia
			for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
				//detectar si algun enemigo está pegado a las paredes VERTICALES
				if(enemigos[ind]!=null){
					if ((x<0 && posicion_enemigos[ind][0]==0) || 
						(x>=globales.ANCHO_LAB && posicion_enemigos[ind][0]==x-1)){
						enemigos[ind].setMareado(true);
					}
				}
			}//fin for
		}
			
	}
	
	public void romperBloque(int x, int y) {
		lab[x][y] = globales.LAB_VACIO;
		cambiosRomperBloque.add(new RomperBloque(x, y, 0));
	}
	
	public void moverBloque(int x, int y, int dirX, int dirY){
		int bloqueAnterior = lab[x][y];
		lab[x][y] = globales.LAB_VACIO;
		int i = x;
		int j = y;
		int fin = 0;
		int bloquesMov = -1;
		//detectar aqui si hay enemigo en el camino
		do{
			bloquesMov++;
			i += dirX;
			j += dirY;
			//parar controlador e incrementar coordenadas de enemigo
			if(!hayPared(i,j)){
				fin = lab[i][j];
				if (fin==5) fin=0;
			}
			else fin = 1;
			
		}while(fin==0);
		int posx = globales.COORD_X_INI_BLOQUES + x*globales.ANCHO_IMG;
		int posy = globales.COORD_Y_INI_BLOQUES + y*globales.ALTO_IMG;
			cambiosMoverBloque.add(new MoverBloque(bloqueAnterior, 
					posx, posy, (posx-globales.COORD_X_INI_BLOQUES)/globales.ANCHO_IMG,(posy-globales.COORD_Y_INI_BLOQUES)/globales.ALTO_IMG,
					posx + dirX*bloquesMov*globales.ANCHO_IMG, posy + dirY*bloquesMov*globales.ALTO_IMG, 
					i - dirX, j - dirY, dirX, dirY));
	}
		
	public void paintFull(Graphics2D g2d){
		
		if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/3) )%tmp.frames_per_sec )==0){
			int posy = globales.COORD_Y_INI_BLOQUES + (fila-1)*globales.ALTO_IMG;
		
			//bordes iniciales laterales
			g2d.drawImage(new ImageIcon(borde_iniciar).getImage(), globales.COORD_X_BORDE_SUP_INF_IZQ, posy, this);
			g2d.drawImage(new ImageIcon(borde_iniciar).getImage(), globales.COORD_X_BORDE_DER, posy, this);
				
			//borde inferior
			if(fila==globales.ALTO_LAB)
				g2d.drawImage(new ImageIcon(borde[0]).getImage(), globales.COORD_X_BORDE_SUP_INF_IZQ, globales.COORD_Y_BORDE_INF, this);
			
			for(int i = 0; i<globales.ANCHO_LAB; i++){
				g2d.drawImage(this.getImage(i,fila-1), globales.COORD_X_INI_BLOQUES + i*globales.ANCHO_IMG, posy, this);
			}
				
			iniciado = (fila==1);
			fila--;		
				
			if(iniciado){
				//Borde superior
				g2d.drawImage(new ImageIcon(borde[0]).getImage(), globales.COORD_X_BORDE_SUP_INF_IZQ, globales.COORD_Y_BORDE_SUP, this);
			}
		}
	}
	
	public void paintRecorrido(Graphics2D g2d){
		if(bloquesQuitados!=(recorridoX.length-1)){

			int posx = globales.COORD_X_INI_BLOQUES + recorridoX[bloquesQuitados]*globales.ANCHO_IMG;
			int posy = globales.COORD_Y_INI_BLOQUES + recorridoY[bloquesQuitados]*globales.ALTO_IMG;
			
			g2d.setColor(Color.BLACK);
			g2d.fillRect(posx, posy, globales.ANCHO_IMG, globales.ALTO_IMG);
			
			lab[recorridoX[bloquesQuitados]][recorridoY[bloquesQuitados]] = globales.LAB_VACIO;
			
			bloquesQuitados++;
		}
		else
			recorridoCompleto = true;
	}
	
	public void paintNuevosEnemigos(Graphics2D g2d){
		
		Image imagen = new ImageIcon(bloqueAzul).getImage();
		if(numAvisoNuevosEnemigos%2 != 0){
			imagen = new ImageIcon(bloqueEnemigo).getImage();
		}
		
		if(numAvisoNuevosEnemigos == 4){
			numAvisoNuevosEnemigos = 0;
			dibujarNuevosEnemigos = true;
		}
		else
			numAvisoNuevosEnemigos++;
		
		for(int i=0; i<nuevoEnemigoX.length; i++){
			
			int posx = globales.COORD_X_INI_BLOQUES + nuevoEnemigoX[i]*globales.ANCHO_IMG;
			int posy = globales.COORD_Y_INI_BLOQUES + nuevoEnemigoY[i]*globales.ALTO_IMG;
			
			g2d.setColor(Color.BLACK);
			g2d.fillRect(posx, posy, globales.ANCHO_IMG, globales.ALTO_IMG);
			
			g2d.drawImage(imagen, posx, posy, this);
		}
	}
	
	public void paintChange(Graphics2D g2d){		
		
		if(moverPared==1 && (( tmp.vecesEjecutado * (tmp.frames_per_sec/5) )%tmp.frames_per_sec )==0){
			if(movBorde != 4){
				g2d.drawImage(new ImageIcon(borde[indBorde]).getImage(), posx_bloque, posy_bloque, this);
				if(indBorde%2 == 0)
					indBorde++;
				else
					indBorde--;
				movBorde++;
			}
			else if(movBorde == 4){
				if(indBorde == 2 || indBorde == 3) //superior - inferior
					g2d.drawImage(new ImageIcon(borde[0]).getImage(), posx_bloque, posy_bloque, this);
				else if(indBorde == 4 || indBorde == 5) //derecho - izquierdo
					g2d.drawImage(new ImageIcon(borde[1]).getImage(), posx_bloque, posy_bloque, this);
				movBorde = 0;
				moverPared = 0;
			}
		}
		
		if(!cambiosRomperBloque.isEmpty() && (( tmp.vecesEjecutado * (tmp.frames_per_sec/5) )%tmp.frames_per_sec )==0){
			for(int i=0; i<cambiosRomperBloque.size(); i++){
				RomperBloque mov = cambiosRomperBloque.get(i);
				int posx = globales.COORD_X_INI_BLOQUES + mov.getChangeX()*globales.ANCHO_IMG;
				int posy = globales.COORD_Y_INI_BLOQUES + mov.getChangeY()*globales.ALTO_IMG;
				
				g2d.setColor(Color.BLACK);
				g2d.fillRect(posx, posy, globales.ANCHO_IMG, globales.ALTO_IMG);
				if(mov.getImg() < romperBloque.length){
					g2d.drawImage(new ImageIcon(romperBloque[mov.getImg()]).getImage(), posx, posy, this);
					mov.setImg(mov.getImg()+1);
				}
				else{
					cambiosRomperBloque.remove(i);
					i--;
				}
			}
		}
		
		if(!cambiosMoverBloque.isEmpty()){
			for(int i=0; i<cambiosMoverBloque.size(); i++){
				MoverBloque mov = cambiosMoverBloque.get(i);

				int ancho=globales.ANCHO_IMG, alto=globales.ALTO_IMG;
				int desfase_X=globales.COORD_X_INI_BLOQUES, desfase_Y=globales.COORD_Y_INI_BLOQUES;
				
				if ( mov.getPosX()!=mov.getMovX() && 
						((mov.getPosX()-mov.getMovX())%36==0 || (mov.getMovX()-mov.getPosX())%36==0) ){//mov VERTICAL
					for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
						if(enemigos[ind]!=null){
							
							int x_futura1=mov.getMoveFromX()+mov.getdX(), 
									x_futura2=mov.getMoveFromX()+2*mov.getdX(),
									y_futura=mov.getMoveFromY();
							int x_enem=posicion_enemigos[ind][0], y_enem=posicion_enemigos[ind][1];
							
							if(x_enem == x_futura1 && y_enem == y_futura){
								enemigos[ind].setMorir();
								enemigos_vivos--;
								if(x_futura1==mov.getMoveToX() && y_futura==mov.getMoveToY()){}
								else{
									cambiosMoverBloqueConEnemigo.add(new MoverBloqueConEnemigo(5, 
											desfase_X+(x_enem*ancho), desfase_Y+y_enem*alto, x_futura1, y_futura, 
											desfase_X+(mov.getMoveToX()*ancho), desfase_Y+(mov.getMoveToY()*alto), 
											mov.getMoveToX(), mov.getMoveToY(), 
											mov.getdX(), mov.getdY(), ind, enemigos[ind].getImgActual()));
								}
							}
							else if(x_enem == x_futura2 && y_enem == y_futura){
								enemigos[ind].setMorir();
								enemigos_vivos--;
								if(x_futura2==mov.getMoveToX() && y_futura==mov.getMoveToY()){}
								else{
									cambiosMoverBloqueConEnemigo.add(new MoverBloqueConEnemigo(5, 
											desfase_X+(x_enem*ancho), desfase_Y+y_enem*alto, x_futura2, y_futura, 
											desfase_X+(mov.getMoveToX()*ancho), desfase_Y+(mov.getMoveToY()*alto), 
											mov.getMoveToX(), mov.getMoveToY(), 
											mov.getdX(), mov.getdY(), ind, enemigos[ind].getImgActual()));
								}
							}
						}
					}
					mov.setMoveFromX((mov.getMoveFromX() + mov.getdX()));
					mov.setMoveFromY((mov.getMoveFromY() + mov.getdY()));
				}
				else if	( mov.getPosY()!=mov.getMovY() && 
						((mov.getPosY()-mov.getMovY())%37==0 || (mov.getMovY()-mov.getPosY())%37==0) ){//mov HORIZONTAL
					for(int ind=0;ind<globales.MAX_NUM_ENEMIGOS_SIMULT;ind++){
						if(enemigos[ind]!=null){
							
							int x_futura=mov.getMoveFromX(), 
									y_futura=mov.getMoveFromY()+mov.getdY(),
									y_futura2=mov.getMoveFromY()+2*mov.getdY();
							int x_enem=posicion_enemigos[ind][0], y_enem=posicion_enemigos[ind][1];							
							if(x_enem == x_futura && y_enem == y_futura){
								enemigos[ind].setMorir();
								setPosicionEnemigo(ind,x_enem,y_enem,-1,-1);
								cambiosMoverBloqueConEnemigo.add(new MoverBloqueConEnemigo(5, 
									desfase_X+(x_enem*ancho), desfase_Y+(y_enem*alto), x_futura, y_futura, 
									desfase_X+(mov.getMoveToX()*ancho), desfase_Y+(mov.getMoveToY()*alto), 
									mov.getMoveToX(), mov.getMoveToY(), 
									mov.getdX(), mov.getdY(), ind, enemigos[ind].getImgActual()));
							}
							else if(x_enem == x_futura && y_enem == y_futura2){
								enemigos[ind].setMorir();
								if(x_futura==mov.getMoveToX() && y_futura2==mov.getMoveToY()){}
								else{
									cambiosMoverBloqueConEnemigo.add(new MoverBloqueConEnemigo(5, 
											desfase_X+(x_enem*ancho), desfase_Y+y_enem*alto, x_futura, y_futura2, 
											desfase_X+(mov.getMoveToX()*ancho), desfase_Y+(mov.getMoveToY()*alto), 
											mov.getMoveToX(), mov.getMoveToY(), 
											mov.getdX(), mov.getdY(), ind, enemigos[ind].getImgActual()));
								}
							}
						}
					}
					mov.setMoveFromX((mov.getMoveFromX() + mov.getdX()));
					mov.setMoveFromY((mov.getMoveFromY() + mov.getdY()));
				}
				
				g2d.setColor(Color.BLACK);
				g2d.fillRect(mov.getPosX(), mov.getPosY(), globales.ANCHO_IMG, globales.ALTO_IMG);
				
				mov.setPosX(mov.getPosX() + mov.getdX());
				mov.setPosY(mov.getPosY() + mov.getdY());

				g2d.drawImage(getImageByNumber(mov.getBloqueAnt()), mov.getPosX(), mov.getPosY(), this);
				if( mov.getPosX()==mov.getMovX() && mov.getPosY()==mov.getMovY()){
					lab[mov.getMoveToX()][mov.getMoveToY()] = mov.getBloqueAnt();
					cambiosMoverBloque.remove(i);
					i--;
				}
			}
		}
		
		if(!cambiosMoverBloqueConEnemigo.isEmpty()){
			for(int i=0; i<cambiosMoverBloqueConEnemigo.size(); i++){
				MoverBloqueConEnemigo mov = cambiosMoverBloqueConEnemigo.get(i);
				if(mov.getPosX()!=mov.getMovX() || mov.getPosY()!=mov.getMovY()){
					g2d.setColor(Color.BLACK);
					g2d.fillRect(mov.getPosX(), mov.getPosY(), globales.ANCHO_IMG, globales.ALTO_IMG);
					mov.setPosX(mov.getPosX() + mov.getdX());
					mov.setPosY(mov.getPosY() + mov.getdY());
					g2d.drawImage(mov.getImagenEnemigo(), mov.getPosX(), mov.getPosY(), this);
					if( mov.getPosX()==mov.getMovX() && mov.getPosY()==mov.getMovY()){
						if(mov.getdX()>0){
							g2d.drawImage(enemigos[mov.getEnemigoId()].getImgMorir(4), mov.getPosX(), mov.getPosY(), this);
						} else if(mov.getdX()>0){
							g2d.drawImage(enemigos[mov.getEnemigoId()].getImgMorir(6), mov.getPosX(), mov.getPosY(), this);
						} else if(mov.getdX()>0){
							g2d.drawImage(enemigos[mov.getEnemigoId()].getImgMorir(0), mov.getPosX(), mov.getPosY(), this);
						} else if(mov.getdX()>0){
							g2d.drawImage(enemigos[mov.getEnemigoId()].getImgMorir(2), mov.getPosX(), mov.getPosY(), this);
						}
						lab[mov.getMoveToX()][mov.getMoveToY()] = mov.getBloqueAnt();
						cambiosMoverBloqueConEnemigo.remove(i);
						i--; 
						pingu.sumarPuntos(globales.PUNTOS_MATAR_ENEMIGO_BLOQUE);
					}
				}
				else{/*destino=origen, no mover más al enemigo*/}
			}
		}
	}
	
	public boolean laberintoIni(){
		return iniciado;
	}

	public boolean haveToChange() {
		return this.moverPared!=0 || !cambiosMoverBloque.isEmpty() || !cambiosRomperBloque.isEmpty();
	}

	public boolean recorridoCompleto() {
		return recorridoCompleto;
	}

	public boolean dibujarNuevosEnemigos() {
		return dibujarNuevosEnemigos;
	}

	public void setPinguino(Pinguino p) {
		pingu=p;
	}

	public void setEnemigo(int id, Enemigo en) {
		enemigos[id]=en;
	}

	public int getEnemigosVivos() {
		int vivos = 0;
		for(int i=0; i<enemigos.length; i++){
			if (enemigos[i]!=null && !enemigos[i].estaMuerto())
				vivos++;
		}
		return vivos;
	}
	
	public int[] getPosicionAleatoria(){
		for(int intentos=0;intentos<10;intentos++){
			int col = randInt(0,globales.ANCHO_LAB-1);
			for(int y=0;y<globales.ANCHO_LAB;y++){
				if(lab[col][y]==globales.LAB_BLOQUE_AZUL){
					 if( (y>0 && lab[col][y-1]== globales.LAB_VACIO) || 
							 (y<globales.ALTO_LAB && lab[col][y+1]==globales.LAB_VACIO) ){
						int[] ret = new int[2];
						ret[0]=col; ret[1]=y;
//						long ini2 = System.currentTimeMillis();
//						while(System.currentTimeMillis()-ini2<10000){}
						lab[col][y]=globales.LAB_VACIO;
						return ret;
					 }
				}
			}
		}
		return new int[]{-1,-1};
	}
	
	public static int randInt(int min, int max) {
	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}