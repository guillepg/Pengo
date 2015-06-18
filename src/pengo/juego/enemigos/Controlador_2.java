package pengo.juego.enemigos;

import java.util.Random;
import java.util.Vector;

import pengo.globales.Globales;

import java.awt.event.KeyEvent;

public class Controlador_2 extends Thread {
	private Enemigo enemy;
	int movX=0, movY=0, dirX=0, dirY=0, img=0; 
	static Random rand = new Random();
	static Globales g=new Globales();
	public Controlador_2(Enemigo enem){
		enemy=enem;
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		boolean saltar_random=false; 
		int keyCode=0;
		Globales g = new Globales();
		KeyEvent e = null;
		Vector<Integer> libres = null;
		while(!enemy.getAparecido()){
			/*esperamos a que aparezca el tablero completo*/
			System.out.print("");
		}
		while(!enemy.estaMuerto()){
			//movimiento inicial
			if(!saltar_random){
				switch(randInt(1,4)){
				case 1: //mover abajo
					movX=0;   movY=g.MOV_ABAJO_SIG_BLOQUE; 
					dirX=0;  dirY=1;
					keyCode = KeyEvent.VK_DOWN; 
					break; 
				case 2: //mover arriba
					movX=0;   movY=g.MOV_ARRIBA_SIG_BLOQUE; 
					dirX=0;  dirY=-1;
					keyCode = KeyEvent.VK_UP; 
					break; 
				case 3: //mover derecha
					movX=g.MOV_DER_SIG_BLOQUE;  movY=0;  	
					dirX=1;  dirY=0;
					keyCode = KeyEvent.VK_RIGHT; 
					break; 
				case 4: //mover izquierda
					movX=g.MOV_IZQ_SIG_BLOQUE; movY=0;  	
					dirX=-1; dirY=0;
					keyCode = KeyEvent.VK_LEFT;
					break; 
				} //ultimo = último movimiento realizado (0-> abajo, 1-> arriba, 2-> der, 3-> izq)
			}else{ saltar_random=false; } 
			
			e = new KeyEvent(enemy, 0, 0, 0, keyCode);
			long ini2 = System.currentTimeMillis();
			int[] pos_enem = enemy.getLab().getPosicionEnemigo(enemy.getId());
			while(TestCondiciones(movX, movY) && !enemy.estaMuerto()){	
				//mantener mientras no haya obstaculo
				if(enemy.getMareado()){ ini2 = System.currentTimeMillis();}
				else{
					if (enemy.keyListIsEmpty())	{
						enemy.keyPressed(e);
						enemy.keyReleased(e);
						pos_enem = enemy.getLab().getPosicionEnemigo(enemy.getId());
						ini2 = System.currentTimeMillis();
					}else {
						if(System.currentTimeMillis()-ini2>500 && 
								pos_enem[0]==enemy.getLab().getPosicionEnemigo(enemy.getId())[0] && 
								pos_enem[1]==enemy.getLab().getPosicionEnemigo(enemy.getId())[1]){
//							enemy.resetKeyList();
							ini2 = System.currentTimeMillis();
						}
					}
				}
			}
			libres = new Vector<Integer>();
			int[] pos_pengo = enemy.getLab().getPosicionPinguino();
			int[] pos_actual = enemy.getLab().getPosicionEnemigo(enemy.getId());
			//comprobaciones si hay libre en alguna dirección, y además se acerca yendo hacia ahí
			
			if((pos_actual[1]<pos_pengo[1] ) && TestCondiciones(0,g.MOV_ABAJO_SIG_BLOQUE)){
				libres.add(KeyEvent.VK_DOWN);
			}
			if((pos_actual[1]>pos_pengo[1] ) && TestCondiciones(0,g.MOV_ARRIBA_SIG_BLOQUE)){
				libres.add(KeyEvent.VK_UP);
			}
			if((pos_actual[0]<pos_pengo[0] ) && TestCondiciones(0,g.MOV_DER_SIG_BLOQUE)){
				libres.add(KeyEvent.VK_RIGHT); 
			}
			if((pos_actual[0]>pos_pengo[0] ) && TestCondiciones(0,g.MOV_IZQ_SIG_BLOQUE)){
				libres.add(KeyEvent.VK_LEFT);
			}
			if(libres.size()==0){ //en este caso no hay ningún camino libre desde el punto actual
				if(enemy.getLab().hayPared(enemy.getX(movX), enemy.getY(movY))){
					//hay pared -> movimiento random
				}
				else if(enemy.getLab().hayBloque(enemy.getX(movX), enemy.getY(movY))){ 
					//bloque -> cambiar/romper
					if(enemy.puedeRomper()){/*puede romper*/
						int res = randInt(1,5);
						if (res<5){//80% de prob de romper bloque, con un 50% apenas rompia
							int old_keyCode= keyCode;
							keyCode = KeyEvent.VK_SPACE;
							enemy.keyPressed(new KeyEvent(enemy, 0, 0, 0, keyCode));
							enemy.keyReleased(new KeyEvent(enemy, 0, 0, 0, keyCode));
							keyCode = old_keyCode;
							saltar_random = true;
						}
					}//fin if puede romper	
					
				}
			} else {//hay algun camino libre
				int resul = randInt(0,libres.size()-1);
				keyCode=libres.get(resul);
				if(keyCode==KeyEvent.VK_DOWN){
					movX=0;   movY=g.MOV_ABAJO_SIG_BLOQUE; dirX=0;  dirY=1; 
				}else if(keyCode==KeyEvent.VK_UP){
					movX=0;   movY=g.MOV_ARRIBA_SIG_BLOQUE; dirX=0;  dirY=-1; 
				}else if(keyCode==KeyEvent.VK_LEFT){
					movX=g.MOV_IZQ_SIG_BLOQUE; movY=0;  dirX=-1; dirY=0; 
				}else if(keyCode==KeyEvent.VK_RIGHT){
					movX=g.MOV_DER_SIG_BLOQUE;  movY=0;  dirX=1;  dirY=0; 
				}
				if(TestCondiciones(movX,movY)){	
					saltar_random=true; 
				}
				
			}
		}
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {
	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	private boolean TestCondiciones(int movX, int movY){
		return (enemy.getX(movX)>-1 && enemy.getY(movY)>-1 && 
				enemy.getX(movX)<g.ANCHO_LAB && enemy.getY(movY)<g.ALTO_LAB &&
				!enemy.getLab().hayBloque(enemy.getX(movX), enemy.getY(movY)) && 
				!enemy.getLab().hayPared(enemy.getX(movX), enemy.getY(movY)) 
		);
	}
	
	public int getIdEnemigo(){
		return enemy.getId();
	}
}
