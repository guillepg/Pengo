package pengo.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pengo.Pengo;
import pengo.Temporizador;

public class MenuFin extends JPanel{

	private static final long serialVersionUID = 1L;
	private String encabezado = "./img/menu/encabezado.png";
	public String[] tfp1 = {
			"./img/letras/thanks_for_playing/T.png",
			"./img/letras/thanks_for_playing/H.png",
			"./img/letras/thanks_for_playing/A.png",
			"./img/letras/thanks_for_playing/N.png",
			"./img/letras/thanks_for_playing/K.png",
			"./img/letras/thanks_for_playing/S.png",			
		};
	
	public String[] tfp2 = {
			"./img/letras/thanks_for_playing/F.png",
			"./img/letras/thanks_for_playing/O.png",
			"./img/letras/thanks_for_playing/R.png",
		};
	
	public String[] tfp3 = {
			"./img/letras/thanks_for_playing/P.png",
			"./img/letras/thanks_for_playing/L.png",
			"./img/letras/thanks_for_playing/A.png",
			"./img/letras/thanks_for_playing/Y.png",
			"./img/letras/thanks_for_playing/I.png",
			"./img/letras/thanks_for_playing/N.png",
			"./img/letras/thanks_for_playing/G.png",
			"./img/letras/thanks_for_playing/PUNTO.png",
		};
	
	public String[] tom1 = {
			"./img/letras/try_once_more/T.png",
			"./img/letras/try_once_more/R.png",
			"./img/letras/try_once_more/Y.png",	
		};
	public String[] tom2 = {
			"./img/letras/try_once_more/O.png",
			"./img/letras/try_once_more/N.png",
			"./img/letras/try_once_more/C.png",
			"./img/letras/try_once_more/E.png",
		};
	public String[] tom3 = {
			"./img/letras/try_once_more/M.png",
			"./img/letras/try_once_more/O.png",
			"./img/letras/try_once_more/R.png",
			"./img/letras/try_once_more/E.png",
			"./img/letras/try_once_more/EXCLAMACION.png",	
		};

    private int i=0, k=0, m=0;
    
    private int j = 25;
    private int yTFP = 260;
    private int yTOM = 320;
    private boolean acabado;
    private Temporizador t;
    int op;
    
    public MenuFin(Temporizador tmp){
         t=tmp;
         acabado = false;
         op = 0;
    }
     
    public void fin(Graphics2D g2d) {
    	if( (( t.vecesEjecutado * (t.frames_per_sec/3) )%t.frames_per_sec )==0 ){
    	//if(t.vecesEjecutado%2==0){
	    	switch(op){
	    	case 0: 
	    		g2d.drawImage(new ImageIcon(encabezado).getImage(), 0, 0, this);
	            op++;
	            break;
	    	case 1:
	    		mtfp1(g2d);
	    		break;
	    	case 2:
	    		mtfp2(g2d);
	    		break;
	    	case 3:
	    		mtfp3(g2d);
	    		break;
	    	case 4:
	    		mtom1(g2d);
	    		break;
	    	case 5:
	    		mtom2(g2d);
	    		break;
	    	case 6:
	    		mtom3(g2d);
	    		break;
	    	default:
	    		setAcabado(true);
            	try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		break;
	    	}
    	}
    }
    
    private void mtom3(Graphics2D g2d) {
    	if(m==0) j+=15;
        g2d.drawImage(new ImageIcon(tom3[m]).getImage(), j, yTOM, this);
        j += 23; m++;
    	if(m==tom3.length) op++;
	}

	private void mtom2(Graphics2D g2d) {
		if(k==0) j+=15;
        g2d.drawImage(new ImageIcon(tom2[k]).getImage(), j, yTOM, this);
        k++; j += 23;
        if(k==tom2.length) op++;
	}

	private void mtom1(Graphics2D g2d) {
		g2d.drawImage(new ImageIcon(tom1[i]).getImage(), j, yTOM, this);
        i++; j += 23;
        if(i==tom1.length) op++;  
	}

	private void mtfp3(Graphics2D g2d) {
    	if(m==0) j+=20;
        g2d.drawImage(new ImageIcon(tfp3[m]).getImage(), j, yTFP, this);
        m++;
        j += 25;
        if(m==tfp3.length) { i = 0; k = 0; m = 0; j = 120; op++; }
	}

	private void mtfp2(Graphics2D g2d) {
		if(k==0) j+=20;
        g2d.drawImage(new ImageIcon(tfp2[k]).getImage(), j, yTFP, this);
        k++;
        j += 25;
        if(k==tfp2.length) op++;
	}

	private void mtfp1(Graphics2D g2d) {
        g2d.drawImage(new ImageIcon(tfp1[i]).getImage(), j, yTFP, this);
        i++;
        j += 25;
        if(i==tfp1.length) op++;
	}

	public boolean isAcabado() {
		return acabado;
	}

	public void setAcabado(boolean acabado) {
		this.acabado = acabado;
	}      
}
