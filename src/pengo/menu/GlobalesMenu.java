package pengo.menu;

public class GlobalesMenu {

	public int COORD_X_INI_START = 100;
	public int COORD_Y_INI_START = 150;
	
	public int ALTO_IMG_NUM = 23;
	public int ANCHO_IMG_NUM = 23;
	public int ANCHO_TOT_IMG_NUM = ANCHO_IMG_NUM * 2;
	public int ALTO_IMG = 25;
	
	public int ALTO_IMG_BONUS = 95;
	public int ANCHO_IMG_BONUS = 468;
	
	public int ALTO_IMG_START = 120;
	public int ANCHO_IMG_START = 320;
	
	public int ANCHO_IMG_CREDIT = 141;

	public int COORD_X_INI_PLAYER = COORD_X_INI_START;
	public int COORD_Y_INI_PLAYER = COORD_Y_INI_START + ALTO_IMG_START + 20;
	
	public int COORD_X_INI_CREDIT = COORD_X_INI_START;
	public int COORD_Y_INI_CREDIT = COORD_Y_INI_PLAYER + ALTO_IMG + 35;
	
	public int COORD_X_INI_NUM_CREDIT = COORD_X_INI_CREDIT + ANCHO_IMG_CREDIT + 36;

	public int COORD_X_INI_BONUS = COORD_X_INI_START - 80;
	public int COORD_Y_INI_BONUS = COORD_Y_INI_CREDIT + ALTO_IMG + 30;
	
}
