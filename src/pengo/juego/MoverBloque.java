package pengo.juego;

public class MoverBloque {
	
private int bloqueAnt;
	
	private int posX;
	private int posY;
	private int moveFromX;
	private int moveFromY;
	
	private int movX;
	private int movY;
	private int moveToX;
	private int moveToY;
	
	private int dX;
	private int dY;
		
	public MoverBloque(int bloqueAnt, int posX, int posY, int moveFromX, int moveFromY,
						int movX, int movY, int moveToX, int moveToY, int dx, int dy){
		this.bloqueAnt = bloqueAnt;
		this.posX = posX;
		this.posY = posY;
		this.moveFromX = moveFromX;
		this.moveFromY = moveFromY;
		this.movX = movX;
		this.movY = movY;
		this.moveToX = moveToX;
		this.moveToY = moveToY;
		this.dX = dx;
		this.dY = dy;
	}

	public int getBloqueAnt() {
		return bloqueAnt;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getMoveFromX() {
		return moveFromX;
	}

	public int getMoveFromY() {
		return moveFromY;
	}
	
	public void setMoveFromX(int x) {
		moveFromX=x;
	}

	public void setMoveFromY(int y) {
		moveFromY=y;
	}

	public int getMoveToX() {
		return moveToX;
	}

	public int getMoveToY() {
		return moveToY;
	}

	public int getMovX() {
		return movX;
	}

	public int getMovY() {
		return movY;
	}

	public int getdX() {
		return dX;
	}

	public int getdY() {
		return dY;
	}

	public void String(){
		System.out.println("Posicion actual en pixels del bloque [posx][posy] = [" + posX + "][" + posY + "]" );
		System.out.println("Pixeles a mover el bloque [movX][movY] = [" + movX + "][" + movY + "]" );
		System.out.println("Pixeles a mover el bloque cada paso [dX][dY] = [" + dX + "][" + dY + "]" );
		System.out.println("Posicion destino del bloque [moveToX][moveToY] = [" + moveToX + "][" + moveToY + "]" );
	}

}
