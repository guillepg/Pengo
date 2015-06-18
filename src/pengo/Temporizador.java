package pengo;

public class Temporizador {
	

	private long nanoSeg_inSec = 1000000000;
	public int frames_per_sec = 90;
	//Cada cuantos nanosegundos hay que actualizar el tablero
	public long actualizar = nanoSeg_inSec/(long)frames_per_sec;
	
	public int vecesEjecutado = 0;
	
}
