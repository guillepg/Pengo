package pengo.juego;

public class RomperBloque {
	
	private int changeX;
	private int changeY;
	
	private int img;
	
	public RomperBloque(int changeX, int changeY, int img){
		this.changeX = changeX;
		this.changeY = changeY;
		this.img = img;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public int getChangeX() {
		return changeX;
	}

	public int getChangeY() {
		return changeY;
	}

	public void String(){
		System.out.println("Posicion del bloque [changeX][changeY] = [" + changeX + "][" + changeY + "]" );
	}

}
