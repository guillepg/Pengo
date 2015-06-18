package pengo.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Temporizador;

public class MenuInicio extends JPanel{

	private static final long serialVersionUID = 1L;
	private String encabezado = "./img/menu/encabezado.png";
	private String sega = "./img/menu/sega.png";
	private Temporizador tmp;
	private boolean acabado;
	private int i, k, m, h, op, j, parar, high1, high2, avanzar, high, w, bRombo, xRombo, yRombo;
	
	private String bloqueAzul;
	private String[] stsb1;
	private String[] stsb2;
	private String[] stsb3;
	private String[] pengo;
	private String[] sb;
	private String[] ib1;
	private String[] ib2;
	private String[] bloqueRombo;
	private String[] db1;
	private String[] db2;
	private String[] enemAmarillo;
	private String[] ping;
	private String[] enemVerde;
	
	private String titulo = "./img/menu/titulo.png";
	
	public MenuInicio(Temporizador temp){
		tmp = temp;
		acabado = false;
		xRombo=yRombo=bRombo=i=k=m=parar=high=high1=high2=avanzar=w=0;
		h=200; op=0; j=90;
		
		iniciarImagenes();
	}
	
	public void iniciarImagenes(){
		stsb1 = new String[]{
				"./img/letras/squash_the_sno-bees/S.png",
				"./img/letras/squash_the_sno-bees/Q.png",
				"./img/letras/squash_the_sno-bees/U.png",
				"./img/letras/squash_the_sno-bees/A.png",
				"./img/letras/squash_the_sno-bees/S.png",
				"./img/letras/squash_the_sno-bees/H.png",			
			};
		stsb2 = new String[]{
				"./img/letras/squash_the_sno-bees/T.png",
				"./img/letras/squash_the_sno-bees/H.png",
				"./img/letras/squash_the_sno-bees/E.png",			
			};
		stsb3 = new String[]{
				"./img/letras/squash_the_sno-bees/S.png",
				"./img/letras/squash_the_sno-bees/N.png",
				"./img/letras/squash_the_sno-bees/O.png",
				"./img/letras/squash_the_sno-bees/-.png",
				"./img/letras/squash_the_sno-bees/B.png",
				"./img/letras/squash_the_sno-bees/E.png",	
				"./img/letras/squash_the_sno-bees/E.png",
				"./img/letras/squash_the_sno-bees/S.png",
			};
		
		pengo = new String[]{
				"./img/letras/pengo/P.png",
				"./img/letras/pengo/E.png",
				"./img/letras/pengo/N.png",
				"./img/letras/pengo/G.png",
				"./img/letras/pengo/O.png",			
			};
		
		sb = new String[]{
				"./img/letras/sno-bee/S.png",
				"./img/letras/sno-bee/N.png",
				"./img/letras/sno-bee/O.png",
				"./img/letras/sno-bee/-.png",
				"./img/letras/sno-bee/B.png",
				"./img/letras/sno-bee/E.png",
				"./img/letras/sno-bee/E.png",
			};
		
		ib1 = new String[]{
				"./img/letras/ice_block/I.png",
				"./img/letras/ice_block/C.png",
				"./img/letras/ice_block/E.png",
			};
		
		ib2 = new String[]{
				"./img/letras/ice_block/B.png",
				"./img/letras/ice_block/L.png",
				"./img/letras/ice_block/O.png",
				"./img/letras/ice_block/C.png",
				"./img/letras/ice_block/K.png",
			};
		
		bloqueAzul = "./img/laberinto/bloque_azul.png";
		
		bloqueRombo = new String[]{
				"./img/laberinto/rombo/bloque_rombo_01.png",
				"./img/laberinto/rombo/bloque_rombo_02.png",
		};
		
		db1 = new String[]{
				"./img/letras/diamond_block/D.png",
				"./img/letras/diamond_block/I.png",
				"./img/letras/diamond_block/A.png",
				"./img/letras/diamond_block/M.png",
				"./img/letras/diamond_block/O.png",
				"./img/letras/diamond_block/N.png",
				"./img/letras/diamond_block/D.png",
			};
		
		db2 = new String[]{
				"./img/letras/diamond_block/B.png",
				"./img/letras/diamond_block/L.png",
				"./img/letras/diamond_block/O.png",
				"./img/letras/diamond_block/C.png",
				"./img/letras/diamond_block/K.png",
			};
		
		enemAmarillo = new String[]{
				"./img/enemigo_amarillo/andar/e_der_1.png",
				"./img/enemigo_amarillo/andar/e_der_2.png",
		};
		
		ping = new String[]{
				"./img/pinguino_rojo/andar/p_der_01.png",
				"./img/pinguino_rojo/andar/p_der_02.png",
		};
		
		enemVerde = new String[]{
				"./img/enemigo_verde/andar/e_der_1.png",
				"./img/enemigo_verde/andar/e_der_2.png",
		};
	}
	
	public void menu(Graphics2D g2d) {
		if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/3) )%tmp.frames_per_sec )==0 ){
        	switch(op){
        	case 0:
        		g2d.drawImage(new ImageIcon(encabezado).getImage(), 0, 0, this);
                g2d.drawImage(new ImageIcon(sega).getImage(), 180, 550, this);
                op++;
        		break;
        	case 1:
        		if(high==0) { high = h-20; w = j; }
        		mstsb(g2d);
        		break;
        	case 2:
        		if(parar==0) {
        			parar = j-10; high1 = h-20;
        		}
        		mpengo(g2d);
        		break;
        	case 3:
        		if(high2==0) {
        			high2 = h-20;
        		}
        		msb(g2d);
        		break;
        	case 4:
        		imb1(g2d);
        		break;
        	case 5:
        		mib(g2d);
        		break;
        	case 6:
        		xRombo = j;
        		yRombo = h;
        		imb2(g2d);
        		h+=20; j=208; op++;
        		break;
        	case 7: 
        		mdb(g2d);
        		imb2(g2d);
        		break;
        	case 8:
        		avanzar++;
        		if(avanzar%5==0) dib(g2d);
        		imb2(g2d);
        		break;
        	default: 
        		try {
    				Thread.sleep(2000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
            	setAcabado(true);
            	break;
        	}
        }
	}
	
	private void mstsb(Graphics2D g2d){
		if(i<stsb1.length){
            g2d.drawImage(new ImageIcon(stsb1[i]).getImage(), j, h, this);
            i++; j += 18;
        }
        else if(k<stsb2.length){
        	if(k==0) j+=8;
            g2d.drawImage(new ImageIcon(stsb2[k]).getImage(), j, h, this);
            k++; j += 18;
        }
        else if(m<stsb3.length){
        	if(m==0) j+=6;
            g2d.drawImage(new ImageIcon(stsb3[m]).getImage(), j, h, this);
            m++;  j += 18;
            if(m==stsb3.length) { i = 0; k = 0; m = 0; j = 208; h+=80; op++; }
        }
	}

	private void mpengo(Graphics2D g2d){
    	g2d.drawImage(new ImageIcon(pengo[i]).getImage(), j, h, this);
        i++; j += 18;
        if(i==pengo.length){ h+=60; j=208; op++; i=0;}
	}
	
	private void msb(Graphics2D g2d){
		g2d.drawImage(new ImageIcon(sb[k]).getImage(), j, h, this);
        k++; j += 18;
        if(k==sb.length){ h+=40; j=150; op++; k=0;}
	}

	private void imb1(Graphics2D g2d) {
		g2d.drawImage(new ImageIcon(bloqueAzul).getImage(), j, h, this);
		h+=20; j=208; op++;
	}
	
	private void mib(Graphics2D g2d) {
		if(i<ib1.length){
            g2d.drawImage(new ImageIcon(ib1[i]).getImage(), j, h, this);
            i++; j += 18;
        }
        else if(k<ib2.length){
        	if(k==0) j+=8;
            g2d.drawImage(new ImageIcon(ib2[k]).getImage(), j, h, this);
            k++; j += 18;
        }
        else { h+=60; j=150; op++; i=0; k=0; }
	}

	private void imb2(Graphics2D g2d) {
		if( (( tmp.vecesEjecutado * (tmp.frames_per_sec/7) )%tmp.frames_per_sec )==0 ){
			g2d.drawImage(new ImageIcon(bloqueRombo[bRombo%2]).getImage(), xRombo, yRombo, this);
			bRombo++;
		}
	}
	
	private void mdb(Graphics2D g2d) {
		if(i<db1.length){
            g2d.drawImage(new ImageIcon(db1[i]).getImage(), j, h, this);
            i++; j += 18;
        }
        else if(k<db2.length){
        	if(k==0) j+=8;
            g2d.drawImage(new ImageIcon(db2[k]).getImage(), j, h, this);
            k++; j += 18;
        }
        else { h+=60; j=208; op++; i=0; k=0; j=0; }
	}
	
	private void dib(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(k-20,high1,36,37);
		g2d.fillRect(k-20,high2,36,37);
		g2d.drawImage(new ImageIcon(ping[i%2]).getImage(), k, high1, this);
		g2d.drawImage(new ImageIcon(enemVerde[i%2]).getImage(), k, high2, this);
		i++; 
		if(k<=parar-50) k+=10;
		else if(j<=w-40){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(j-36,high,36,37);
			g2d.drawImage(new ImageIcon(enemAmarillo[i%2]).getImage(), j, high, this);
			j+=18;
		} else if (j<=510){
			g2d.setColor(Color.BLACK);
			g2d.fillRect(j-36,high,36,37);
			g2d.drawImage(new ImageIcon(enemAmarillo[i%2]).getImage(), j, high, this);
			j+=18;
		} else {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(j-36,high,36,37);
			g2d.drawImage(new ImageIcon(titulo).getImage(), w, 60, this);
			op++;
		}
	}
	
	public boolean isAcabado() {
		return acabado;
	}

	private void setAcabado(boolean acabado) {
		this.acabado = acabado;
	}    
}
