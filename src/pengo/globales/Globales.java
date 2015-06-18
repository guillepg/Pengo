package pengo.globales;

public class Globales {

	public int ANCHO_LAB = 13;
	public int ALTO_LAB = 15;
	
	public int ANCHO_IMG = 36;
	public int ALTO_IMG = 37;

	public int ANCHO_IMG_VIDAS_ENEM = 18;
	public int ANCHO_IMG_NUM_PTOS = 18;
	public int ALTO_IMG_NUM_PTOS = 18;
	
	public int ALTO_IMG_PUNTOS = 18; 
	public int COORD_Y_INI_PUNTOS = 3;
	
	public int ANCHO_BORDE = 18;
	
	public int COORD_X_INI_BLOQUES = 22;
	public int COORD_Y_INI_BLOQUES = 80;

	public int COORD_X_FIN_PUNTOS = COORD_X_INI_BLOQUES + ANCHO_IMG*4;
	
	public int COORD_X_FIN_BLOQUES = COORD_X_INI_BLOQUES + (ANCHO_LAB*ANCHO_IMG);
	public int COORD_Y_FIN_BLOQUES = COORD_Y_INI_BLOQUES + (ALTO_LAB*ALTO_IMG);
	
	public int MOV_DER_SIG_BLOQUE = 14;
	public int MOV_IZQ_SIG_BLOQUE = -23;
	public int MOV_ARRIBA_SIG_BLOQUE = -7;
	public int MOV_ABAJO_SIG_BLOQUE = 31;
	
	//Borde superior
	public int COORD_X_BORDE_SUP_INF_IZQ = COORD_X_INI_BLOQUES - ANCHO_BORDE;
	public int COORD_Y_BORDE_SUP = COORD_Y_INI_BLOQUES - ANCHO_BORDE;
	
	//Borde inferior
	public int COORD_Y_BORDE_INF = COORD_Y_INI_BLOQUES + (ALTO_IMG*ALTO_LAB);
	
	//Borde izquierdo
	public int COORD_Y_BORDE_IZQ_DER = COORD_Y_INI_BLOQUES;

	//Borde derecho
	public int COORD_X_BORDE_DER = COORD_X_INI_BLOQUES + (ANCHO_IMG*ANCHO_LAB);
	
	//Varios de abajo
	public int COORD_Y_VARIOS = COORD_Y_INI_BLOQUES + (ALTO_IMG*ALTO_LAB) + ANCHO_BORDE;
	public int COORD_X_ACT = COORD_X_INI_BLOQUES;
	//Pinguino amarillo con banderita
	public int COORD_X_PNIVEL = COORD_X_INI_BLOQUES + (4*ANCHO_IMG);
	//Logo sega
	public int COORD_X_LOGO = COORD_X_INI_BLOQUES + (7*ANCHO_IMG) + ANCHO_BORDE;
	public int COORD_X_NUM_NIVEL = COORD_X_INI_BLOQUES + (2*ANCHO_IMG);
	
	public int COORD_Y_VIDAS = COORD_Y_INI_BLOQUES - 55;
	public int COORD_X_VIDAS_P = COORD_X_BORDE_SUP_INF_IZQ;
	public int COORD_X_VIDAS_ENEM = COORD_X_INI_BLOQUES + (6*ANCHO_IMG) + ANCHO_IMG_VIDAS_ENEM;
	
	public int IMG_INI_MOVER_ABAJO = 0; 
	public int IMG_INI_MOVER_ARRIBA = 2; 
	public int IMG_INI_MOVER_DER = 4; 
	public int IMG_INI_MOVER_IZQ = 6; 
	
	public int LAB_VACIO = 0;
	public int LAB_BLOQUE_AZUL = 1;
	public int LAB_DIAMANTE = 2;
	public int LAB_BLOQUE_ENEM = 3;
	public int LAB_PINGUINO = 4;
	public int LAB_ENEMIGO = 5;
	
	public int MAX_NUM_ENEMIGOS_SIMULT = 2;
	public int MAX_NUM_ENEMIGOS = 6;
	public int PUNTOS_MATAR_ENEMIGO_BLOQUE = 400;
	public int PUNTOS_MATAR_ENEMIGO_MAREO = 100;
}
